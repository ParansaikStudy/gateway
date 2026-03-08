"use client";

import { usePathname } from "next/navigation";
import { Bell } from "lucide-react";

const pageTitles: Record<string, string> = {
  "/": "대시보드",
  "/analysis": "종목 분석",
  "/learn": "용어 학습",
  "/news": "시장 뉴스",
  "/settings": "설정",
};

export default function Header() {
  const pathname = usePathname();
  const title = pageTitles[pathname] ?? "FinSight";

  return (
    <header className="sticky top-0 z-30 flex h-16 items-center justify-between border-b border-border bg-background/80 px-6 backdrop-blur-md">
      <h1 className="text-lg font-semibold text-foreground">{title}</h1>
      <div className="flex items-center gap-3">
        <button className="relative rounded-lg p-2 text-muted-foreground transition-colors hover:bg-accent hover:text-foreground">
          <Bell className="h-4.5 w-4.5" />
          <span className="absolute right-1.5 top-1.5 h-2 w-2 rounded-full bg-primary" />
        </button>
      </div>
    </header>
  );
}
