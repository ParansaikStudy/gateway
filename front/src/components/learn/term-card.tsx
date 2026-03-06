"use client";

import { useState } from "react";
import { ChevronDown, ChevronUp, Lightbulb } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import type { Term } from "@/mocks/terms";

interface TermCardProps {
  term: Term;
}

export default function TermCard({ term }: TermCardProps) {
  const [expanded, setExpanded] = useState(false);

  const difficultyColor =
    term.difficulty === "초급"
      ? "bg-emerald-500/20 text-emerald-400 border-emerald-500/30"
      : term.difficulty === "중급"
        ? "bg-yellow-500/20 text-yellow-400 border-yellow-500/30"
        : "bg-rose-500/20 text-rose-400 border-rose-500/30";

  return (
    <div className="rounded-xl border border-border/50 bg-card/50 backdrop-blur-sm transition-colors hover:bg-accent/20">
      <button
        onClick={() => setExpanded(!expanded)}
        className="flex w-full items-center justify-between p-4 text-left"
      >
        <div className="flex items-center gap-3">
          <h3 className="font-semibold text-card-foreground">{term.name}</h3>
          <Badge className={difficultyColor}>{term.difficulty}</Badge>
        </div>
        {expanded ? (
          <ChevronUp className="h-4 w-4 text-muted-foreground" />
        ) : (
          <ChevronDown className="h-4 w-4 text-muted-foreground" />
        )}
      </button>

      {expanded && (
        <div className="border-t border-border/30 px-4 pb-4 pt-3">
          <p className="mb-3 text-sm leading-relaxed text-muted-foreground">
            {term.definition}
          </p>
          <div className="rounded-lg bg-accent/50 p-3">
            <div className="mb-1.5 flex items-center gap-1.5">
              <Lightbulb className="h-3.5 w-3.5 text-yellow-400" />
              <span className="text-xs font-semibold text-card-foreground">
                예시
              </span>
            </div>
            <p className="text-xs leading-relaxed text-muted-foreground">
              {term.example}
            </p>
          </div>
        </div>
      )}
    </div>
  );
}
