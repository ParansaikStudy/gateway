import { NextRequest, NextResponse } from "next/server";
import { KIS_BASE_URL, KIS_TR_ID_OVERSEAS_PRICE } from "@/lib/kis";
import { kisThrottle } from "@/lib/kis-throttle";
import {
  getCredentialsFromRequest,
  getTokenFromRequest,
} from "@/lib/credentials";

/**
 * 선택한 종목(해외) 기준 한국투자증권 실시간 환율 조회.
 * GET /api/kis/exchange-rate?stockCode=NVDA&market=US
 * - market=US: 해외주식 현재가 API로 해당 종목 시세 조회 시 반환되는 적용환율 사용
 * - market=KR: 국내 종목은 환율 미제공 (원화 기준)
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
    const market = (req.nextUrl.searchParams.get("market") || "KR").toUpperCase();

    if (market !== "US") {
      return NextResponse.json(
        {
          rate: null,
          change: null,
          changePercent: null,
          source: "한국투자증권",
          message: "국내 종목은 원화 기준입니다.",
        },
        { status: 200 },
      );
    }

    const symbol = stockCode || "NVDA";
    const baseUrl = KIS_BASE_URL[creds.environment];
    const trId = KIS_TR_ID_OVERSEAS_PRICE[creds.environment];

    await kisThrottle();
    const res = await fetch(
      `${baseUrl}/uapi/overseas-price/v1/quotations/price?` +
        new URLSearchParams({
          AUTH: "",
          EXCD: "NAS",
          SYMB: symbol,
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
    let data: {
      rt_cd?: string;
      msg1?: string;
      output?: Record<string, unknown>;
    };
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
        { error: data.msg1 || "환율 조회에 실패했습니다." },
        { status: res.status >= 400 ? res.status : 502 },
      );
    }

    if (data.rt_cd !== "0") {
      return NextResponse.json(
        { error: data.msg1 || "환율 조회에 실패했습니다." },
        { status: 400 },
      );
    }

    const output = data.output as Record<string, unknown> | undefined;
    if (!output) {
      return NextResponse.json(
        { error: "환율 정보가 없습니다." },
        { status: 502 },
      );
    }

    // KIS 응답 필드명: 적용환율(xprc), 전일대비 등 (문서에 따라 xprc, wghn_avrg_stck_prc 등)
    const rateRaw =
      (output.xprc as string) ??
      (output.wghn_avrg_stck_prc as string) ??
      (output.exchange_rate as string) ??
      (output.frcr_rsvl_rate as string);
    const rate = rateRaw != null ? Number(String(rateRaw).replace(/,/g, "")) : null;

    if (rate == null || Number.isNaN(rate) || rate <= 0) {
      return NextResponse.json(
        {
          rate: null,
          change: null,
          changePercent: null,
          source: "한국투자증권",
          message: "적용환율을 확인할 수 없습니다. KIS API 문서를 확인해 주세요.",
        },
        { status: 200 },
      );
    }

    const changeRaw = (output.prdy_vrss as string) ?? (output.diff as string);
    const changePercentRaw = (output.prdy_ctrt as string) ?? (output.rate as string);
    const change = changeRaw != null ? Number(String(changeRaw).replace(/,/g, "")) : null;
    const changePercent = changePercentRaw != null ? Number(String(changePercentRaw).replace(/,/g, "")) : null;

    return NextResponse.json({
      rate: Math.round(rate * 10) / 10,
      change: change != null && !Number.isNaN(change) ? Math.round(change * 10) / 10 : null,
      changePercent: changePercent != null && !Number.isNaN(changePercent) ? Math.round(changePercent * 100) / 100 : null,
      source: "한국투자증권",
      realtime: true,
    });
  } catch (e) {
    const message = e instanceof Error ? e.message : "Internal server error";
    return NextResponse.json(
      { error: message },
      { status: 500 },
    );
  }
}
