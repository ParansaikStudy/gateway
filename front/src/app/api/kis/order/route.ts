import { NextRequest, NextResponse } from "next/server";
import { KIS_BASE_URL } from "@/lib/kis";
import { kisThrottle } from "@/lib/kis-throttle";
import {
  getCredentialsFromRequest,
  getTokenFromRequest,
} from "@/lib/credentials";

/** 계좌번호 "50123456-01" → { CANO: "50123456", ACNT_PRDT_CD: "01" } */
function parseAccountNumber(accountNumber: string): { CANO: string; ACNT_PRDT_CD: string } {
  const trimmed = String(accountNumber).trim().replace(/-/g, "");
  if (trimmed.length < 10) {
    throw new Error("계좌번호는 앞 8자리-뒤 2자리 형식이어야 합니다.");
  }
  return {
    CANO: trimmed.slice(0, 8),
    ACNT_PRDT_CD: trimmed.slice(8, 10),
  };
}

/**
 * 국내주식 현금 매수/매도 주문
 * POST /api/kis/order
 * Body: { side: "buy"|"sell", stockCode: string, orderType: "limit"|"market", quantity: number, price?: number }
 * - orderType "market" 시 시장가(ORD_DVSN "01"), price 생략 가능
 * - orderType "limit" 시 지정가(ORD_DVSN "00"), price 필수
 */
export async function POST(req: NextRequest) {
  try {
    const creds = getCredentialsFromRequest(req);
    const token = getTokenFromRequest(req);

    if (!creds || !token) {
      return NextResponse.json(
        { error: "한국투자증권 API가 연결되지 않았습니다. 설정에서 연결해 주세요." },
        { status: 401 },
      );
    }

    let body: { side?: string; stockCode?: string; orderType?: string; quantity?: number; price?: number };
    try {
      body = await req.json();
    } catch {
      return NextResponse.json(
        { error: "요청 본문이 올바르지 않습니다." },
        { status: 400 },
      );
    }

    const { side, stockCode, orderType, quantity, price } = body;
    if (!side || !stockCode || orderType === undefined || quantity == null) {
      return NextResponse.json(
        { error: "side, stockCode, orderType, quantity는 필수입니다." },
        { status: 400 },
      );
    }

    if (side !== "buy" && side !== "sell") {
      return NextResponse.json(
        { error: "side는 buy 또는 sell이어야 합니다." },
        { status: 400 },
      );
    }

    const pdno = String(stockCode).trim();
    if (!/^\d{6}$/.test(pdno)) {
      return NextResponse.json(
        { error: "국내주식 종목코드는 6자리 숫자여야 합니다." },
        { status: 400 },
      );
    }

    const ordDvsn = orderType === "market" ? "01" : "00"; // 00: 지정가, 01: 시장가
    const ordQty = String(Math.floor(Number(quantity)) || 0);
    if (Number(ordQty) <= 0) {
      return NextResponse.json(
        { error: "주문 수량은 1 이상이어야 합니다." },
        { status: 400 },
      );
    }

    let ordUnpr: string;
    if (orderType === "market") {
      ordUnpr = "0"; // 시장가 시 0 전달 가능 (상한가로 주문 후 체결가로 정산 등)
    } else {
      const p = Number(price);
      if (!Number.isFinite(p) || p <= 0) {
        return NextResponse.json(
          { error: "지정가 주문 시 주문 단가는 0보다 커야 합니다." },
          { status: 400 },
        );
      }
      ordUnpr = String(Math.floor(p));
    }

    const { CANO, ACNT_PRDT_CD } = parseAccountNumber(creds.accountNumber);
    const baseUrl = KIS_BASE_URL[creds.environment];

    const isReal = creds.environment === "production";
    const trId =
      side === "buy"
        ? isReal
          ? "TTTC0012U"
          : "VTTC0012U"
        : isReal
          ? "TTTC0011U"
          : "VTTC0011U";

    const params: Record<string, string> = {
      CANO,
      ACNT_PRDT_CD,
      PDNO: pdno,
      ORD_DVSN: ordDvsn,
      ORD_QTY: ordQty,
      ORD_UNPR: ordUnpr,
      EXCG_ID_DVSN_CD: "KRX",
    };
    if (side === "sell") {
      params.SLL_TYPE = "01"; // 01: 일반매도
    }

    await kisThrottle();
    const res = await fetch(`${baseUrl}/uapi/domestic-stock/v1/trading/order-cash`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json; charset=utf-8",
        authorization: `Bearer ${token}`,
        appkey: creds.appkey,
        appsecret: creds.appsecret,
        tr_id: trId,
      },
      body: JSON.stringify(params),
    });

    const text = await res.text();
    let data: { rt_cd?: string; msg_cd?: string; msg1?: string; output?: Record<string, unknown> };
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
        { error: data.msg1 || "주문 요청에 실패했습니다." },
        { status: res.status >= 400 ? res.status : 502 },
      );
    }

    if (data.rt_cd !== "0") {
      return NextResponse.json(
        { error: data.msg1 || "주문이 거부되었습니다." },
        { status: 400 },
      );
    }

    return NextResponse.json({
      success: true,
      message: data.msg1 ?? "주문이 전송되었습니다.",
      output: data.output ?? {},
    });
  } catch (e) {
    const message = e instanceof Error ? e.message : "Internal server error";
    return NextResponse.json(
      { error: message },
      { status: 500 },
    );
  }
}
