"use client";

import { useCallback, useEffect, useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  type KisDailyPrice,
  type ChartDataPoint,
  type ChartPeriod,
  type KisAnalysisResponse,
  CHART_PERIODS,
  getStartDate,
  getTodayDate,
} from "@/lib/kis";

// ─── Types ───────────────────────────────────────────────────────────

interface KisCredentialsInput {
  appkey: string;
  appsecret: string;
  accountNumber: string;
  environment: "practice" | "production";
}

// ─── useKisCredentials ───────────────────────────────────────────────

export function useKisCredentials() {
  const queryClient = useQueryClient();

  const { data, isLoading } = useQuery<{
    connected: boolean;
    environment?: string;
  }>({
    queryKey: ["kis-credentials"],
    queryFn: async () => {
      const res = await fetch("/api/kis/credentials");
      if (!res.ok) return { connected: false };
      return res.json();
    },
    staleTime: Infinity,
  });

  const saveCredentials = useCallback(
    async (creds: KisCredentialsInput) => {
      const res = await fetch("/api/kis/credentials", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(creds),
      });
      if (res.ok) {
        queryClient.invalidateQueries({ queryKey: ["kis-credentials"] });
        queryClient.invalidateQueries({ queryKey: ["kis-token"] });
      }
      return res.ok;
    },
    [queryClient],
  );

  const clearCredentials = useCallback(async () => {
    await fetch("/api/kis/credentials", { method: "DELETE" });
    queryClient.invalidateQueries({ queryKey: ["kis-credentials"] });
    queryClient.invalidateQueries({ queryKey: ["kis-token"] });
  }, [queryClient]);

  return {
    isConnected: data?.connected ?? false,
    environment: data?.environment,
    isLoading,
    saveCredentials,
    clearCredentials,
  };
}

// ─── useKisToken ─────────────────────────────────────────────────────

export function useKisToken(isConnected: boolean) {
  return useQuery<{ valid: boolean }>({
    queryKey: ["kis-token"],
    queryFn: async () => {
      const res = await fetch("/api/kis/token", { method: "POST" });
      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.error || "Token request failed");
      }
      return res.json();
    },
    enabled: isConnected,
    staleTime: 23 * 60 * 60 * 1000,
    retry: 3,
    retryDelay: (attempt) => Math.min(1000 * 2 ** attempt, 30000),
  });
}

/** 실시간(푸시) 시세 한 건 */
export interface KisRealtimeTick {
  market: "KR" | "US";
  price: string;
  change: string;
  changePercent: string;
  sign: string;
}

// ─── useKisRealtimePrice (WebSocket → SSE 실시간 푸시, 국내+해외) ─────────

export function useKisRealtimePrice(
  stockCode: string | undefined,
  enabled: boolean,
  market: "KR" | "US" = "KR",
) {
  const [tick, setTick] = useState<KisRealtimeTick | null>(null);
  const [isStreaming, setIsStreaming] = useState(false);

  useEffect(() => {
    if (!enabled || !stockCode) {
      setTick(null);
      setIsStreaming(false);
      return;
    }
    if (market === "KR" && !/^\d{6}$/.test(stockCode)) {
      setTick(null);
      setIsStreaming(false);
      return;
    }

    const url = `${typeof window !== "undefined" ? window.location.origin : ""}/api/kis/realtime?stockCode=${encodeURIComponent(stockCode)}&market=${market}`;
    const es = new EventSource(url);

    es.onopen = () => setIsStreaming(true);
    es.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data) as {
          error?: string;
          market?: "KR" | "US";
          price?: string;
          change?: string;
          changePercent?: string;
          sign?: string;
        };
        if (data.error) return;
        if (data.price != null && data.market) {
          setTick({
            market: data.market,
            price: String(data.price),
            change: data.change ?? "",
            changePercent: data.changePercent ?? "",
            sign: data.sign ?? "",
          });
        }
      } catch {
        // ignore
      }
    };
    es.onerror = () => {
      setIsStreaming(false);
      es.close();
    };

    return () => {
      es.close();
      setIsStreaming(false);
      setTick(null);
    };
  }, [enabled, stockCode, market]);

  return {
    realtimePrice: tick?.price ?? null,
    realtimeChange: tick?.change ?? null,
    realtimeChangePercent: tick?.changePercent ?? null,
    realtimeSign: tick?.sign ?? null,
    realtimeMarket: tick?.market ?? null,
    isStreaming,
  };
}

