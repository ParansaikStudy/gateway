import { NextRequest, NextResponse } from "next/server";
import {
  encrypt,
  getCredentialsFromRequest,
  CRED_COOKIE,
  TOKEN_COOKIE,
  COOKIE_OPTIONS,
} from "@/lib/credentials";

// GET — check if credentials exist (never returns actual credentials)
export async function GET(req: NextRequest) {
  const creds = getCredentialsFromRequest(req);
  if (!creds) {
    return NextResponse.json({ connected: false });
  }
  return NextResponse.json({
    connected: true,
    environment: creds.environment,
  });
}

// POST — save credentials to encrypted httpOnly cookie
export async function POST(req: NextRequest) {
  try {
    const { appkey, appsecret, accountNumber, environment } = await req.json();

    if (!appkey || !appsecret || !accountNumber || !environment) {
      return NextResponse.json(
        { error: "모든 필드를 입력해주세요" },
        { status: 400 },
      );
    }

    if (environment !== "practice" && environment !== "production") {
      return NextResponse.json(
        { error: "Invalid environment" },
        { status: 400 },
      );
    }

    const encrypted = encrypt(
      JSON.stringify({ appkey, appsecret, accountNumber, environment }),
    );

    const response = NextResponse.json({ connected: true, environment });

    response.cookies.set(CRED_COOKIE, encrypted, {
      ...COOKIE_OPTIONS,
      maxAge: 30 * 24 * 60 * 60, // 30 days
    });

    return response;
  } catch {
    return NextResponse.json(
      { error: "Failed to save credentials" },
      { status: 500 },
    );
  }
}

// DELETE — clear all KIS cookies
export async function DELETE() {
  const response = NextResponse.json({ connected: false });

  response.cookies.set(CRED_COOKIE, "", {
    ...COOKIE_OPTIONS,
    maxAge: 0,
  });
  response.cookies.set(TOKEN_COOKIE, "", {
    ...COOKIE_OPTIONS,
    maxAge: 0,
  });

  return response;
}
