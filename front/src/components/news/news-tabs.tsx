"use client";

import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import NewsSection from "./news-section";
import type { NewsSection as NewsSectionType } from "@/mocks/news";

interface NewsTabsProps {
  domesticSections: NewsSectionType[];
  usSections: NewsSectionType[];
}

export default function NewsTabs({
  domesticSections,
  usSections,
}: NewsTabsProps) {
  return (
    <Tabs defaultValue="domestic">
      <TabsList className="mb-6">
        <TabsTrigger value="domestic">국내 시장</TabsTrigger>
        <TabsTrigger value="us">미국 시장</TabsTrigger>
      </TabsList>

      <TabsContent value="domestic" className="space-y-6">
        {domesticSections.map((section) => (
          <NewsSection key={section.title} section={section} />
        ))}
      </TabsContent>

      <TabsContent value="us" className="space-y-6">
        {usSections.map((section) => (
          <NewsSection key={section.title} section={section} />
        ))}
      </TabsContent>
    </Tabs>
  );
}