// ─── useKisPrice ─────────────────────────────────────────────────────

export function useKisPrice(
  stockCode: string | undefined,
  isConnected: boolean,
  tokenValid: boolean,
) {
  return useQuery({
    queryKey: ["kis-price", stockCode],
    queryFn: async () => {
      if (!stockCode) throw new Error("No stock code");

      const res = await fetch("/api/kis/price", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ stockCode }),
      });

      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.error || "Price request failed");
      }

      return res.json();
    },
    enabled: isConnected && tokenValid && !!stockCode,
    staleTime: 30 * 1000,           // 30초간 캐시 (실시간 SSE가 있으므로 자주 폴링할 필요 없음)
    refetchInterval: 60 * 1000,     // 60초마다 갱신 (이전: 15초 → 제한 초과 방지)
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: true,
    retry: 2,
    retryDelay: (attempt) => Math.min(2000 * 2 ** attempt, 30000),
  });
}

// ─── useKisExchangeRate (검색한 종목 기준 KIS 실시간 환율) ─────────────

export interface KisExchangeRateData {
  rate: number | null;
  change: number | null;
  changePercent: number | null;
  source: string;
  realtime?: boolean;
  message?: string;
}

export function useKisExchangeRate(
  stockCode: string | undefined,
  market: "KR" | "US",
  isConnected: boolean,
  tokenValid: boolean,
) {
  return useQuery<KisExchangeRateData>({
    queryKey: ["kis-exchange-rate", stockCode, market],
    queryFn: async () => {
      const params = new URLSearchParams({ market });
      if (market === "US" && stockCode) params.set("stockCode", stockCode);
      const res = await fetch(`/api/kis/exchange-rate?${params}`);
      const data = await res.json().catch(() => ({}));
      if (!res.ok) {
        throw new Error(data.error || "환율 조회에 실패했습니다.");
      }
      return {
        rate: data.rate ?? null,
        change: data.change ?? null,
        changePercent: data.changePercent ?? null,
        source: data.source ?? "한국투자증권",
        realtime: data.realtime ?? false,
        message: data.message,
      };
    },
    enabled: isConnected && tokenValid && !!stockCode,
    staleTime: 5 * 60 * 1000,      // 5분간 캐시 (환율은 자주 안 바뀜)
    refetchInterval: 5 * 60 * 1000, // 5분마다 갱신 (이전: 60초)
    retry: 1,
    retryDelay: 2000,
  });
}

// ─── useKisChart ─────────────────────────────────────────────────────

export function useKisChart(
  stockCode: string | undefined,
  period: ChartPeriod,
  isConnected: boolean,
  tokenValid: boolean,
) {
  return useQuery<ChartDataPoint[]>({
    queryKey: ["kis-chart", stockCode, period],
    queryFn: async () => {
      if (!stockCode) throw new Error("No stock code");

      const periodConfig = CHART_PERIODS.find((p) => p.key === period);
      if (!periodConfig) throw new Error("Invalid period");

      const res = await fetch("/api/kis/chart", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          stockCode,
          startDate: getStartDate(period),
          endDate: getTodayDate(),
          periodDivCode: periodConfig.fid_period_div_code,
        }),
      });

      if (!res.ok) {
        const err = await res.json();
        throw new Error(err.error || "Chart request failed");
      }

      const items: KisDailyPrice[] = await res.json();

      return items
        .map((item) => ({
          time: `${item.stck_bsop_date.slice(0, 4)}-${item.stck_bsop_date.slice(4, 6)}-${item.stck_bsop_date.slice(6, 8)}`,
          value: Number(item.stck_clpr),
        }))
        .sort((a, b) => a.time.localeCompare(b.time));
    },
    enabled: isConnected && tokenValid && !!stockCode,
    staleTime: 5 * 60 * 1000,
    retry: 3,
    retryDelay: (attempt) => Math.min(1000 * 2 ** attempt, 30000),
  });
}

