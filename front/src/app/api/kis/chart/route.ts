import { NextRequest, NextResponse } from "next/server";
import { KIS_BASE_URL } from "@/lib/kis";
import { kisThrottle } from "@/lib/kis-throttle";
import {
  getCredentialsFromRequest,
  getTokenFromRequest,
} from "@/lib/credentials";

export async function POST(req: NextRequest) {
  try {
    const creds = getCredentialsFromRequest(req);
    const token = getTokenFromRequest(req);

    if (!creds || !token) {
      return NextResponse.json(
        { error: "Not authenticated" },
        { status: 401 },
      );
    }

    let body: unknown;
    try {
      body = await req.json();
    } catch {
      return NextResponse.json(
        { error: "Invalid JSON body" },
        { status: 400 },
      );
    }
    const { stockCode, startDate, endDate, periodDivCode } = body as Record<string, string>;

    if (!stockCode || !startDate || !endDate || !periodDivCode) {
      return NextResponse.json(
        { error: "Missing required fields: stockCode, startDate, endDate, periodDivCode" },
        { status: 400 },
      );
    }

    // 국내주식 API는 6자리 종목코드만 지원
    if (!/^\d{6}$/.test(String(stockCode).trim())) {
      return NextResponse.json(
        { error: "국내주식 종목코드는 6자리 숫자만 지원합니다." },
        { status: 400 },
      );
    }

    const baseUrl = KIS_BASE_URL[creds.environment];
    const url =
      `${baseUrl}/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice?` +
      new URLSearchParams({
        FID_COND_MRKT_DIV_CODE: "J",
        FID_INPUT_ISCD: String(stockCode).trim(),
        FID_INPUT_DATE_1: startDate,
        FID_INPUT_DATE_2: endDate,
        FID_PERIOD_DIV_CODE: periodDivCode,
        FID_ORG_ADJ_PRC: "0",
      });

    await kisThrottle();
    const res = await fetch(url, {
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        authorization: `Bearer ${token}`,
        appkey: creds.appkey,
        appsecret: creds.appsecret,
        tr_id: "FHKST03010100",
      },
    });

    const text = await res.text();
    let data: { rt_cd?: string; msg1?: string; output2?: unknown[] };
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
        { error: data.msg1 || "차트 조회에 실패했습니다." },
        { status: res.status >= 400 ? res.status : 502 },
      );
    }
    if (data.rt_cd !== "0") {
      return NextResponse.json(
        { error: data.msg1 || "차트 조회에 실패했습니다." },
        { status: 400 },
      );
    }

    const list = Array.isArray(data.output2) ? data.output2 : [];
    return NextResponse.json(list);
  } catch (e) {
    const message = e instanceof Error ? e.message : "Internal server error";
    return NextResponse.json(
      { error: message },
      { status: 500 },
    );
  }
}
