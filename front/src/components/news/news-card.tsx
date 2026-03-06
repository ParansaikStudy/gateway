import { Clock } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import type { NewsItem } from "@/mocks/news";

interface NewsCardProps {
  item: NewsItem;
}

export default function NewsCard({ item }: NewsCardProps) {
  return (
    <div className="rounded-xl border border-border/50 bg-card/50 p-4 backdrop-blur-sm transition-colors hover:bg-accent/30">
      <div className="mb-2 flex items-center gap-2">
        <Badge variant="secondary" className="text-xs">
          {item.category}
        </Badge>
        <span className="text-xs text-muted-foreground">{item.source}</span>
        <div className="flex items-center gap-1 text-xs text-muted-foreground">
          <Clock className="h-3 w-3" />
          {item.time}
        </div>
      </div>
      <h3 className="mb-1.5 text-sm font-semibold leading-snug text-card-foreground">
        {item.title}
      </h3>
      <p className="text-xs leading-relaxed text-muted-foreground">
        {item.summary}
      </p>
    </div>
  );
}
