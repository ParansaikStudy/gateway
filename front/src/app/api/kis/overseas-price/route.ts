import { NextRequest, NextResponse } from "next/server";
import { KIS_BASE_URL, KIS_TR_ID_OVERSEAS_PRICE } from "@/lib/kis";
import { kisThrottle } from "@/lib/kis-throttle";
import {
  getCredentialsFromRequest,
  getTokenFromRequest,
} from "@/lib/credentials";

/**
 * 해외주식 현재가 조회 (KIS) — 공식 샘플 기준 price API.
 * GET /api/kis/overseas-price?stockCode=AAPL&exchange=NAS
 */
export async function GET(req: NextRequest) {
  try {
    const creds = getCredentialsFromRequest(req);
    const token = getTokenFromRequest(req);

    if (!creds || !token) {
      return NextResponse.json(
        { error: "Not authenticated" },
        { status: 401 },
      );
    }

    const stockCode = req.nextUrl.searchParams.get("stockCode")?.trim();
    const exchange = req.nextUrl.searchParams.get("exchange") || "NAS";

    if (!stockCode) {
      return NextResponse.json(
        { error: "stockCode is required" },
        { status: 400 },
      );
    }

    const baseUrl = KIS_BASE_URL[creds.environment];
    const trId = KIS_TR_ID_OVERSEAS_PRICE[creds.environment];

    await kisThrottle();
    const res = await fetch(
      `${baseUrl}/uapi/overseas-price/v1/quotations/price?` +
        new URLSearchParams({
          AUTH: "",
          EXCD: exchange,
          SYMB: stockCode,
        }),
      {
        headers: {
          "Content-Type": "application/json; charset=utf-8",
          authorization: `Bearer ${token}`,
          appkey: creds.appkey,
          appsecret: creds.appsecret,
          tr_id: trId,
          custtype: "P",
        },
      },
    );

    const text = await res.text();
    let data: { rt_cd?: string; msg1?: string; output?: Record<string, unknown>; output2?: unknown };
    try {
      data = text ? JSON.parse(text) : {};
    } catch {
      return NextResponse.json(
        { error: "KIS API가 JSON이 아닌 응답을 반환했습니다." },
        { status: 502 },
      );
    }

    if (!res.ok) {
      return NextResponse.json(
        { error: data.msg1 || "해외 현재가 조회에 실패했습니다." },
        { status: res.status >= 400 ? res.status : 502 },
      );
    }

    if (data.rt_cd !== "0") {
      console.error("[kis/overseas-price] KIS error:", data.msg1, "rt_cd:", data.rt_cd);
      return NextResponse.json(
        { error: data.msg1 || "해외 현재가 조회에 실패했습니다." },
        { status: 400 },
      );
    }

    let output: Record<string, unknown> = (data.output as Record<string, unknown>) ?? {};
    if (Object.keys(output).length === 0 && Array.isArray(data.output2) && data.output2[0]) {
      output = data.output2[0] as Record<string, unknown>;
    }
    return NextResponse.json(output);
  } catch (e) {
    console.error("[kis/overseas-price] Unhandled error:", e);
    const message = e instanceof Error ? e.message : "Internal server error";
    return NextResponse.json(
      { error: message },
      { status: 500 },
    );
  }
}
