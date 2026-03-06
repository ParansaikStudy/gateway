import { NextRequest } from "next/server";
import {
  KIS_BASE_URL,
  KIS_WS_URL,
  KIS_WS_TR_ID_REALTIME_PRICE,
  KIS_WS_TR_ID_OVERSEAS_CNT,
} from "@/lib/kis";
import { getCredentialsFromRequest } from "@/lib/credentials";

export const dynamic = "force-dynamic";
export const runtime = "nodejs";

/** 국내 H0STCNT0 실시간 체결가 컬럼 순서 (^ 구분) */
const H0STCNT0_COLUMNS = [
  "MKSC_SHRN_ISCD",
  "STCK_CNTG_HOUR",
  "STCK_PRPR",
  "PRDY_VRSS_SIGN",
  "PRDY_VRSS",
  "PRDY_CTRT",
  "WGHN_AVRG_STCK_PRC",
  "STCK_OPRC",
  "STCK_HGPR",
  "STCK_LWPR",
  "ASKP1",
  "BIDP1",
  "CNTG_VOL",
  "ACML_VOL",
];
const IDX = (arr: string[], name: string) => arr.indexOf(name);

/** 해외 HDFSCNT0 실시간지연체결가 컬럼 (LAST, SIGN, DIFF, RATE) */
const HDFSCNT0_COLUMNS = [
  "SYMB", "ZDIV", "TYMD", "XYMD", "XHMS", "KYMD", "KHMS",
  "OPEN", "HIGH", "LOW", "LAST", "SIGN", "DIFF", "RATE",
  "PBID", "PASK", "VBID", "VASK", "EVOL", "TVOL", "TAMT",
  "BIVL", "ASVL", "STRN", "MTYP",
];

/**
 * GET /api/kis/realtime?stockCode=005930&market=KR  (국내)
 * GET /api/kis/realtime?stockCode=NVDA&market=US   (해외, 나스닥)
 * KIS WebSocket 실시간 체결가를 SSE로 스트리밍.
 */
export async function GET(req: NextRequest) {
  const stockCode = req.nextUrl.searchParams.get("stockCode")?.trim();
  const market = (req.nextUrl.searchParams.get("market") || "KR").toUpperCase();

  if (!stockCode) {
    return new Response(
      JSON.stringify({ error: "stockCode 필수입니다." }),
      { status: 400, headers: { "Content-Type": "application/json" } },
    );
  }

  const isOverseas = market === "US";
  const trKey = isOverseas ? `DNAS${stockCode}` : stockCode; // 해외: DNAS+티커(나스닥)
  if (!isOverseas && !/^\d{6}$/.test(stockCode)) {
    return new Response(
      JSON.stringify({ error: "국내 종목은 6자리 종목코드여야 합니다." }),
      { status: 400, headers: { "Content-Type": "application/json" } },
    );
  }

  const creds = getCredentialsFromRequest(req);
  if (!creds) {
    return new Response(JSON.stringify({ error: "Not authenticated" }), {
      status: 401,
      headers: { "Content-Type": "application/json" },
    });
  }

  const baseUrl = KIS_BASE_URL[creds.environment];
  const approvalRes = await fetch(`${baseUrl}/oauth2/Approval`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      grant_type: "client_credentials",
      appkey: creds.appkey,
      secretkey: creds.appsecret,
    }),
  });

  const approvalData = (await approvalRes.json()) as { approval_key?: string; msg1?: string };
  const approval_key = approvalData.approval_key;
  if (!approval_key) {
    return new Response(
      JSON.stringify({
        error: approvalData.msg1 || "WebSocket 접속키 발급 실패",
      }),
      {
        status: 400,
        headers: { "Content-Type": "application/json" },
      },
    );
  }

  const tr_id = isOverseas ? KIS_WS_TR_ID_OVERSEAS_CNT : KIS_WS_TR_ID_REALTIME_PRICE;
  const wsUrl = KIS_WS_URL[creds.environment];
  const encoder = new TextEncoder();

  const stream = new ReadableStream({
    async start(controller) {
      let closed = false;
      const close = () => {
        if (closed) return;
        closed = true;
        try {
          controller.close();
        } catch (_e) {
          // Controller already closed (e.g. client disconnected) — ignore
        }
      };

      const push = (payload: Record<string, unknown>) => {
        if (closed) return;
        try {
          controller.enqueue(encoder.encode(`data: ${JSON.stringify(payload)}\n\n`));
        } catch (_e) {
          // Stream already closed, mark so we don't call controller.close() again
          closed = true;
        }
      };

      try {
        const WebSocket = (await import("ws")).default;
        const ws = new WebSocket(wsUrl);

        ws.on("open", () => {
          ws.send(
            JSON.stringify({
              header: {
                approval_key,
                tr_type: "1",
                custtype: "P",
                "content-type": "utf-8",
              },
              body: { input: { tr_id, tr_key: trKey } },
            }),
          );
        });

        ws.on("message", (data: Buffer | string) => {
          if (closed) return;
          const raw = typeof data === "string" ? data : data.toString("utf8");
          if (raw[0] !== "0" && raw[0] !== "1") return;
          const parts = raw.split("|");
          if (parts.length < 4) return;
          const cols = parts[3].split("^");

          if (isOverseas) {
            const last = cols[IDX(HDFSCNT0_COLUMNS, "LAST")];
            const sign = cols[IDX(HDFSCNT0_COLUMNS, "SIGN")];
            const diff = cols[IDX(HDFSCNT0_COLUMNS, "DIFF")];
            const rate = cols[IDX(HDFSCNT0_COLUMNS, "RATE")];
            if (last != null) {
              push({
                market: "US",
                price: last,
                change: diff ?? "",
                changePercent: rate ?? "",
                sign: sign ?? "",
              });
            }
          } else {
            const stck_prpr = cols[IDX(H0STCNT0_COLUMNS, "STCK_PRPR")];
            const prdy_vrss = cols[IDX(H0STCNT0_COLUMNS, "PRDY_VRSS")];
            const prdy_ctrt = cols[IDX(H0STCNT0_COLUMNS, "PRDY_CTRT")];
            const prdy_vrss_sign = cols[IDX(H0STCNT0_COLUMNS, "PRDY_VRSS_SIGN")];
            if (stck_prpr != null) {
              push({
                market: "KR",
                price: stck_prpr,
                change: prdy_vrss ?? "",
                changePercent: prdy_ctrt ?? "",
                sign: prdy_vrss_sign ?? "",
              });
            }
          }
        });

        ws.on("error", () => close());
        ws.on("close", () => close());
      } catch (e) {
        push({ error: e instanceof Error ? e.message : "WebSocket 연결 실패" });
        close();
      }
    },
  });

  return new Response(stream, {
    headers: {
      "Content-Type": "text/event-stream",
      "Cache-Control": "no-store, no-cache",
      Connection: "keep-alive",
    },
  });
}
