import { TrendingUp, TrendingDown } from "lucide-react";
import type { MarketIndex } from "@/mocks/stocks";

interface MarketIndexCardProps {
  index: MarketIndex;
}

export default function MarketIndexCard({ index }: MarketIndexCardProps) {
  const isPositive = index.change >= 0;

  return (
    <div className="rounded-xl border border-border/50 bg-card/50 p-5 backdrop-blur-sm">
      <div className="mb-1 text-sm font-medium text-muted-foreground">
        {index.name}
      </div>
      <div className="mb-2 text-2xl font-bold text-card-foreground">
        {index.value.toLocaleString("ko-KR", { minimumFractionDigits: 2 })}
      </div>
      <div
        className={`flex items-center gap-1.5 text-sm font-medium ${
          isPositive ? "text-emerald-400" : "text-rose-400"
        }`}
      >
        {isPositive ? (
          <TrendingUp className="h-3.5 w-3.5" />
        ) : (
          <TrendingDown className="h-3.5 w-3.5" />
        )}
        <span>
          {isPositive ? "+" : ""}
          {index.change.toLocaleString("ko-KR", { minimumFractionDigits: 2 })}
        </span>
        <span>
          ({isPositive ? "+" : ""}
          {index.changePercent.toFixed(2)}%)
        </span>
      </div>
    </div>
  );
}
