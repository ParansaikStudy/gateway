import createClient from "openapi-fetch";
import type { paths } from "@/types/api";

// 8000은 Gateway 포트가 아님 → 17000으로 보정
const baseUrl = (() => {
  const raw = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:17000";
  if (raw.includes("localhost:8000")) return "http://localhost:17000";
  return raw;
})();

export const api = createClient<paths>({ baseUrl });
