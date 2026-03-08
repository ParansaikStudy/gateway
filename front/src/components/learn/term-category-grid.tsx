import {
  LineChart,
  FileText,
  TrendingUp,
  ArrowLeftRight,
  Building2,
  Layers,
} from "lucide-react";
import type { TermCategory } from "@/mocks/terms";

const iconMap: Record<string, React.ElementType> = {
  LineChart,
  FileText,
  TrendingUp,
  ArrowLeftRight,
  Building2,
  Layers,
};

interface TermCategoryGridProps {
  categories: TermCategory[];
  selectedId: string | null;
  onSelect: (id: string) => void;
}

export default function TermCategoryGrid({
  categories,
  selectedId,
  onSelect,
}: TermCategoryGridProps) {
  return (
    <div className="grid grid-cols-2 gap-3 md:grid-cols-3 lg:grid-cols-6">
      {categories.map((cat) => {
        const Icon = iconMap[cat.icon] ?? LineChart;
        const isSelected = selectedId === cat.id;
        return (
          <button
            key={cat.id}
            onClick={() => onSelect(cat.id)}
            className={`rounded-xl border p-4 text-left transition-all ${
              isSelected
                ? "border-primary bg-primary/10"
                : "border-border/50 bg-card/50 hover:bg-accent/30"
            } backdrop-blur-sm`}
          >
            <Icon
              className={`mb-2 h-5 w-5 ${isSelected ? "text-primary" : "text-muted-foreground"}`}
            />
            <div className="text-sm font-semibold text-card-foreground">
              {cat.name}
            </div>
            <div className="mt-0.5 text-xs text-muted-foreground">
              {cat.termCount}개 용어
            </div>
          </button>
        );
      })}
    </div>
  );
}
