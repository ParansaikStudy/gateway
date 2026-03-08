"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import {
  LayoutDashboard,
  Search,
  ShoppingCart,
  BookOpen,
  Newspaper,
  Settings,
  TrendingUp,
} from "lucide-react";
import { cn } from "@/lib/utils";

const navItems = [
  { href: "/", label: "대시보드", icon: LayoutDashboard },
  { href: "/analysis", label: "종목 분석", icon: Search },
  { href: "/trading", label: "매매", icon: ShoppingCart },
  { href: "/learn", label: "용어 학습", icon: BookOpen },
  { href: "/news", label: "시장 뉴스", icon: Newspaper },
  { href: "/settings", label: "설정", icon: Settings },
];

export default function Sidebar() {
  const pathname = usePathname();

  return (
    <aside className="fixed left-0 top-0 z-40 flex h-screen w-60 flex-col border-r border-sidebar-border bg-sidebar">
      {/* Logo */}
      <div className="flex h-16 items-center gap-2.5 px-6">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary">
          <TrendingUp className="h-4.5 w-4.5 text-primary-foreground" />
        </div>
        <span className="text-lg font-bold tracking-tight text-sidebar-foreground">
          FinSight
        </span>
      </div>

      {/* Navigation */}
      <nav className="flex-1 space-y-1 px-3 py-4">
        {navItems.map((item) => {
          const isActive =
            pathname === item.href ||
            (item.href !== "/" && pathname.startsWith(item.href));
          return (
            <Link
              key={item.href}
              href={item.href}
              className={cn(
                "flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-colors",
                isActive
                  ? "bg-sidebar-accent text-sidebar-primary"
                  : "text-sidebar-foreground/70 hover:bg-sidebar-accent/50 hover:text-sidebar-foreground",
              )}
            >
              <item.icon className="h-4.5 w-4.5" />
              {item.label}
            </Link>
          );
        })}
      </nav>

      {/* Footer */}
      <div className="border-t border-sidebar-border p-4">
        <p className="text-xs text-sidebar-foreground/50">
          FinSight v0.1.0
        </p>
      </div>
    </aside>
  );
}
