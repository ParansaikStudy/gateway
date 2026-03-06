"use client";

import { useCallback, useState } from "react";
import { loadTossPayments } from "@tosspayments/payment-sdk";

/** 개발용 1원, 운영 시 5000 등으로 변경 */
const PAYMENT_AMOUNT = 1;
const CLIENT_KEY = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY ?? "";

export type PaymentMethod = "카드" | "토스페이" | "카카오페이" | "네이버페이" | "삼성페이";

export interface PaymentCreateResult {
  orderId: string;
  amount: number;
  orderName: string;
}

export function usePayment() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const getBaseUrl = useCallback(() => {
    if (typeof window === "undefined") return "";
    return window.location.origin;
  }, []);

  const createOrder = useCallback(async (): Promise<PaymentCreateResult | null> => {
    const res = await fetch("/api/kis/payment/create", { method: "POST" });
    const data = await res.json();
    if (!res.ok) {
      setError((data as { error?: string }).error ?? "주문 생성 실패");
      return null;
    }
    return data as PaymentCreateResult;
  }, []);

  const requestPayment = useCallback(
    async (method: PaymentMethod): Promise<boolean> => {
      if (!CLIENT_KEY) {
        setError("결제 클라이언트 키가 설정되지 않았습니다.");
        return false;
      }
      setIsLoading(true);
      setError(null);
      try {
        const order = await createOrder();
        if (!order) return false;

        const baseUrl = getBaseUrl();
        const successUrl = `${baseUrl}/analysis?payment=success`;
        const failUrl = `${baseUrl}/analysis?payment=fail`;

        const tossPayments = await loadTossPayments(CLIENT_KEY);
        const params: Record<string, unknown> = {
          amount: order.amount,
          orderId: order.orderId,
          orderName: order.orderName,
          successUrl,
          failUrl,
        };
        if (method !== "카드") {
          params.flowMode = "DIRECT";
          params.easyPay = method;
        }
        await tossPayments.requestPayment("카드", params);
        return true;
      } catch (e) {
        const msg = e instanceof Error ? e.message : "결제 요청 실패";
        setError(msg);
        return false;
      } finally {
        setIsLoading(false);
      }
    },
    [createOrder, getBaseUrl],
  );

  const confirmPayment = useCallback(
    async (paymentKey: string, orderId: string, amount: number): Promise<boolean> => {
      const res = await fetch("/api/kis/payment/confirm", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ paymentKey, orderId, amount }),
      });
      const data = await res.json().catch(() => ({}));
      if (!res.ok) {
        setError((data as { error?: string }).error ?? "결제 승인 실패");
        return false;
      }
      return true;
    },
    [],
  );

  return {
    requestPayment,
    confirmPayment,
    isLoading,
    error,
    clearError: () => setError(null),
    paymentAmount: PAYMENT_AMOUNT,
    isClientKeySet: !!CLIENT_KEY,
  };
}
