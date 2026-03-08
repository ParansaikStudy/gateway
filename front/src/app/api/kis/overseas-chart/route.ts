import { NextRequest, NextResponse } from "next/server";
import { KIS_BASE_URL, getTodayDate } from "@/lib/kis";
import { kisThrottle } from "@/lib/kis-throttle";
import {
  getCredentialsFromRequest,
  getTokenFromRequest,
} from "@/lib/credentials";

/** 해외 일봉 TR ID (공식 샘플 기준) */
const KIS_TR_ID_OVERSEAS_DAILYPRICE = "HHDFS76240000";

/** 해외 일봉 항목에서 날짜·종가 추출 (KIS: xymd, clos 등) */
function toChartPoint(item: Record<string, unknown>): { time: string; value: number } | null {
  // 날짜 필드: KIS 해외 일봉은 xymd(YYYYMMDD) 사용
  const dateRaw =
    item.xymd ??
    item.stck_bsop_date ??
    item.kymd ??
    item.date ??
    item.ord_ymd ??
    item.trd_dd ??
    item.XYMD ??
    item.bas_dt;
  // 종가 필드: KIS 해외 일봉은 clos 사용
  const closeRaw =
    item.clos ??
    item.CLOS ??
    item.stck_clpr ??
    item.close ??
    item.last ??
    item.clos_price ??
    item.ovrs_nmix_prpr;
  if (dateRaw == null || closeRaw == null) return null;
  const dateStr = String(dateRaw).trim();
  if (!dateStr) return null;
  let y: string, m: string, d: string;
  if (dateStr.length >= 8 && /^\d{8}$/.test(dateStr.slice(0, 8))) {
    y = dateStr.slice(0, 4);
    m = dateStr.slice(4, 6);
    d = dateStr.slice(6, 8);
  } else if (dateStr.length >= 10 && /^\d{4}-\d{2}-\d{2}/.test(dateStr)) {
    y = dateStr.slice(0, 4);
    m = dateStr.slice(5, 7);
    d = dateStr.slice(8, 10);
  } else {
    return null;
  }
  if (!y || !m || !d) return null;
  const time = `${y}-${m}-${d}`;
  const value = Number(String(closeRaw).replace(/,/g, ""));
  if (Number.isNaN(value) || value <= 0) return null;
  return { time, value };
}

/** 응답에서 봉 배열 추출 (output2 우선, 객체/배열/키배열 모두 처리) */
function extractBars(data: { output1?: unknown; output2?: unknown }): Record<string, unknown>[] {
  const asList = (v: unknown): Record<string, unknown>[] => {
    if (Array.isArray(v)) return v as Record<string, unknown>[];
    if (v != null && typeof v === "object" && !Array.isArray(v)) {
      const obj = v as Record<string, unknown>;
      if (Object.keys(obj).every((k) => /^\d+$/.test(k)))
        return Object.keys(obj)
          .sort((a, b) => Number(a) - Number(b))
          .map((k) => obj[k] as Record<string, unknown>)
          .filter(Boolean);
      return [obj];
    }
    return [];
  };
  const list2 = asList(data.output2);
  if (list2.length > 0) return list2;
  return asList(data.output1);
}

/**
 * 해외주식 기간별 시세 (일봉/주봉/월봉) → 차트용.
 * POST body: { stockCode, exchange?, bymd?, gubn? }
 * - gubn: 0=일, 1=주, 2=월
 */
export async function POST(req: NextRequest) {
  try {
    const creds = getCredentialsFromRequest(req);
    const token = getTokenFromRequest(req);

    if (!creds || !token) {
      return NextResponse.json(
        { error: "Not authenticated" },
        { status: 401 },
      );
    }

    let body: unknown;
    try {
      body = await req.json();
    } catch {
      return NextResponse.json(
        { error: "Invalid JSON body" },
        { status: 400 },
      );
    }
    const { stockCode, exchange = "NAS", bymd, gubn = "0" } = body as Record<string, string>;
    const symbol = String(stockCode || "").trim();
    if (!symbol) {
      return NextResponse.json(
        { error: "stockCode is required" },
        { status: 400 },
      );
    }

    const baseUrl = KIS_BASE_URL[creds.environment];
    const endDate = (bymd || "").trim() || getTodayDate();
    const url =
      `${baseUrl}/uapi/overseas-price/v1/quotations/dailyprice?` +
      new URLSearchParams({
        AUTH: "",
        EXCD: exchange || "NAS",
        SYMB: symbol,
        GUBN: gubn || "0",
        BYMD: endDate,
        MODP: "1",
      });

    await kisThrottle();
    const res = await fetch(url, {
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        authorization: `Bearer ${token}`,
        appkey: creds.appkey,
        appsecret: creds.appsecret,
        tr_id: KIS_TR_ID_OVERSEAS_DAILYPRICE,
        custtype: "P",
      },
    });

    const text = await res.text();
    let data: { rt_cd?: string; msg1?: string; output1?: unknown; output2?: unknown };
    try {
      data = text ? JSON.parse(text) : {};
    } catch {
      console.warn("[kis/overseas-chart] KIS non-JSON response:", res.status);
      return NextResponse.json([]);
    }

    if (!res.ok) {
      console.warn("[kis/overseas-chart] KIS not ok:", res.status, data.msg1);
      return NextResponse.json([]);
    }
    if (data.rt_cd !== "0") {
      console.warn("[kis/overseas-chart] KIS rt_cd:", data.rt_cd, data.msg1);
      return NextResponse.json([]);
    }

    const all = extractBars(data);
    const points = all
      .map((item) => toChartPoint(item))
      .filter((p): p is { time: string; value: number } => p != null);
    // 중복 날짜 제거 (마지막 값 유지)
    const seen = new Map<string, { time: string; value: number }>();
    for (const p of points) {
      seen.set(p.time, p);
    }
    const sorted = Array.from(seen.values()).sort((a, b) => a.time.localeCompare(b.time));

    // 차트 bar에서 추출 가능한 보조 데이터 (모의투자에서 현재가 API가 미제공하는 필드 보완)
    const toNum = (v: unknown) => { const n = Number(String(v ?? "").replace(/,/g, "")); return Number.isNaN(n) || n <= 0 ? null : n; };
    const latestBar = all.length > 0 ? all[all.length - 1] : null;
    let allHigh = -Infinity, allLow = Infinity;
    for (const bar of all) {
      const h = toNum(bar.high ?? bar.HIGH); if (h != null && h > allHigh) allHigh = h;
      const l = toNum(bar.low ?? bar.LOW);   if (l != null && l < allLow) allLow = l;
    }

    return NextResponse.json({
      chartData: sorted,
      // 최신 bar OHLC (현재가 API에서 미제공 시 프론트에서 보완용)
      latestOHLC: latestBar ? {
        open: toNum(latestBar.open ?? latestBar.OPEN),
        high: toNum(latestBar.high ?? latestBar.HIGH),
        low: toNum(latestBar.low ?? latestBar.LOW),
      } : null,
      // 전체 기간 고/저 (52주 대용)
      periodHigh: allHigh > -Infinity ? allHigh : null,
      periodLow: allLow < Infinity ? allLow : null,
    });
  } catch (e) {
    const message = e instanceof Error ? e.message : "Internal server error";
    return NextResponse.json(
      { error: message },
      { status: 500 },
    );
  }
}
