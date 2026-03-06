import crypto from "crypto";

const CRED_COOKIE = "kis-cred";
const TOKEN_COOKIE = "kis-tkn";

function getKey(): Buffer {
  return crypto
    .createHash("sha256")
    .update(process.env.ENCRYPTION_KEY || "stockflow-dev-encryption-key-2024")
    .digest();
}

export function encrypt(text: string): string {
  const iv = crypto.randomBytes(16);
  const cipher = crypto.createCipheriv("aes-256-gcm", getKey(), iv);
  let encrypted = cipher.update(text, "utf8", "hex");
  encrypted += cipher.final("hex");
  const authTag = cipher.getAuthTag().toString("hex");
  return `${iv.toString("hex")}:${authTag}:${encrypted}`;
}

export function decrypt(text: string): string {
  const parts = text.split(":");
  if (parts.length !== 3) throw new Error("Invalid encrypted data");
  const [ivHex, tagHex, enc] = parts;
  const decipher = crypto.createDecipheriv(
    "aes-256-gcm",
    getKey(),
    Buffer.from(ivHex, "hex"),
  );
  decipher.setAuthTag(Buffer.from(tagHex, "hex"));
  let decrypted = decipher.update(enc, "hex", "utf8");
  decrypted += decipher.final("utf8");
  return decrypted;
}

export interface StoredCredentials {
  appkey: string;
  appsecret: string;
  accountNumber: string;
  environment: "practice" | "production";
}

export function getCredentialsFromRequest(req: {
  cookies: { get: (name: string) => { value: string } | undefined };
}): StoredCredentials | null {
  const cookie = req.cookies.get(CRED_COOKIE);
  if (!cookie) return null;
  try {
    return JSON.parse(decrypt(cookie.value)) as StoredCredentials;
  } catch {
    return null;
  }
}

export function getTokenFromRequest(req: {
  cookies: { get: (name: string) => { value: string } | undefined };
}): string | null {
  const cookie = req.cookies.get(TOKEN_COOKIE);
  if (!cookie) return null;
  try {
    const data = JSON.parse(decrypt(cookie.value));
    if (data.expires_at && Date.now() > data.expires_at) return null;
    return data.access_token as string;
  } catch {
    return null;
  }
}

const isProduction = process.env.NODE_ENV === "production";

export const COOKIE_OPTIONS = {
  httpOnly: true,
  secure: isProduction,
  sameSite: "strict" as const,
  path: "/",
};

export { CRED_COOKIE, TOKEN_COOKIE };
