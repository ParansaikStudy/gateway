import NewsTabs from "@/components/news/news-tabs";
import { domesticNews, usNews } from "@/mocks/news";

export default function NewsPage() {
  return (
    <div className="mx-auto max-w-4xl">
      <NewsTabs domesticSections={domesticNews} usSections={usNews} />
    </div>
  );
}
