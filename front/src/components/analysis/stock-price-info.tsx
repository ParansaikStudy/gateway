import { TrendingUp, TrendingDown } from "lucide-react";
import type { KisStockDetailDisplay } from "@/lib/kis";

interface StockPriceInfoProps {
  stock: KisStockDetailDisplay;
  /** WebSocket 실시간(푸시) 시세 수신 중일 때 true */
  isRealtime?: boolean;
  /** 통화: KRW(원), USD(달러) */
  currency?: "KRW" | "USD";
  /** 등락 방향 (실시간 시 sign 기준으로 넘기면 됨) */
  isPositive?: boolean;
}

export default function StockPriceInfo({
  stock,
  isRealtime,
  currency = "KRW",
  isPositive: isPositiveProp,
}: StockPriceInfoProps) {
  const isPositive = isPositiveProp ?? stock.change >= 0;
  const isUsd = currency === "USD";
  const priceStr = isUsd
    ? `$${Number(stock.price).toLocaleString("en-US", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`
    : `${stock.price.toLocaleString("ko-KR")}원`;
  const changeStr = isUsd
    ? `${isPositive ? "+" : ""}${Number(stock.change).toFixed(2)}`
    : `${isPositive ? "+" : ""}${stock.change.toLocaleString("ko-KR")}원`;

  return (
    <div className="rounded-xl border border-border/50 bg-card/50 p-6 backdrop-blur-sm">
      <div className="mb-4 flex items-baseline gap-3">
        <h2 className="text-xl font-bold text-card-foreground">
          {stock.name}
        </h2>
        <span className="text-sm text-muted-foreground">{stock.code}</span>
        {isRealtime && (
          <span className="rounded bg-emerald-500/20 px-2 py-0.5 text-xs font-medium text-emerald-500">
            실시간
          </span>
        )}
      </div>

      {/* Current Price */}
      <div className="mb-6">
        <div className="text-3xl font-bold text-card-foreground">
          {priceStr}
        </div>
        <div
          className={`mt-1 flex items-center gap-1.5 text-base font-medium ${
            isPositive ? "text-emerald-400" : "text-rose-400"
          }`}
        >
          {isPositive ? (
            <TrendingUp className="h-4 w-4" />
          ) : (
            <TrendingDown className="h-4 w-4" />
          )}
          <span>{changeStr}</span>
          <span>
            ({isPositive ? "+" : ""}
            {stock.changePercent.toFixed(2)}%)
          </span>
        </div>
      </div>

      {/* Detail Grid - KIS 실데이터 기준, 없으면 - 표시 */}
      <div className="grid grid-cols-2 gap-x-6 gap-y-3 text-sm">
        <DetailRow label="전일종가" value={stock.base != null ? (isUsd ? `$${stock.base.toLocaleString()}` : `${stock.base.toLocaleString()}원`) : "-"} />
        <DetailRow label="시가" value={stock.open != null ? (isUsd ? `$${stock.open.toLocaleString()}` : `${stock.open.toLocaleString()}원`) : "-"} />
        <DetailRow label="고가" value={stock.high != null ? (isUsd ? `$${stock.high.toLocaleString()}` : `${stock.high.toLocaleString()}원`) : "-"} positive />
        <DetailRow label="저가" value={stock.low != null ? (isUsd ? `$${stock.low.toLocaleString()}` : `${stock.low.toLocaleString()}원`) : "-"} negative />
        <DetailRow label="거래량" value={stock.volume != null ? `${stock.volume.toLocaleString()}주` : "-"} />
        {stock.prevVolume != null && <DetailRow label="전일거래량" value={`${stock.prevVolume.toLocaleString()}주`} />}
        <DetailRow label="시가총액" value={stock.marketCap ?? "-"} />
        <DetailRow label="PER" value={stock.per != null ? `${stock.per}배` : "-"} />
        <DetailRow label="PBR" value={stock.pbr != null ? `${stock.pbr}배` : "-"} />
        <DetailRow label="EPS" value={stock.eps != null ? (isUsd ? `$${stock.eps.toLocaleString()}` : `${stock.eps.toLocaleString()}원`) : "-"} />
        <DetailRow label="배당수익률" value={stock.dividendYield != null ? `${stock.dividendYield.toFixed(2)}%` : "-"} />
        <DetailRow label="52주 최고" value={stock.week52High != null ? (isUsd ? `$${stock.week52High.toLocaleString()}` : `${stock.week52High.toLocaleString()}원`) : "-"} />
        <DetailRow label="52주 최저" value={stock.week52Low != null ? (isUsd ? `$${stock.week52Low.toLocaleString()}` : `${stock.week52Low.toLocaleString()}원`) : "-"} />
      </div>
    </div>
  );
}

function DetailRow({
  label,
  value,
  positive,
  negative,
}: {
  label: string;
  value: string;
  positive?: boolean;
  negative?: boolean;
}) {
  return (
    <div className="flex justify-between">
      <span className="text-muted-foreground">{label}</span>
      <span
        className={`font-medium ${
          positive
            ? "text-emerald-400"
            : negative
              ? "text-rose-400"
              : "text-card-foreground"
        }`}
      >
        {value}
      </span>
    </div>
  );
}
