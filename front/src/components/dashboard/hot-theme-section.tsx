import { Flame, TrendingUp } from "lucide-react";
import type { HotTheme } from "@/mocks/stocks";

interface HotThemeSectionProps {
  themes: HotTheme[];
}

export default function HotThemeSection({ themes }: HotThemeSectionProps) {
  return (
    <section>
      <div className="mb-4 flex items-center gap-2">
        <Flame className="h-5 w-5 text-orange-400" />
        <h2 className="text-lg font-semibold text-foreground">
          오늘의 핫 테마
        </h2>
      </div>
      <div className="grid grid-cols-1 gap-3 md:grid-cols-2 lg:grid-cols-3">
        {themes.map((theme) => (
          <div
            key={theme.id}
            className="rounded-xl border border-border/50 bg-card/50 p-4 backdrop-blur-sm transition-colors hover:bg-accent/30"
          >
            <div className="mb-2 flex items-center justify-between">
              <h3 className="font-semibold text-card-foreground">
                {theme.name}
              </h3>
              <div className="flex items-center gap-1 text-sm font-medium text-emerald-400">
                <TrendingUp className="h-3.5 w-3.5" />+{theme.change.toFixed(2)}%
              </div>
            </div>
            <p className="mb-3 text-xs text-muted-foreground">
              {theme.description}
            </p>
            <div className="flex flex-wrap gap-1.5">
              {theme.stocks.map((stock) => (
                <span
                  key={stock}
                  className="rounded-md bg-primary/10 px-2 py-0.5 text-xs font-medium text-primary"
                >
                  {stock}
                </span>
              ))}
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}
