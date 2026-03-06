"use client";

import { useState, useRef, useEffect, useCallback } from "react";
import { Search } from "lucide-react";
import { Input } from "@/components/ui/input";
import { searchStocks, type SearchableStock } from "@/lib/stock-search-list";

export interface StockSearchResult {
  code: string;
  market: "KR" | "US";
  name: string;
}

interface StockSearchBarProps {
  onSearch: (result: StockSearchResult) => void;
}

/** 종목명(엔비디아, 애플) 또는 종목코드(005930, NVDA)로 검색 → 선택 시 KIS 실시간 시세/분석 */
export default function StockSearchBar({ onSearch }: StockSearchBarProps) {
  const [query, setQuery] = useState("");
  const [suggestions, setSuggestions] = useState<SearchableStock[]>([]);
  const [isOpen, setIsOpen] = useState(false);
  const [activeIndex, setActiveIndex] = useState(-1);
  const containerRef = useRef<HTMLDivElement>(null);
  const listRef = useRef<HTMLUListElement>(null);

  const updateSuggestions = useCallback((value: string) => {
    const list = searchStocks(value);
    setSuggestions(list);
    setIsOpen(list.length > 0);
    setActiveIndex(-1);
  }, []);

  const handleChange = (value: string) => {
    setQuery(value);
    updateSuggestions(value);
  };

  const selectStock = (stock: SearchableStock) => {
    setQuery(stock.name);
    setIsOpen(false);
    setSuggestions([]);
    setActiveIndex(-1);
    onSearch({ code: stock.code, market: stock.market, name: stock.name });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const trimmed = query.trim();
    if (!trimmed) return;
    // 추천 목록에서 정확히 일치하는 항목 있으면 선택
    const list = searchStocks(trimmed);
    const exact = list.find(
      (s) =>
        s.name.toLowerCase() === trimmed.toLowerCase() ||
        s.code.toUpperCase() === trimmed.toUpperCase(),
    );
    if (exact) {
      selectStock(exact);
    } else if (list.length > 0) {
      selectStock(list[0]);
    } else {
      // 6자리 숫자면 국내 코드로 간주
      if (/^\d{6}$/.test(trimmed)) {
        onSearch({ code: trimmed, market: "KR", name: trimmed });
      } else {
        onSearch({ code: trimmed, market: "US", name: trimmed });
      }
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (!isOpen || suggestions.length === 0) return;
    if (e.key === "ArrowDown") {
      e.preventDefault();
      setActiveIndex((i) => (i < suggestions.length - 1 ? i + 1 : 0));
    } else if (e.key === "ArrowUp") {
      e.preventDefault();
      setActiveIndex((i) => (i > 0 ? i - 1 : suggestions.length - 1));
    } else if (e.key === "Enter" && activeIndex >= 0) {
      e.preventDefault();
      selectStock(suggestions[activeIndex]);
    } else if (e.key === "Escape") {
      setIsOpen(false);
      setActiveIndex(-1);
    }
  };

  useEffect(() => {
    if (activeIndex >= 0 && listRef.current) {
      const el = listRef.current.children[activeIndex] as HTMLElement | undefined;
      el?.scrollIntoView({ block: "nearest" });
    }
  }, [activeIndex]);

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setIsOpen(false);
        setActiveIndex(-1);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  return (
    <div ref={containerRef} className="relative">
      <form onSubmit={handleSubmit} className="relative">
        <Search className="absolute left-3 top-1/2 h-4.5 w-4.5 -translate-y-1/2 text-muted-foreground" />
        <Input
          type="text"
          placeholder="종목명 또는 종목코드 입력 (예: 엔비디아, 애플, 005930)"
          value={query}
          onChange={(e) => handleChange(e.target.value)}
          onKeyDown={handleKeyDown}
          onFocus={() => updateSuggestions(query)}
          className="h-12 rounded-xl border-border/50 bg-card/50 pl-10 text-base backdrop-blur-sm placeholder:text-muted-foreground/50"
          autoComplete="off"
        />
      </form>

      {isOpen && suggestions.length > 0 && (
        <ul
          ref={listRef}
          className="absolute z-50 mt-1 max-h-72 w-full overflow-auto rounded-xl border border-border/50 bg-card shadow-lg backdrop-blur-sm"
        >
          {suggestions.map((stock, index) => (
            <li
              key={`${stock.market}-${stock.code}`}
              onMouseDown={() => selectStock(stock)}
              onMouseEnter={() => setActiveIndex(index)}
              className={`flex cursor-pointer items-center justify-between px-4 py-3 text-sm transition-colors ${
                index === activeIndex
                  ? "bg-accent text-accent-foreground"
                  : "text-foreground hover:bg-accent/50"
              }`}
            >
              <span className="font-medium">{stock.name}</span>
              <span className="flex items-center gap-2 text-muted-foreground">
                <span>{stock.code}</span>
                <span
                  className={`rounded px-1.5 py-0.5 text-xs font-medium ${
                    stock.market === "KR"
                      ? "bg-blue-500/10 text-blue-500"
                      : "bg-emerald-500/10 text-emerald-500"
                  }`}
                >
                  {stock.market === "KR" ? "KRX" : "US"}
                </span>
              </span>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
