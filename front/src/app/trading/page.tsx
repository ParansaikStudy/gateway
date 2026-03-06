"use client";

import { useState } from "react";
import Link from "next/link";
import { ShoppingCart, Settings, Loader2, ArrowUpCircle, ArrowDownCircle } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useKisCredentials, useKisToken } from "@/hooks/use-kis";
import { cn } from "@/lib/utils";

export default function TradingPage() {
  const { isConnected, environment } = useKisCredentials();
  const { data: tokenData, isLoading: tokenLoading } = useKisToken(isConnected);
  const tokenValid = !!tokenData?.valid;

  const [stockCode, setStockCode] = useState("");
  const [side, setSide] = useState<"buy" | "sell">("buy");
  const [orderType, setOrderType] = useState<"limit" | "market">("limit");
  const [quantity, setQuantity] = useState("");
  const [price, setPrice] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [result, setResult] = useState<{ success: boolean; message: string } | null>(null);

  const canSubmit =
    isConnected &&
    tokenValid &&
    /^\d{6}$/.test(stockCode.trim()) &&
    quantity.trim() !== "" &&
    Number(quantity) >= 1 &&
    (orderType === "market" || (price.trim() !== "" && Number(price) > 0));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!canSubmit || submitting) return;

    setSubmitting(true);
    setResult(null);

    try {
      const res = await fetch("/api/kis/order", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          side,
          stockCode: stockCode.trim(),
          orderType,
          quantity: Number(quantity),
          price: orderType === "limit" ? Number(price) : undefined,
        }),
      });

      const data = await res.json().catch(() => ({}));

      if (res.ok && data.success) {
        setResult({ success: true, message: data.message ?? "주문이 전송되었습니다." });
        setQuantity("");
        setPrice("");
      } else {
        setResult({
          success: false,
          message: data.error ?? "주문에 실패했습니다.",
        });
      }
    } catch {
      setResult({ success: false, message: "네트워크 오류가 발생했습니다." });
    } finally {
      setSubmitting(false);
    }
  };

  if (!isConnected) {
    return (
      <div className="mx-auto max-w-lg space-y-6">
        <div className="flex items-center gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-primary/10">
            <ShoppingCart className="h-5 w-5 text-primary" />
          </div>
          <div>
            <h2 className="text-lg font-semibold text-foreground">매수 / 매도</h2>
            <p className="text-sm text-muted-foreground">
              한국투자증권 API 연결 후 주문할 수 있습니다.
            </p>
          </div>
        </div>
        <div className="rounded-xl border border-border bg-card p-8 text-center">
          <p className="mb-4 text-sm text-muted-foreground">
            설정에서 한국투자증권 API(앱키, 시크릿, 계좌번호)를 입력하고 연결해 주세요.
          </p>
          <Button asChild>
            <Link href="/settings" className="inline-flex items-center gap-2">
              <Settings className="h-4 w-4" />
              설정으로 이동
            </Link>
          </Button>
        </div>
      </div>
    );
  }

  if (!tokenValid && !tokenLoading) {
    return (
      <div className="mx-auto max-w-lg space-y-6">
        <div className="flex items-center gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-primary/10">
            <ShoppingCart className="h-5 w-5 text-primary" />
          </div>
          <div>
            <h2 className="text-lg font-semibold text-foreground">매수 / 매도</h2>
            <p className="text-sm text-muted-foreground">
              토큰이 만료되었거나 연결에 실패했습니다. 설정에서 다시 연결해 주세요.
            </p>
          </div>
        </div>
        <div className="rounded-xl border border-amber-500/30 bg-amber-500/10 p-6 text-center">
          <p className="mb-4 text-sm text-amber-700 dark:text-amber-400">
            API 연결 상태를 확인할 수 없습니다.
          </p>
          <Button asChild variant="outline">
            <Link href="/settings" className="inline-flex items-center gap-2">
              <Settings className="h-4 w-4" />
              설정에서 다시 연결
            </Link>
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-lg space-y-6">
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-3">
          <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-primary/10">
            <ShoppingCart className="h-5 w-5 text-primary" />
          </div>
          <div>
            <h2 className="text-lg font-semibold text-foreground">매수 / 매도</h2>
            <p className="text-sm text-muted-foreground">
              국내주식 현금 주문 · {environment === "practice" ? "모의" : "실전"} 환경
            </p>
          </div>
        </div>
        <Button asChild variant="ghost" size="sm">
          <Link href="/settings">설정</Link>
        </Button>
      </div>

      <form onSubmit={handleSubmit} className="space-y-5 rounded-xl border border-border bg-card p-6">
        {/* 종목 (KIS 국내 6자리 종목코드) */}
        <div className="space-y-2">
          <label className="text-sm font-medium text-foreground">종목코드</label>
          <Input
            placeholder="종목코드 6자리 (예: 005930)"
            value={stockCode}
            onChange={(e) => {
              setStockCode(e.target.value.replace(/\D/g, "").slice(0, 6));
              setResult(null);
            }}
            className="font-mono max-w-[10rem]"
            maxLength={6}
          />
          {stockCode.length === 6 && (
            <p className="text-xs text-muted-foreground">
              종목코드: {stockCode}
            </p>
          )}
        </div>

        {/* 매수 / 매도 */}
        <div className="space-y-2">
          <label className="text-sm font-medium text-foreground">구분</label>
          <div className="flex gap-2">
            <button
              type="button"
              onClick={() => setSide("buy")}
              className={cn(
                "flex flex-1 items-center justify-center gap-2 rounded-lg border py-2.5 text-sm font-medium transition-colors",
                side === "buy"
                  ? "border-emerald-500 bg-emerald-500/15 text-emerald-600 dark:text-emerald-400"
                  : "border-border text-muted-foreground hover:bg-accent",
              )}
            >
              <ArrowDownCircle className="h-4 w-4" />
              매수
            </button>
            <button
              type="button"
              onClick={() => setSide("sell")}
              className={cn(
                "flex flex-1 items-center justify-center gap-2 rounded-lg border py-2.5 text-sm font-medium transition-colors",
                side === "sell"
                  ? "border-red-500 bg-red-500/15 text-red-600 dark:text-red-400"
                  : "border-border text-muted-foreground hover:bg-accent",
              )}
            >
              <ArrowUpCircle className="h-4 w-4" />
              매도
            </button>
          </div>
        </div>

        {/* 주문 유형 */}
        <div className="space-y-2">
          <label className="text-sm font-medium text-foreground">주문 유형</label>
          <div className="flex gap-2">
            <button
              type="button"
              onClick={() => setOrderType("limit")}
              className={cn(
                "flex-1 rounded-lg border py-2.5 text-sm font-medium transition-colors",
                orderType === "limit"
                  ? "border-primary bg-primary/10 text-primary"
                  : "border-border text-muted-foreground hover:bg-accent",
              )}
            >
              지정가
            </button>
            <button
              type="button"
              onClick={() => setOrderType("market")}
              className={cn(
                "flex-1 rounded-lg border py-2.5 text-sm font-medium transition-colors",
                orderType === "market"
                  ? "border-primary bg-primary/10 text-primary"
                  : "border-border text-muted-foreground hover:bg-accent",
              )}
            >
              시장가
            </button>
          </div>
        </div>

        {/* 수량 */}
        <div className="space-y-2">
          <label className="text-sm font-medium text-foreground">수량 (주)</label>
          <Input
            type="number"
            min={1}
            placeholder="주문 수량"
            value={quantity}
            onChange={(e) => setQuantity(e.target.value.replace(/\D/g, "").slice(0, 8))}
          />
        </div>

        {/* 주문 단가 (지정가일 때만) */}
        {orderType === "limit" && (
          <div className="space-y-2">
            <label className="text-sm font-medium text-foreground">주문 단가 (원)</label>
            <Input
              type="number"
              min={1}
              placeholder="예: 72000"
              value={price}
              onChange={(e) => setPrice(e.target.value.replace(/\D/g, "").slice(0, 10))}
            />
          </div>
        )}

        {/* 결과 메시지 */}
        {result && (
          <div
            className={cn(
              "rounded-lg border px-4 py-3 text-sm",
              result.success
                ? "border-emerald-500/30 bg-emerald-500/10 text-emerald-700 dark:text-emerald-400"
                : "border-destructive/30 bg-destructive/10 text-destructive",
            )}
          >
            {result.message}
          </div>
        )}

        {/* 주문하기 */}
        <Button
          type="submit"
          disabled={!canSubmit || submitting}
          className={cn(
            "w-full",
            side === "buy"
              ? "bg-emerald-600 hover:bg-emerald-700"
              : "bg-red-600 hover:bg-red-700",
          )}
        >
          {submitting ? (
            <>
              <Loader2 className="h-4 w-4 animate-spin" />
              처리 중…
            </>
          ) : (
            <>
              {side === "buy" ? (
                <ArrowDownCircle className="h-4 w-4" />
              ) : (
                <ArrowUpCircle className="h-4 w-4" />
              )}
              {side === "buy" ? "매수" : "매도"} 주문하기
            </>
          )}
        </Button>

        <p className="text-center text-xs text-muted-foreground">
          모의투자에서 충분히 테스트한 후 실전을 사용하세요. 주문 전 반드시 종목·수량·가격을 확인하세요.
        </p>
      </form>
    </div>
  );
}
