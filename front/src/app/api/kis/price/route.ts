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

    const { stockCode } = await req.json();
    if (!stockCode) {
      return NextResponse.json(
        { error: "stockCode is required" },
        { status: 400 },
      );
    }

    const baseUrl = KIS_BASE_URL[creds.environment];

    await kisThrottle();
    const res = await fetch(
      `${baseUrl}/uapi/domestic-stock/v1/quotations/inquire-price?` +
        new URLSearchParams({
          FID_COND_MRKT_DIV_CODE: "J",
          FID_INPUT_ISCD: stockCode,
        }),
      {
        headers: {
          "Content-Type": "application/json; charset=utf-8",
          authorization: `Bearer ${token}`,
          appkey: creds.appkey,
          appsecret: creds.appsecret,
          tr_id: "FHKST01010100",
        },
      },
    );

    const data = await res.json();

    if (!res.ok || data.rt_cd !== "0") {
      return NextResponse.json(
        { error: data.msg1 || "Price request failed" },
        { status: res.ok ? 400 : res.status },
      );
    }

    return NextResponse.json(data.output);
  } catch (e) {
    return NextResponse.json(
      { error: e instanceof Error ? e.message : "Internal server error" },
      { status: 500 },
    );
  }
}
