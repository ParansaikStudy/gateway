import { NextRequest, NextResponse } from "next/server";

// 백엔드 Gateway: Eureka local 17000, 기본 7003 (8000은 사용 안 함 → 17000으로 보정)
const BACKEND_URL = (() => {
  const raw =
    process.env.API_URL ||
    process.env.NEXT_PUBLIC_API_URL ||
    "http://localhost:17000";
  if (raw.includes("localhost:8000")) return "http://localhost:17000";
  return raw;
})();

/** 백엔드 POST /api/kis/analysis 프록시 — KIS 현재가·일봉 기반 분석(summary, fullAnalysis) 반환 */
export async function POST(req: NextRequest) {
  try {
    const body = (await req.json()) as Record<string, unknown>;
    const stockCode = body?.stockCode;

    if (!stockCode || typeof stockCode !== "string") {
      return NextResponse.json(
        { error: "stockCode is required" },
        { status: 400 },
      );
    }

    const url = `${BACKEND_URL}/api/stock/analysis`;
    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });

    const rawText = await res.text();
    let data: Record<string, unknown> = {};
    try {
      data = rawText ? (JSON.parse(rawText) as Record<string, unknown>) : {};
    } catch {
      // 백엔드가 JSON이 아닌 응답(HTML 에러 페이지 등)을 주는 경우
      console.error("[api/kis/analysis] Backend returned non-JSON. Status:", res.status, "URL:", url);
      return NextResponse.json(
        { error: `백엔드 응답 오류 (${res.status}). Gateway·Stock 서비스 상태를 확인하세요.` },
        { status: 500 },
      );
    }

    if (!res.ok) {
      const message =
        (data.error as string) ??
        (data.message as string) ??
        (typeof data.payload === "string" ? data.payload : null) ??
        "분석 요청에 실패했습니다.";
      console.error("[api/kis/analysis] Backend error:", res.status, message, "URL:", url);
      return NextResponse.json({ error: message }, { status: res.status });
    }

    return NextResponse.json(data);
  } catch (e) {
    const err = e as Error & { code?: string };
    const isConnectionError =
      err?.code === "ECONNREFUSED" ||
      err?.message?.includes("ECONNREFUSED") ||
      err?.message?.includes("fetch failed");
    const message = isConnectionError
      ? `백엔드에 연결할 수 없습니다. (${BACKEND_URL}) Gateway가 실행 중인지, 포트가 맞는지 확인하세요.`
      : err?.message ?? "분석 요청 중 오류가 발생했습니다.";
    console.error("[api/kis/analysis]", err?.message ?? e);
    return NextResponse.json({ error: message }, { status: 500 });
  }
}
