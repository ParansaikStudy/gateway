"use client";

import { useState } from "react";
import TermCategoryGrid from "@/components/learn/term-category-grid";
import TermCard from "@/components/learn/term-card";
import { termCategories, terms } from "@/mocks/terms";

export default function LearnPage() {
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);

  const filteredTerms = selectedCategory
    ? terms.filter((t) => t.categoryId === selectedCategory)
    : terms;

  const selectedCategoryName = selectedCategory
    ? termCategories.find((c) => c.id === selectedCategory)?.name
    : null;

  return (
    <div className="mx-auto max-w-6xl space-y-6">
      {/* Category Grid */}
      <TermCategoryGrid
        categories={termCategories}
        selectedId={selectedCategory}
        onSelect={(id) =>
          setSelectedCategory((prev) => (prev === id ? null : id))
        }
      />

      {/* Terms List */}
      <div>
        <div className="mb-4 flex items-center justify-between">
          <h2 className="text-lg font-semibold text-foreground">
            {selectedCategoryName ?? "전체 용어"}
          </h2>
          <span className="text-sm text-muted-foreground">
            {filteredTerms.length}개 용어
          </span>
        </div>
        <div className="space-y-2">
          {filteredTerms.map((term) => (
            <TermCard key={term.id} term={term} />
          ))}
        </div>
      </div>
    </div>
  );
}
