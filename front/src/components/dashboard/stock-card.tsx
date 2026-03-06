import { TrendingUp, TrendingDown, BarChart3 } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import type { Stock } from "@/mocks/stocks";

interface StockCardProps {
  stock: Stock;
}

export default function StockCard({ stock }: StockCardProps) {
  const isPositive = stock.change >= 0;

  return (
    <div className="flex items-center gap-4 rounded-xl border border-border/50 bg-card/50 p-4 backdrop-blur-sm transition-colors hover:bg-accent/30">
      {/* Rank */}
      <div className="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-primary/10 text-sm font-bold text-primary">
        {stock.rank}
      </div>

      {/* Stock Info */}
      <div className="min-w-0 flex-1">
        <div className="flex items-center gap-2">
          <span className="font-semibold text-card-foreground">
            {stock.name}
          </span>
          <span className="text-xs text-muted-foreground">{stock.code}</span>
        </div>
        <div className="mt-1 flex items-center gap-1.5">
          <BarChart3 className="h-3 w-3 text-primary" />
          <p className="truncate text-xs text-muted-foreground">
            {stock.aiSummary}
          </p>
        </div>
      </div>

      {/* Price & Change */}
      <div className="shrink-0 text-right">
        <div className="font-semibold text-card-foreground">
          {stock.price.toLocaleString("ko-KR")}원
        </div>
        <div
          className={`flex items-center justify-end gap-1 text-sm ${
            isPositive ? "text-emerald-400" : "text-rose-400"
          }`}
        >
          {isPositive ? (
            <TrendingUp className="h-3 w-3" />
          ) : (
            <TrendingDown className="h-3 w-3" />
          )}
          <span>
            {isPositive ? "+" : ""}
            {stock.changePercent.toFixed(2)}%
          </span>
        </div>
      </div>

      {/* Score */}
      <Badge
        variant={stock.aiScore >= 80 ? "default" : "secondary"}
        className="shrink-0"
      >
        {stock.aiScore}점
      </Badge>
    </div>
  );
}
