import {
  BarChart3,
  TrendingUp,
  TrendingDown,
  Minus,
  Target,
  ShieldAlert,
  CheckCircle2,
  AlertTriangle,
  Info,
} from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import type { AIAnalysis } from "@/mocks/stocks";

interface AIAnalysisPanelProps {
  analysis: AIAnalysis;
}

export default function AIAnalysisPanel({ analysis }: AIAnalysisPanelProps) {
  const pressureIcon =
    analysis.pressure === "상방" ? (
      <TrendingUp className="h-5 w-5 text-emerald-400" />
    ) : analysis.pressure === "하방" ? (
      <TrendingDown className="h-5 w-5 text-rose-400" />
    ) : (
      <Minus className="h-5 w-5 text-yellow-400" />
    );

  const pressureColor =
    analysis.pressure === "상방"
      ? "text-emerald-400"
      : analysis.pressure === "하방"
        ? "text-rose-400"
        : "text-yellow-400";

  const sentimentColor =
    analysis.sentiment === "긍정적"
      ? "bg-emerald-500/20 text-emerald-400 border-emerald-500/30"
      : analysis.sentiment === "부정적"
        ? "bg-rose-500/20 text-rose-400 border-rose-500/30"
        : "bg-yellow-500/20 text-yellow-400 border-yellow-500/30";

  return (
    <div className="rounded-xl border border-border/50 bg-card/50 p-6 backdrop-blur-sm">
      {/* Header */}
      <div className="mb-6 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <BarChart3 className="h-5 w-5 text-primary" />
          <h2 className="text-lg font-semibold text-card-foreground">
            스마트 시장 분석
          </h2>
        </div>
        <Badge className={sentimentColor}>
          {analysis.sentiment}
        </Badge>
      </div>

      {/* Pressure Indicator */}
      <div className="mb-5 rounded-lg bg-accent/50 p-4">
        <div className="mb-2 flex items-center gap-2">
          {pressureIcon}
          <span className={`text-base font-semibold ${pressureColor}`}>
            {analysis.pressure} 압력
          </span>
          <span className="text-sm text-muted-foreground">
            (강도: {analysis.pressureScore}/100)
          </span>
        </div>
        {/* Pressure Bar */}
        <div className="h-2 w-full overflow-hidden rounded-full bg-muted">
          <div
            className={`h-full rounded-full transition-all ${
              analysis.pressure === "상방"
                ? "bg-emerald-400"
                : analysis.pressure === "하방"
                  ? "bg-rose-400"
                  : "bg-yellow-400"
            }`}
            style={{ width: `${analysis.pressureScore}%` }}
          />
        </div>
      </div>

      {/* Technical Summary */}
      <div className="mb-5">
        <h3 className="mb-2 text-sm font-semibold text-card-foreground">
          기술적 분석
        </h3>
        <p className="text-sm leading-relaxed text-muted-foreground">
          {analysis.technicalSummary}
        </p>
        <div className="mt-2 text-sm">
          <span className="text-muted-foreground">이동평균 배열: </span>
          <span className="font-medium text-primary">
            {analysis.maPosition}
          </span>
        </div>
      </div>

      <Separator className="mb-5" />

      {/* Historical Pattern Reference */}
      <div className="mb-5 grid grid-cols-2 gap-4">
        <div className="rounded-lg bg-emerald-500/10 p-3">
          <div className="mb-1 text-xs text-muted-foreground">
            과거 유사 패턴 상승 범위
          </div>
          <div className="text-xl font-bold text-emerald-400">
            +{analysis.historicalUpside}%
          </div>
        </div>
        <div className="rounded-lg bg-rose-500/10 p-3">
          <div className="mb-1 text-xs text-muted-foreground">
            과거 유사 패턴 하락 범위
          </div>
          <div className="text-xl font-bold text-rose-400">
            {analysis.historicalDownside}%
          </div>
        </div>
      </div>

      {/* Resistance & Support */}
      <div className="mb-5 grid grid-cols-2 gap-4">
        <div className="flex items-center gap-2">
          <Target className="h-4 w-4 text-primary" />
          <div>
            <div className="text-xs text-muted-foreground">기술적 저항선</div>
            <div className="font-semibold text-card-foreground">
              {analysis.resistanceLevel.toLocaleString()}원
            </div>
          </div>
        </div>
        <div className="flex items-center gap-2">
          <ShieldAlert className="h-4 w-4 text-rose-400" />
          <div>
            <div className="text-xs text-muted-foreground">기술적 지지선</div>
            <div className="font-semibold text-card-foreground">
              {analysis.supportLevel.toLocaleString()}원
            </div>
          </div>
        </div>
      </div>

      <Separator className="mb-5" />

      {/* Positive Factors */}
      <div className="mb-5">
        <h3 className="mb-2 flex items-center gap-1.5 text-sm font-semibold text-card-foreground">
          <CheckCircle2 className="h-4 w-4 text-emerald-400" />
          긍정 요인
        </h3>
        <ul className="space-y-1.5">
          {analysis.positiveFactors.map((factor, i) => (
            <li key={i} className="flex gap-2 text-sm text-muted-foreground">
              <span className="mt-1.5 h-1 w-1 shrink-0 rounded-full bg-emerald-400" />
              {factor}
            </li>
          ))}
        </ul>
      </div>

      {/* Risk Factors */}
      <div className="mb-5">
        <h3 className="mb-2 flex items-center gap-1.5 text-sm font-semibold text-card-foreground">
          <AlertTriangle className="h-4 w-4 text-yellow-400" />
          리스크 요인
        </h3>
        <ul className="space-y-1.5">
          {analysis.riskFactors.map((risk, i) => (
            <li key={i} className="flex gap-2 text-sm text-muted-foreground">
              <span className="mt-1.5 h-1 w-1 shrink-0 rounded-full bg-yellow-400" />
              {risk}
            </li>
          ))}
        </ul>
      </div>

      {/* Legal Disclaimer */}
      <div className="flex gap-2 rounded-lg border border-border/50 bg-muted/30 p-3">
        <Info className="mt-0.5 h-4 w-4 shrink-0 text-muted-foreground" />
        <p className="text-[11px] leading-relaxed text-muted-foreground">
          본 분석은 기술적 지표와 과거 데이터를 기반으로 자동 생성된 참고
          자료이며, 투자자문에 해당하지 않습니다. 특정 종목의 매수·매도를
          권유하지 않으며, 투자에 따른 최종 판단과 책임은 이용자 본인에게
          있습니다. 과거 패턴이 미래 수익률을 보장하지 않습니다.
        </p>
      </div>
    </div>
  );
}
