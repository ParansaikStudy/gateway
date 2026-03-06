import MarketIndexCard from "@/components/dashboard/market-index-card";
import HotThemeSection from "@/components/dashboard/hot-theme-section";
import PickSection from "@/components/dashboard/ai-pick-section";
import { marketIndices, hotThemes, aiPickStocks } from "@/mocks/stocks";

export default function DashboardPage() {
  return (
    <div className="mx-auto max-w-6xl space-y-8">
      {/* Market Indices */}
      <section>
        <h2 className="mb-4 text-lg font-semibold text-foreground">
          시장 지수
        </h2>
        <div className="grid grid-cols-2 gap-3 lg:grid-cols-4">
          {marketIndices.map((index) => (
            <MarketIndexCard key={index.name} index={index} />
          ))}
        </div>
      </section>

      {/* Hot Themes */}
      <HotThemeSection themes={hotThemes} />

      {/* Top Picks */}
      <PickSection stocks={aiPickStocks} />
    </div>
  );
}
