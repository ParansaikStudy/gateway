"use client";

import { useRef, useEffect, useState } from "react";
import { createChart, type IChartApi, type ISeriesApi, LineSeries } from "lightweight-charts";
import { CHART_PERIODS, type ChartDataPoint, type ChartPeriod } from "@/lib/kis";
import { cn } from "@/lib/utils";
import { Skeleton } from "@/components/ui/skeleton";

// ─── Moving Average Config ──────────────────────────────────────────
const MA_CONFIGS = [
  { period: 5, label: "5일", color: "#FF6B6B", darkColor: "#FF6B6B" },
  { period: 20, label: "20일", color: "#FFCE56", darkColor: "#FFCE56" },
  { period: 60, label: "60일", color: "#4BC0C0", darkColor: "#4BC0C0" },
  { period: 120, label: "120일", color: "#9966FF", darkColor: "#9966FF" },
] as const;

function calculateMA(data: ChartDataPoint[], period: number): ChartDataPoint[] {
  if (data.length < period) return [];
  const result: ChartDataPoint[] = [];
  for (let i = period - 1; i < data.length; i++) {
    let sum = 0;
    for (let j = 0; j < period; j++) {
      sum += data[i - j].value;
    }
    result.push({ time: data[i].time, value: sum / period });
  }
  return result;
}

// ─── Props ──────────────────────────────────────────────────────────
interface StockChartProps {
  data: ChartDataPoint[] | undefined;
  isLoading?: boolean;
  selectedPeriod: ChartPeriod;
  onPeriodChange: (period: ChartPeriod) => void;
  currency?: "KRW" | "USD";
}

