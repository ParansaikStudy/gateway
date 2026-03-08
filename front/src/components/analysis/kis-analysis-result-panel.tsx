"use client";

import { BarChart3, Info } from "lucide-react";
import type { KisAnalysisResponse } from "@/lib/kis";

interface KisAnalysisResultPanelProps {
  result: KisAnalysisResponse;
  /** 종목명(코드) — 있으면 결론 문장의 {주식명(티커 xxx)} 치환 */
  stockDisplayName?: string;
}

export default function KisAnalysisResultPanel({
  result,
  stockDisplayName,
}: KisAnalysisResultPanelProps) {
  const conclusionText =
    result.conclusion && stockDisplayName
      ? result.conclusion.replace(
          /\{주식명\(티커\s*[^)]*\)\}/,
          stockDisplayName,
        )
      : result.conclusion ?? null;

  return (
    <div className="rounded-xl border border-border/50 bg-card/50 p-6 backdrop-blur-sm">
      <div className="mb-4 flex items-center gap-2">
        <BarChart3 className="h-5 w-5 text-primary" />
        <h2 className="text-lg font-semibold text-card-foreground">
          스마트 시장 분석
        </h2>
      </div>

      {/* 50자 이하 요약 */}
      <div className="mb-5 rounded-lg bg-primary/10 px-4 py-3">
        <p className="text-sm font-medium leading-relaxed text-foreground">
          {result.summary}
        </p>
      </div>

      {/* 사용자용 결론 (압력·상황·매도/매수) */}
      {conclusionText ? (
        <div className="mb-5 rounded-lg border border-primary/30 bg-primary/5 px-4 py-4">
          <h3 className="mb-2 text-sm font-semibold text-card-foreground">
            결론
          </h3>
          <p className="whitespace-pre-line text-sm leading-relaxed text-foreground">
            {conclusionText}
          </p>
        </div>
      ) : null}

      {/* 전체 분석 문장 (수치) */}
      <div className="mb-5">
        <h3 className="mb-2 text-sm font-semibold text-card-foreground">
          상세 분석
        </h3>
        <p className="whitespace-pre-line text-sm leading-relaxed text-muted-foreground">
          {result.fullAnalysis}
        </p>
      </div>

      <div className="flex gap-2 rounded-lg border border-border/50 bg-muted/30 p-3">
        <Info className="mt-0.5 h-4 w-4 shrink-0 text-muted-foreground" />
        <p className="text-[11px] leading-relaxed text-muted-foreground">
          본 분석은 기술적 지표와 과거 데이터를 기반으로 자동 생성된 참고
          자료이며, 투자자문에 해당하지 않습니다. 투자에 따른 최종 판단과
          책임은 이용자 본인에게 있습니다.
        </p>
      </div>
    </div>
  );
}