// ─── useKisOverseasPrice (해외 현재가 → 상세 패널) ─────────────────────────

export interface KisOverseasPriceOutput {
  last?: string;
  prdy_vrss?: string;
  prdy_ctrt?: string;
  open?: string;
  high?: string;
  low?: string;
  acml_vol?: string;
  [key: string]: unknown;
}

export function useKisOverseasPrice(
  stockCode: string | undefined,
  isConnected: boolean,
  tokenValid: boolean,
) {
  return useQuery<KisOverseasPriceOutput | null>({
    queryKey: ["kis-overseas-price", stockCode],
    queryFn: async () => {
      if (!stockCode) return null;
      const res = await fetch(
        `/api/kis/overseas-price?stockCode=${encodeURIComponent(stockCode)}&exchange=NAS`,
        { credentials: "include" },
      );
      if (!res.ok) return null;
      return res.json();
    },
    enabled: isConnected && tokenValid && !!stockCode,
    staleTime: 60 * 1000,          // 60초간 캐시 (이전: 30초, 실시간 SSE 보완)
    retry: 2,
    retryDelay: 2000,
  });
}

// ─── useKisOverseasChart (해외 일/주/월봉 → 차트) ────────────────────────

const OVERSEAS_GUBN: Record<ChartPeriod, string> = {
  "1D": "0",
  "1W": "1",
  "1M": "2",
  "3M": "2",
  "1Y": "2",
};

/** 해외 차트 API 응답 (chartData + 보조 데이터) */
export interface OverseasChartResponse {
  chartData: ChartDataPoint[];
  latestOHLC: { open?: number | null; high?: number | null; low?: number | null } | null;
  periodHigh: number | null;
  periodLow: number | null;
}

export function useKisOverseasChart(
  stockCode: string | undefined,
  period: ChartPeriod,
  isConnected: boolean,
  tokenValid: boolean,
) {
  return useQuery<OverseasChartResponse>({
    queryKey: ["kis-overseas-chart", stockCode, period],
    queryFn: async () => {
      if (!stockCode) throw new Error("No stock code");
      const res = await fetch("/api/kis/overseas-chart", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          stockCode,
          exchange: "NAS",
          bymd: getTodayDate(),
          gubn: OVERSEAS_GUBN[period] ?? "0",
        }),
        credentials: "include",
      });
      if (!res.ok) {
        const err = await res.json().catch(() => ({}));
        throw new Error((err as { error?: string }).error || "해외 차트 조회 실패");
      }
      const data = await res.json();
      // 새 형식: { chartData, latestOHLC, periodHigh, periodLow }
      if (data && typeof data === "object" && Array.isArray(data.chartData)) {
        return data as OverseasChartResponse;
      }
      // 이전 형식 호환 (배열 직접 반환)
      if (Array.isArray(data)) {
        return { chartData: data, latestOHLC: null, periodHigh: null, periodLow: null };
      }
      return { chartData: [], latestOHLC: null, periodHigh: null, periodLow: null };
    },
    enabled: isConnected && tokenValid && !!stockCode,
    staleTime: 5 * 60 * 1000,
    retry: 2,
    retryDelay: 1000,
  });
}

// ─── useAnalysisQuota (무료 N회 → 유료 BM) ───────────────────────────────

const ANALYSIS_QUOTA_KEY = "analysisFreeRemaining";
/** 테스트용 100회, 운영 시 3 등으로 변경 */
const DEFAULT_FREE_COUNT = 100;

function getQuotaFromStorage(): number {
  if (typeof window === "undefined") return DEFAULT_FREE_COUNT;
  try {
    const v = localStorage.getItem(ANALYSIS_QUOTA_KEY);
    if (v == null) return DEFAULT_FREE_COUNT;
    const n = parseInt(v, 10);
    return Number.isNaN(n) || n < 0 ? 0 : n;
  } catch {
    return DEFAULT_FREE_COUNT;
  }
}

function setQuotaToStorage(value: number): void {
  if (typeof window === "undefined") return;
  try {
    localStorage.setItem(ANALYSIS_QUOTA_KEY, String(Math.max(0, value)));
  } catch {}
}

