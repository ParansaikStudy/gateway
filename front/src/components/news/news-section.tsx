import NewsCard from "./news-card";
import type { NewsSection as NewsSectionType } from "@/mocks/news";

interface NewsSectionProps {
  section: NewsSectionType;
}

export default function NewsSection({ section }: NewsSectionProps) {
  return (
    <div>
      <h3 className="mb-3 text-base font-semibold text-foreground">
        {section.title}
      </h3>
      <div className="space-y-2">
        {section.items.map((item) => (
          <NewsCard key={item.id} item={item} />
        ))}
      </div>
    </div>
  );
}
