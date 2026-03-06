"use client";

import { useState, useRef, useEffect } from "react";
import { useSearchParams } from "next/navigation";
import { useQueryClient } from "@tanstack/react-query";
import { BarChart3, Loader2, Sparkles } from "lucide-react";
import StockSearchBar from "@/components/analysis/stock-search-bar";
import StockPriceInfo from "@/components/analysis/stock-price-info";
import KisAnalysisResultPanel from "@/components/analysis/kis-analysis-result-panel";
import StockChart from "@/components/chart/stock-chart";
import ExchangeRateBar from "@/components/analysis/exchange-rate-bar";
import PaymentModal from "@/components/analysis/payment-modal";
import { Button } from "@/components/ui/button";
import { useKisCredentials, useKisToken, useKisChart, useKisOverseasChart, useKisAnalysis, useKisRealtimePrice, useKisPrice, useKisOverseasPrice, useAnalysisQuota } from "@/hooks/use-kis";
import { usePayment } from "@/hooks/use-payment";
import { kisCurrentPriceToDisplay, overseasPriceToDisplay, type ChartPeriod, type KisStockDetailDisplay } from "@/lib/kis";

export default function AnalysisPage() {
  const [searched, setSearched] = useState(false);
  const [stockCode, setStockCode] = useState<string>("");
  const [stockName, setStockName] = useState<string>("");
  const [stockMarket, setStockMarket] = useState<"KR" | "US" | undefined>(undefined);
  const [chartPeriod, setChartPeriod] = useState<ChartPeriod>("1D");
  const queryClient = useQueryClient();
  const prevStockCodeRef = useRef<string>("");

  const { isConnected } = useKisCredentials();
  const { data: tokenData } = useKisToken(isConnected);
  const tokenValid = !!tokenData?.valid;
  const isKrStock = stockMarket === "KR";
  const searchParams = useSearchParams();
  const { remaining: analysisRemaining, decrement: decrementQuota, addQuota, resetQuota, canRequest: canRequestAnalysis, defaultFreeCount } = useAnalysisQuota();
  const { confirmPayment } = usePayment();
  const [paymentModalOpen, setPaymentModalOpen] = useState(false);
  const {
    data: analysisResult,
    isPending: analysisLoading,
    isError: analysisError,
    error: analysisErrorMessage,
    mutate: requestAnalysis,
    reset: resetAnalysis,
  } = useKisAnalysis();

  const { data: kisPriceData } = useKisPrice(
    isKrStock && /^\d{6}$/.test(stockCode || "") ? stockCode || undefined : undefined,
    isConnected,
    tokenValid,
  );
  const { data: kisOverseasPriceData } = useKisOverseasPrice(
    !isKrStock && stockCode ? stockCode : undefined,
    isConnected,
    tokenValid,
  );

  const { data: kisChartData } = useKisChart(
    isKrStock ? stockCode || undefined : undefined,
    chartPeriod,
    isConnected,
    tokenValid,
  );
  const { data: kisOverseasChartData } = useKisOverseasChart(
    !isKrStock ? stockCode || undefined : undefined,
    chartPeriod,
    isConnected,
    tokenValid,
  );

  const {
    realtimePrice,
    realtimeChange,
    realtimeChangePercent,
    realtimeSign,
    realtimeMarket,
    isStreaming,
  } = useKisRealtimePrice(
    stockCode || undefined,
    isConnected && tokenValid && !!stockCode,
    stockMarket ?? "KR",
  );

  const paymentProcessedRef = useRef(false);
  // 결제 성공 리다이렉트 처리 (paymentKey, orderId, amount 쿼리로 복귀)
  useEffect(() => {
    const payment = searchParams.get("payment");
    const paymentKey = searchParams.get("paymentKey");
    const orderId = searchParams.get("orderId");
    const amountStr = searchParams.get("amount");
    if (payment !== "success" || !paymentKey || !orderId || amountStr == null || paymentProcessedRef.current) return;
    const amount = Number(amountStr);
    if (Number.isNaN(amount) || amount <= 0) return;
    paymentProcessedRef.current = true;
    (async () => {
      const ok = await confirmPayment(paymentKey, orderId, amount);
      if (ok) addQuota();
      window.history.replaceState({}, "", "/analysis");
    })();
  }, [searchParams, confirmPayment, addQuota]);

  // 종목 변경 시 이전 종목의 price 캐시 정리
  useEffect(() => {
    const prev = prevStockCodeRef.current;
    if (prev && prev !== stockCode) {
      queryClient.removeQueries({ queryKey: ["kis-price", prev] });
    }
    prevStockCodeRef.current = stockCode;
  }, [stockCode, queryClient]);

  // KIS 실데이터만 사용. 국내=일봉차트, 해외=해외 일/주/월봉
  const domesticChart = isKrStock && kisChartData && kisChartData.length > 0 ? kisChartData : [];
  const overseasChartPoints = !isKrStock && kisOverseasChartData?.chartData && kisOverseasChartData.chartData.length > 0 ? kisOverseasChartData.chartData : [];
  const chartData = isKrStock ? domesticChart : overseasChartPoints;
  const chartLoading =
    isConnected &&
    tokenValid &&
    !!stockCode &&
    (isKrStock ? !kisChartData : !kisOverseasChartData);

  // 국내: KIS 현재가 → 표시. 해외: KIS 해외 현재가 → 표시. 실시간 시세 있으면 가격/등락만 덮어씀
  let baseDetailFromKis: KisStockDetailDisplay | null =
    isKrStock && kisPriceData && stockCode
      ? kisCurrentPriceToDisplay(kisPriceData as Parameters<typeof kisCurrentPriceToDisplay>[0], stockCode)
      : !isKrStock && kisOverseasPriceData && stockCode
        ? overseasPriceToDisplay(
            kisOverseasPriceData as Record<string, unknown>,
            stockCode,
            stockName || stockCode,
          )
        : null;

  // 해외 모의투자: 현재가 API에서 미제공되는 필드를 차트 데이터에서 보완
  if (!isKrStock && baseDetailFromKis && kisOverseasChartData) {
    const { latestOHLC, periodHigh, periodLow } = kisOverseasChartData;
    baseDetailFromKis = { ...baseDetailFromKis };
    if (baseDetailFromKis.open == null && latestOHLC?.open != null) baseDetailFromKis.open = latestOHLC.open;
    if (baseDetailFromKis.high == null && latestOHLC?.high != null) baseDetailFromKis.high = latestOHLC.high;
    if (baseDetailFromKis.low == null && latestOHLC?.low != null) baseDetailFromKis.low = latestOHLC.low;
    if (baseDetailFromKis.week52High == null && periodHigh != null) baseDetailFromKis.week52High = periodHigh;
    if (baseDetailFromKis.week52Low == null && periodLow != null) baseDetailFromKis.week52Low = periodLow;
  }
  const displayName = baseDetailFromKis?.name ?? (stockName || stockCode || "");

  const priceNum =
    realtimePrice != null
      ? Number(realtimePrice)
      : baseDetailFromKis?.price ?? 0;
  const changeNum =
    realtimeChange != null && realtimeChange !== ""
      ? Number(realtimeChange)
      : baseDetailFromKis?.change ?? 0;
  const changePercentNum =
    realtimeChangePercent != null && realtimeChangePercent !== ""
      ? Number(realtimeChangePercent)
      : baseDetailFromKis?.changePercent ?? 0;
  const isDown = realtimeSign ? ["-", "5", "4"].includes(realtimeSign) : (baseDetailFromKis?.change ?? 0) < 0;

  const stockDetail: KisStockDetailDisplay = baseDetailFromKis
    ? {
        ...baseDetailFromKis,
        name: displayName,
        price: priceNum,
        change: changeNum,
        changePercent: changePercentNum,
      }
    : {
        name: displayName,
        code: stockCode || "",
        price: priceNum,
        change: changeNum,
        changePercent: changePercentNum,
      };
  const currency = (realtimeMarket ?? stockMarket) === "US" ? "USD" : "KRW";

  const handleSearch = (result: { code: string; market: "KR" | "US"; name: string }) => {
    setSearched(true);
    setStockCode(result.code);
    setStockName(result.name);
    setStockMarket(result.market);
    resetAnalysis();
  };

  const handleRequestAnalysis = () => {
    if (!stockCode || !canRequestAnalysis) return;
    requestAnalysis(
      { stockCode, market: stockMarket },
      {
        onSuccess: () => {
          decrementQuota();
        },
      },
    );
  };

  return (
    <div className="mx-auto max-w-6xl space-y-6">
      {/* Search */}
      <StockSearchBar onSearch={handleSearch} />

      {searched ? (
        <>
          {/* 환율 (검색한 종목 기준 한국투자증권 실시간) */}
          <ExchangeRateBar
            stockCode={stockCode || undefined}
            market={stockMarket ?? "KR"}
            isConnected={isConnected}
            tokenValid={tokenValid}
          />
          {/* Chart - full width (key로 종목/기간 변경 시 차트 재생성해 모든 종목에서 표시) */}
          <StockChart
            key={`chart-${stockCode || "default"}-${chartPeriod}`}
            data={chartData}
            isLoading={chartLoading}
            selectedPeriod={chartPeriod}
            onPeriodChange={setChartPeriod}
            currency={currency}
          />

          <div className="grid grid-cols-1 gap-6 lg:grid-cols-5">
            {/* Left: Price Info */}
            <div className="lg:col-span-2">
              <StockPriceInfo
                stock={stockDetail}
                isRealtime={isStreaming && realtimePrice != null}
                currency={currency}
                isPositive={!isDown}
              />
            </div>

            {/* Right: Analysis */}
            <div className="lg:col-span-3">
              {analysisLoading ? (
                <div className="flex h-full min-h-[280px] flex-col items-center justify-center rounded-xl border border-border/50 bg-card/50 p-8 text-center backdrop-blur-sm">
                  <Loader2 className="mb-4 h-10 w-10 animate-spin text-primary" />
                  <p className="text-sm text-muted-foreground">
                    분석 중입니다…
                  </p>
                </div>
              ) : analysisError ? (
                <div className="flex h-full flex-col items-center justify-center rounded-xl border border-border/50 bg-card/50 p-8 text-center backdrop-blur-sm">
                  <p className="mb-4 text-sm text-destructive">
                    {analysisErrorMessage?.message ?? "분석 요청에 실패했습니다."}
                  </p>
                  {canRequestAnalysis ? (
                    <button
                      onClick={handleRequestAnalysis}
                      className="inline-flex items-center gap-2 rounded-lg bg-primary px-6 py-3 text-sm font-semibold text-primary-foreground transition-colors hover:bg-primary/90"
                    >
                      <Sparkles className="h-4 w-4" />
                      다시 요청하기
                    </button>
                  ) : (
                    <>
                      <p className="mb-4 text-sm text-muted-foreground">
                        무료 {defaultFreeCount}회를 모두 사용했습니다. 유료 결제 후 이용해 주세요.
                      </p>
                      <div className="flex flex-wrap items-center justify-center gap-2">
                        <Button
                          onClick={() => setPaymentModalOpen(true)}
                          className="inline-flex items-center gap-2"
                        >
                          <Sparkles className="h-4 w-4" />
                          5,000원 결제하고 분석하기
                        </Button>
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={resetQuota}
                          className="text-muted-foreground"
                        >
                          무료 횟수 초기화 (테스트용)
                        </Button>
                      </div>
                    </>
                  )}
                </div>
              ) : analysisResult ? (
                <KisAnalysisResultPanel
                  result={analysisResult}
                  stockDisplayName={
                    displayName && stockCode
                      ? `${displayName}(${stockCode})`
                      : undefined
                  }
                />
              ) : (
                <div className="flex h-full flex-col items-center justify-center rounded-xl border border-border/50 bg-card/50 p-8 text-center backdrop-blur-sm">
                  <div className="mb-4 flex h-14 w-14 items-center justify-center rounded-2xl bg-primary/10">
                    <BarChart3 className="h-7 w-7 text-primary" />
                  </div>
                  <h3 className="mb-2 text-lg font-semibold text-foreground">
                    스마트 시장 분석
                  </h3>
                  <p className="mb-2 max-w-xs text-sm text-muted-foreground">
                    기술적 지표, 시장 데이터, 리스크 요인을 종합하여
                    참고 정보를 제공합니다.
                  </p>
                  <p className="mb-4 text-xs text-muted-foreground">
                    정보 분석은 한국투자증권 API 설정을 연결한 후 이용할 수 있습니다.
                  </p>
                  {canRequestAnalysis ? (
                    <>
                      <p className="mb-4 text-xs text-muted-foreground">
                        무료 {defaultFreeCount}회 중 <strong className="text-foreground">{analysisRemaining}</strong>회 남음
                      </p>
                      <button
                        onClick={handleRequestAnalysis}
                        disabled={!stockCode}
                        className="inline-flex items-center gap-2 rounded-lg bg-primary px-6 py-3 text-sm font-semibold text-primary-foreground transition-colors hover:bg-primary/90 disabled:opacity-50 disabled:pointer-events-none"
                      >
                        <Sparkles className="h-4 w-4" />
                        분석 요청하기
                      </button>
                    </>
                  ) : (
                    <>
                      <p className="mb-4 text-sm text-muted-foreground">
                        무료 {defaultFreeCount}회를 모두 사용했습니다.
                      </p>
                      <div className="flex flex-col items-center gap-2 sm:flex-row">
                        <button
                          type="button"
                          onClick={() => setPaymentModalOpen(true)}
                          className="inline-flex items-center gap-2 rounded-lg bg-primary px-6 py-3 text-sm font-semibold text-primary-foreground transition-colors hover:bg-primary/90"
                        >
                          <Sparkles className="h-4 w-4" />
                          5,000원 결제하고 분석하기
                        </button>
                        <button
                          type="button"
                          onClick={resetQuota}
                          className="text-sm text-muted-foreground underline hover:text-foreground"
                        >
                          무료 횟수 초기화 (테스트용)
                        </button>
                      </div>
                    </>
                  )}
                </div>
              )}
            </div>
          </div>
        </>
      ) : (
        <div className="flex flex-col items-center justify-center py-24 text-center">
          <div className="mb-4 flex h-16 w-16 items-center justify-center rounded-2xl bg-primary/10">
            <svg
              className="h-8 w-8 text-primary"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              strokeWidth={1.5}
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"
              />
            </svg>
          </div>
          <h3 className="mb-2 text-lg font-semibold text-foreground">
            종목을 검색해보세요
          </h3>
          <p className="max-w-sm text-sm text-muted-foreground">
            종목명 또는 종목코드를 입력하면 시장 데이터를 분석하여
            참고자료를 제공합니다.
          </p>
        </div>
      )}

      <PaymentModal open={paymentModalOpen} onOpenChange={setPaymentModalOpen} />
    </div>
  );
}