/** AI 분석 무료 N회, 이후 유료. remaining·decrement·addQuota·canRequest 반환 */
export function useAnalysisQuota() {
  const [remaining, setRemaining] = useState(DEFAULT_FREE_COUNT);

  useEffect(() => {
    setRemaining(getQuotaFromStorage());
  }, []);

  const decrement = useCallback(() => {
    setRemaining((prev) => {
      const next = Math.max(0, prev - 1);
      setQuotaToStorage(next);
      return next;
    });
  }, []);

  /** 결제 완료 시 1회 분량 추가 */
  const addQuota = useCallback(() => {
    setRemaining((prev) => {
      const next = prev + 1;
      setQuotaToStorage(next);
      return next;
    });
  }, []);

  /** 테스트용: 무료 횟수를 기본값(100회)으로 초기화 */
  const resetQuota = useCallback(() => {
    setQuotaToStorage(DEFAULT_FREE_COUNT);
    setRemaining(DEFAULT_FREE_COUNT);
  }, []);

  return {
    remaining,
    decrement,
    addQuota,
    resetQuota,
    canRequest: remaining > 0,
    defaultFreeCount: DEFAULT_FREE_COUNT,
  };
}

/** 분석용 일봉 기간: 약 120일 전 ~ 오늘 (YYYYMMDD) */
function getAnalysisChartDates() {
  const end = new Date();
  const start = new Date(end);
  start.setDate(start.getDate() - 120);
  const fmt = (d: Date) => {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    return `${y}${m}${day}`;
  };
  return { startDate: fmt(start), endDate: fmt(end) };
}

const OVERSEAS_EXCHANGE: Record<string, string> = { US: "NAS" };

/** 해외 현재가를 백엔드 분석 API용 국내 형식(stck_prpr, stck_oprc 등)으로 정규화 */
function normalizeOverseasPriceForAnalysis(raw: Record<string, unknown>): Record<string, string> {
  const num = (v: unknown) => {
    if (v == null) return 0;
    const n = Number(String(v).replace(/,/g, ""));
    return Number.isNaN(n) ? 0 : n;
  };
  const str = (v: unknown) => (v != null ? String(v).replace(/,/g, "") : "");
  const cur = raw.last ?? raw.LAST ?? raw.last_price ?? raw.stck_prpr ?? raw.clos_price ?? raw.CLOS ?? 0;
  const open = raw.open ?? raw.OPEN ?? raw.stck_oprc ?? raw.oprc ?? raw.open_prc;
  const high = raw.high ?? raw.HIGH ?? raw.stck_hgpr ?? raw.hgpr ?? raw.high_prc;
  const low = raw.low ?? raw.LOW ?? raw.stck_lwpr ?? raw.lwpr ?? raw.low_prc;
  const change = raw.diff ?? raw.DIFF ?? raw.prdy_vrss ?? 0;
  const changeRate = raw.rate ?? raw.RATE ?? raw.prdy_ctrt ?? 0;
  const out: Record<string, string> = {
    stck_prpr: str(cur) || String(num(cur)),
    prdy_vrss: str(change) || String(num(change)),
    prdy_ctrt: str(changeRate) || String(num(changeRate)),
    // 통화 정보: 백엔드가 USD/KRW 구분할 수 있도록
    currency: "USD",
  };
  if (open != null && String(open).trim() !== "" && num(open) > 0) out.stck_oprc = str(open) || String(num(open));
  if (high != null && String(high).trim() !== "" && num(high) > 0) out.stck_hgpr = str(high) || String(num(high));
  if (low != null && String(low).trim() !== "" && num(low) > 0) out.stck_lwpr = str(low) || String(num(low));
  // 추가 해외 필드 (가능한 경우)
  const h52p = raw.h52p ?? raw.H52P;
  const l52p = raw.l52p ?? raw.L52P;
  const perx = raw.perx ?? raw.per;
  const epsx = raw.epsx ?? raw.eps;
  if (h52p != null && num(h52p) > 0) out.stck_52w_hgpr = String(num(h52p));
  if (l52p != null && num(l52p) > 0) out.stck_52w_lwpr = String(num(l52p));
  if (perx != null && num(perx) > 0) out.per = String(num(perx));
  if (epsx != null && num(epsx) > 0) out.eps = String(num(epsx));
  return out;
}

