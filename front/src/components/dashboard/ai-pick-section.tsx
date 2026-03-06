import { Sparkles } from "lucide-react";
import StockCard from "./stock-card";
import type { Stock } from "@/mocks/stocks";

interface PickSectionProps {
  stocks: Stock[];
}

export default function PickSection({ stocks }: PickSectionProps) {
  return (
    <section>
      <div className="mb-4 flex items-center gap-2">
        <Sparkles className="h-5 w-5 text-primary" />
        <h2 className="text-lg font-semibold text-foreground">주요 관심 종목</h2>
      </div>
      <div className="space-y-2">
        {stocks.map((stock) => (
          <StockCard key={stock.code} stock={stock} />
        ))}
      </div>
    </section>
  );
}
