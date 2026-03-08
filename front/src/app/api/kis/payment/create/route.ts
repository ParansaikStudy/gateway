import { NextResponse } from "next/server";

const BACKEND_URL =
  process.env.API_URL ||
  process.env.NEXT_PUBLIC_API_URL ||
  "http://localhost:17000";

/** 백엔드 POST /api/stock/payment/create — 주문 ID·금액(5,000원) 반환 */
export async function POST() {
  try {
    const res = await fetch(`${BACKEND_URL}/api/stock/payment/create`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
    });
    const data = await res.json().catch(() => ({}));
    if (!res.ok) {
      return NextResponse.json(
        { error: (data as { error?: string }).error ?? "결제 주문 생성 실패" },
        { status: res.status },
      );
    }
    return NextResponse.json(data);
  } catch (e) {
    return NextResponse.json(
      { error: e instanceof Error ? e.message : "결제 주문 생성 중 오류" },
      { status: 500 },
    );
  }
}