// ─── useKisAnalysis ─────────────────────────────────────────────────────

export function useKisAnalysis() {
  return useMutation<
    KisAnalysisResponse,
    Error,
    { stockCode: string; market?: "KR" | "US" }
  >({
    mutationFn: async ({ stockCode, market }) => {
      const code = stockCode?.trim();
      if (!code) {
        throw new Error("종목 코드를 입력해 주세요.");
      }

      const body: Record<string, unknown> = { stockCode: code, market: market ?? "KR" };
      const exchange = market === "US" ? OVERSEAS_EXCHANGE.US : null;
      if (exchange) body.exchange = exchange;

      // KIS API 호출을 순차적으로 실행 (rate limit 방지, 500ms 간격)
      if (market === "US" && exchange) {
        try {
          const { endDate } = getAnalysisChartDates();
          // 1) 현재가 먼저
          const priceRes = await fetch(
            `/api/kis/overseas-price?stockCode=${encodeURIComponent(code)}&exchange=${encodeURIComponent(exchange)}`,
            { credentials: "include" },
          );
          if (priceRes.ok) {
            const price = await priceRes.json();
            body.price = normalizeOverseasPriceForAnalysis(price as Record<string, unknown>);
          }
          // 2) 차트는 현재가 후 (서버 스로틀이 간격 보장)
          const chartRes = await fetch("/api/kis/overseas-chart", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ stockCode: code, exchange, bymd: endDate, gubn: "0" }),
            credentials: "include",
          });
          if (chartRes.ok) {
            const chartRaw = await chartRes.json();
            // 새 형식: { chartData: [...], ... } 또는 이전 형식: [...]
            const arr = Array.isArray(chartRaw) ? chartRaw : (Array.isArray(chartRaw?.chartData) ? chartRaw.chartData : []);
            body.dailyChart = arr
              .map((p: { time?: string; value?: number }) =>
                p?.time != null && p?.value != null
                  ? { stck_bsop_date: p.time.replace(/-/g, ""), stck_clpr: String(p.value) }
                  : null,
              )
              .filter(Boolean);
          }
        } catch {
          // 해외 시세/차트 실패 시 exchange만 전송
        }
      }

      if (/^\d{6}$/.test(code)) {
        try {
          // 1) 현재가 먼저
          const priceRes = await fetch("/api/kis/price", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ stockCode: code }),
            credentials: "include",
          });
          // 2) 차트는 현재가 후 (서버 스로틀이 간격 보장)
          const { startDate, endDate } = getAnalysisChartDates();
          const chartRes = await fetch("/api/kis/chart", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              stockCode: code,
              startDate,
              endDate,
              periodDivCode: "D",
            }),
            credentials: "include",
          });
          if (priceRes.ok && chartRes.ok) {
            const [price, dailyChart] = await Promise.all([
              priceRes.json(),
              chartRes.json(),
            ]);
            body.price = price;
            body.dailyChart = Array.isArray(dailyChart) ? dailyChart : [];
          }
        } catch {
          // 시세/차트 조회 실패 시 stockCode만 전송 (백엔드가 KIS 미설정 등 안내)
        }
      }

      const res = await fetch("/api/kis/analysis", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
        credentials: "include",
      });

      const data = await res.json().catch(() => ({}));

      if (!res.ok) {
        throw new Error(data.error || data.message || "분석 요청에 실패했습니다.");
      }

      if (
        typeof data.summary !== "string" ||
        typeof data.fullAnalysis !== "string"
      ) {
        throw new Error("잘못된 분석 응답 형식입니다.");
      }
      if (data.conclusion != null && typeof data.conclusion !== "string") {
        throw new Error("잘못된 분석 응답 형식입니다.");
      }

      return {
        summary: data.summary,
        fullAnalysis: data.fullAnalysis,
        conclusion: data.conclusion ?? undefined,
      };
    },
  });
}
