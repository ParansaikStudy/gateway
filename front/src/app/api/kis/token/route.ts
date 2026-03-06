import { NextRequest, NextResponse } from "next/server";
import { KIS_BASE_URL } from "@/lib/kis";
import { kisThrottle } from "@/lib/kis-throttle";
import {
  encrypt,
  getCredentialsFromRequest,
  getTokenFromRequest,
  TOKEN_COOKIE,
  COOKIE_OPTIONS,
} from "@/lib/credentials";

export async function POST(req: NextRequest) {
  try {
    const creds = getCredentialsFromRequest(req);
    if (!creds) {
      return NextResponse.json(
        { error: "No credentials configured" },
        { status: 401 },
      );
    }

    // Check for cached token in cookie
    const cachedToken = getTokenFromRequest(req);
    if (cachedToken) {
      return NextResponse.json({ valid: true });
    }

    const baseUrl = KIS_BASE_URL[creds.environment];

    await kisThrottle();
    const res = await fetch(`${baseUrl}/oauth2/tokenP`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        grant_type: "client_credentials",
        appkey: creds.appkey,
        appsecret: creds.appsecret,
      }),
    });

    const data = await res.json();

    if (!res.ok) {
      return NextResponse.json(
        { error: data.msg1 || "Token request failed" },
        { status: res.status },
      );
    }

    // Store token in encrypted httpOnly cookie
    const expiresAt = Date.now() + 23 * 60 * 60 * 1000;
    const tokenData = encrypt(
      JSON.stringify({
        access_token: data.access_token,
        expires_at: expiresAt,
      }),
    );

    const response = NextResponse.json({ valid: true });
    response.cookies.set(TOKEN_COOKIE, tokenData, {
      ...COOKIE_OPTIONS,
      maxAge: 23 * 60 * 60, // 23 hours
    });

    return response;
  } catch (e) {
    return NextResponse.json(
      { error: e instanceof Error ? e.message : "Internal server error" },
      { status: 500 },
    );
  }
}