export default function StockChart({
  data,
  isLoading,
  selectedPeriod,
  onPeriodChange,
  currency = "KRW",
}: StockChartProps) {
  const containerRef = useRef<HTMLDivElement>(null);
  const chartRef = useRef<IChartApi | null>(null);
  const seriesRef = useRef<ISeriesApi<"Line"> | null>(null);
  const maSeriesRefs = useRef<ISeriesApi<"Line">[]>([]);
  const [isDark, setIsDark] = useState(false);
  const [visibleMAs, setVisibleMAs] = useState<Record<number, boolean>>(() =>
    Object.fromEntries(MA_CONFIGS.map((c) => [c.period, true])),
  );

  const isUsd = currency === "USD";
  const hasData = !!data && data.length > 0;

  // Detect dark mode
  useEffect(() => {
    const root = document.documentElement;
    const check = () => setIsDark(root.classList.contains("dark"));
    check();
    const observer = new MutationObserver(check);
    observer.observe(root, { attributes: true, attributeFilter: ["class"] });
    return () => observer.disconnect();
  }, []);

  // Create / recreate chart
  useEffect(() => {
    if (!containerRef.current) return;

    const container = containerRef.current;

    const chart = createChart(container, {
      width: container.clientWidth,
      height: 360,
      layout: {
        background: { color: isDark ? "#1a1f3a" : "#ffffff" },
        textColor: isDark ? "#a0a8c8" : "#71717a",
        fontFamily: "inherit",
      },
      grid: {
        vertLines: { color: isDark ? "#252b4a" : "#f4f4f5" },
        horzLines: { color: isDark ? "#252b4a" : "#f4f4f5" },
      },
      crosshair: {
        vertLine: { color: isDark ? "#3ec6c0" : "#a1a1aa", width: 1, style: 2 },
        horzLine: { color: isDark ? "#3ec6c0" : "#a1a1aa", width: 1, style: 2 },
      },
      rightPriceScale: {
        borderColor: isDark ? "#2d345a" : "#e4e4e7",
      },
      timeScale: {
        borderColor: isDark ? "#2d345a" : "#e4e4e7",
        timeVisible: false,
      },
      localization: {
        priceFormatter: (price: number) =>
          isUsd
            ? `$${price.toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
            : price.toLocaleString("ko-KR") + "원",
      },
    });

    // Main price line
    const series = chart.addSeries(LineSeries, {
      color: isDark ? "#3ec6c0" : "#18181b",
      lineWidth: 2,
      crosshairMarkerBackgroundColor: isDark ? "#3ec6c0" : "#18181b",
      crosshairMarkerRadius: 4,
      priceLineVisible: false,
      lastValueVisible: true,
    });

    // Moving average lines
    const maSeries: ISeriesApi<"Line">[] = [];
    for (const ma of MA_CONFIGS) {
      const mLine = chart.addSeries(LineSeries, {
        color: isDark ? ma.darkColor : ma.color,
        lineWidth: 1,
        crosshairMarkerRadius: 0,
        priceLineVisible: false,
        lastValueVisible: false,
        visible: visibleMAs[ma.period] !== false,
      });
      maSeries.push(mLine);
    }

    chartRef.current = chart;
    seriesRef.current = series;
    maSeriesRefs.current = maSeries;

    // Set data if available
    if (data && data.length > 0) {
      series.setData(data);
      MA_CONFIGS.forEach((ma, idx) => {
        const maData = calculateMA(data, ma.period);
        if (maData.length > 0) {
          maSeries[idx].setData(maData);
        }
      });
      chart.timeScale().fitContent();
    }

    // Responsive resize
    const resizeObserver = new ResizeObserver((entries) => {
      for (const entry of entries) {
        const { width } = entry.contentRect;
        chart.applyOptions({ width });
      }
    });
    resizeObserver.observe(container);

    return () => {
      resizeObserver.disconnect();
      chart.remove();
      chartRef.current = null;
      seriesRef.current = null;
      maSeriesRefs.current = [];
    };
  }, [isDark, isUsd]); // eslint-disable-line react-hooks/exhaustive-deps

  // Update data when it changes (without recreating chart)
  useEffect(() => {
    if (!seriesRef.current || !chartRef.current) return;
    if (data && data.length > 0) {
      seriesRef.current.setData(data);
      MA_CONFIGS.forEach((ma, idx) => {
        if (maSeriesRefs.current[idx]) {
          const maData = calculateMA(data, ma.period);
          maSeriesRefs.current[idx].setData(maData);
        }
      });
      chartRef.current.timeScale().fitContent();
    }
  }, [data]);

  // Toggle MA visibility
  const toggleMA = (period: number, idx: number) => {
    setVisibleMAs((prev) => {
      const next = { ...prev, [period]: !prev[period] };
      if (maSeriesRefs.current[idx]) {
        maSeriesRefs.current[idx].applyOptions({ visible: next[period] });
      }
      return next;
    });
  };

  return (
    <div className="rounded-xl border border-border bg-card p-4">
      {/* Period selector + MA legend */}
      <div className="mb-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <h3 className="text-sm font-semibold text-foreground">주가 차트</h3>
        <div className="flex flex-wrap items-center gap-2">
          {/* MA legend */}
          <div className="flex flex-wrap items-center gap-1.5 mr-2">
            <span
              className="flex items-center gap-1 rounded px-1.5 py-0.5 text-[11px] font-medium text-muted-foreground border border-border/50"
              style={{ borderColor: isDark ? "#3ec6c0" : "#18181b" }}
            >
              <span
                className="inline-block h-[2px] w-3 rounded-full"
                style={{ backgroundColor: isDark ? "#3ec6c0" : "#18181b" }}
              />
              종가
            </span>
            {MA_CONFIGS.map((ma, idx) => {
              const active = visibleMAs[ma.period] !== false;
              const maHasData = data && data.length >= ma.period;
              return (
                <button
                  key={ma.period}
                  onClick={() => toggleMA(ma.period, idx)}
                  disabled={!maHasData}
                  className={cn(
                    "flex items-center gap-1 rounded px-1.5 py-0.5 text-[11px] font-medium transition-all",
                    "border",
                    active && maHasData
                      ? "border-border/50 text-foreground"
                      : "border-transparent text-muted-foreground/40",
                    maHasData && "cursor-pointer hover:bg-accent/50",
                    !maHasData && "cursor-default opacity-30",
                  )}
                >
                  <span
                    className="inline-block h-[2px] w-3 rounded-full transition-opacity"
                    style={{
                      backgroundColor: isDark ? ma.darkColor : ma.color,
                      opacity: active && maHasData ? 1 : 0.3,
                    }}
                  />
                  MA{ma.period}
                </button>
              );
            })}
          </div>
          {/* Period buttons */}
          <div className="flex gap-1">
            {CHART_PERIODS.map(({ key, label }) => (
              <button
                key={key}
                onClick={() => onPeriodChange(key)}
                className={cn(
                  "rounded-md px-3 py-1.5 text-xs font-medium transition-colors",
                  selectedPeriod === key
                    ? "bg-primary text-primary-foreground"
                    : "text-muted-foreground hover:bg-accent hover:text-foreground",
                )}
              >
                {label}
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Chart area — container always rendered so chart can be created */}
      <div className="relative h-[360px] w-full">
        <div
          ref={containerRef}
          className={cn(
            "absolute inset-0 [&_a[href*='tradingview']]:!hidden",
            !hasData && "invisible",
          )}
        />
        {isLoading && (
          <Skeleton className="absolute inset-0 rounded-lg" />
        )}
        {!isLoading && !hasData && (
          <div className="absolute inset-0 flex items-center justify-center text-sm text-muted-foreground">
            차트 데이터가 없습니다
          </div>
        )}
      </div>
    </div>
  );
}
