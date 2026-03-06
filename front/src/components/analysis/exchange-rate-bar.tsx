"use client";

import { useKisExchangeRate } from "@/hooks/use-kis";
import { Skeleton } from "@/components/ui/skeleton";

interface ExchangeRateBarProps {
  stockCode: string | undefined;
  market: "KR" | "US";
  isConnected: boolean;
  tokenValid: boolean;
}

/**
 * 차트 위 환율/통화 정보.
 * 검색한 종목·시장 기준으로 한국투자증권 API에서 실시간 데이터를 가져와 표시합니다.
 */
export default function ExchangeRateBar({
  stockCode,
  market,
  isConnected,
  tokenValid,
}: ExchangeRateBarProps) {
  const {
    data,
    isLoading,
    isError,
    error,
  } = useKisExchangeRate(stockCode, market, isConnected, tokenValid);

  if (!stockCode) {
    return (
      <div className="flex flex-wrap items-center gap-x-4 gap-y-1 rounded-lg border border-border/60 bg-muted/40 px-4 py-3 text-sm text-muted-foreground">
        종목을 검색하면 해당 종목 기준 실시간 정보(환율 등)를 표시합니다. 한국투자증권 API 연결 후 이용 가능합니다.
      </div>
    );
  }

  if (!isConnected || !tokenValid) {
    return (
      <div className="flex flex-wrap items-center gap-x-4 gap-y-1 rounded-lg border border-border/60 bg-muted/40 px-4 py-3 text-sm text-muted-foreground">
        한국투자증권 API 설정을 연결하면 검색한 종목 기준 실시간 환율을 볼 수 있습니다.
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="rounded-lg border border-border/60 bg-muted/40 px-4 py-3">
        <Skeleton className="h-5 w-64" />
      </div>
    );
  }

  if (isError || !data) {
    return (
      <div className="flex flex-wrap items-center gap-x-4 gap-y-1 rounded-lg border border-border/60 bg-muted/40 px-4 py-3 text-sm text-muted-foreground">
        {error?.message ?? "환율 정보를 불러오지 못했습니다."} (검색 종목: {stockCode})
      </div>
    );
  }

  // 국내 종목: 원화 기준
  if (market === "KR") {
    return (
      <div className="flex flex-wrap items-center gap-x-4 gap-y-1 rounded-lg border border-border/60 bg-muted/40 px-4 py-3 text-sm">
        <span className="font-medium text-foreground">원화 기준</span>
        <span className="text-muted-foreground">{data.source} 실시간</span>
      </div>
    );
  }

  // 해외 종목인데 환율 미제공(API 구조 상이 등)
  if (data.rate == null) {
    return (
      <div className="flex flex-wrap items-center gap-x-4 gap-y-1 rounded-lg border border-border/60 bg-muted/40 px-4 py-3 text-sm text-muted-foreground">
        {data.message ?? "해당 종목 적용환율을 KIS에서 조회할 수 없습니다. API 문서를 확인해 주세요."} · {data.source}
      </div>
    );
  }

  const change = data.change ?? 0;
  const changePercent = data.changePercent ?? 0;
  const isDown = change < 0;

  return (
    <div className="flex flex-wrap items-center gap-x-4 gap-y-1 rounded-lg border border-border/60 bg-muted/40 px-4 py-3 text-sm">
      <span className="font-semibold text-foreground">
        환율 {data.rate.toLocaleString("ko-KR")}원
      </span>
      <span className="text-muted-foreground">
        어제보다{" "}
        <span className={isDown ? "text-red-600 dark:text-red-400" : "text-green-600 dark:text-green-400"}>
          {isDown ? "" : "+"}
          {change.toLocaleString("ko-KR")}원
        </span>
        {" "}({changePercent > 0 ? "+" : ""}{changePercent}%)
      </span>
      {data.realtime && (
        <span className="rounded bg-primary/10 px-2 py-0.5 text-xs font-medium text-primary">
          실시간
        </span>
      )}
      <span className="text-muted-foreground">{data.source}</span>
    </div>
  );
}
