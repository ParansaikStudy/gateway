// KIS (Korea Investment & Securities) OpenAPI types, constants, and helpers

export interface KisCredentials {
  appkey: string;
  appsecret: string;
  accountNumber: string;
  environment: "practice" | "production";
}

export interface KisTokenResponse {
  access_token: string;
  token_type: string;
  expires_in: number;
}

/** KIS 국내주식 현재가 조회(FHKST01010100) output */
export interface KisCurrentPrice {
  stck_prpr: string; // 현재가
  prdy_vrss: string; // 전일 대비
  prdy_vrss_sign: string; // 전일 대비 부호
  prdy_ctrt: string; // 전일 대비율
  stck_oprc: string; // 시가
  stck_hgpr: string; // 고가
  stck_lwpr: string; // 저가
  acml_vol: string; // 누적 거래량
  acml_tr_pbmn: string; // 누적 거래대금
  hts_avls: string; // 시가총액
  per: string;
  pbr: string;
  eps: string;
  stck_mxpr: string; // 상한가
  stck_llam: string; // 하한가
  /** 종목 한글명 (API 응답에 있으면 사용) */
  stck_kor_isnm?: string;
}

export interface KisDailyPrice {
  stck_bsop_date: string; // 영업일자
  stck_clpr: string; // 종가
  stck_oprc: string; // 시가
  stck_hgpr: string; // 고가
  stck_lwpr: string; // 저가
  acml_vol: string; // 누적 거래량
  prdy_vrss: string; // 전일 대비
  prdy_vrss_sign: string; // 전일 대비 부호
}

export interface ChartDataPoint {
  time: string; // "YYYY-MM-DD"
  value: number;
}

export type ChartPeriod = "1D" | "1W" | "1M" | "3M" | "1Y";

export const CHART_PERIODS: {
  key: ChartPeriod;
  label: string;
  fid_period_div_code: string;
}[] = [
  { key: "1D", label: "1일", fid_period_div_code: "D" },
  { key: "1W", label: "1주", fid_period_div_code: "W" },
  { key: "1M", label: "1개월", fid_period_div_code: "M" },
  { key: "3M", label: "3개월", fid_period_div_code: "M" },
  { key: "1Y", label: "1년", fid_period_div_code: "M" },
];

export const KIS_BASE_URL = {
  practice: "https://openapivts.koreainvestment.com:29443",
  production: "https://openapi.koreainvestment.com:9443",
} as const;

/** WebSocket 실시간 시세 (국내주식 실시간 체결가 H0STCNT0) */
export const KIS_WS_URL = {
  practice: "ws://ops.koreainvestment.com:31000",
  production: "ws://ops.koreainvestment.com:21000",
} as const;

/** 국내주식 실시간 체결가 TR ID (KRX) */
export const KIS_WS_TR_ID_REALTIME_PRICE = "H0STCNT0";

/** 해외주식 실시간지연체결가 TR ID (미국 0분지연=실시간) */
export const KIS_WS_TR_ID_OVERSEAS_CNT = "HDFSCNT0";

/** 해외주식 현재가 조회 tr_id (공식 샘플: HHDFS00000300, 모의/실전 공통) */
export const KIS_TR_ID_OVERSEAS_PRICE = {
  practice: "HHDFS00000300",
  production: "HHDFS00000300",
} as const;

/** Get today's date as YYYYMMDD */
export function getTodayDate(): string {
  const d = new Date();
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${y}${m}${day}`;
}

/** Get start date for a chart period as YYYYMMDD */
export function getStartDate(period: ChartPeriod): string {
  const d = new Date();
  switch (period) {
    case "1D":
      d.setDate(d.getDate() - 30); // 일봉 30개
      break;
    case "1W":
      d.setDate(d.getDate() - 7 * 30); // 주봉 30주
      break;
    case "1M":
      d.setMonth(d.getMonth() - 6); // 월봉 6개월
      break;
    case "3M":
      d.setMonth(d.getMonth() - 12); // 월봉 12개월
      break;
    case "1Y":
      d.setFullYear(d.getFullYear() - 3); // 월봉 3년
      break;
  }
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${y}${m}${day}`;
}

/** Parse KIS price sign code to +/- multiplier */
export function parseSign(sign: string): number {
  // 1: 상한, 2: 상승, 3: 보합, 4: 하한, 5: 하락
  return sign === "4" || sign === "5" ? -1 : 1;
}

/** 화면 표시용 종목 상세 (KIS 현재가 + 실시간 시세로 구성, 필수 외 선택) */
export interface KisStockDetailDisplay {
  name: string;
  code: string;
  price: number;
  change: number;
  changePercent: number;
  base?: number;        // 전일종가
  open?: number;
  high?: number;
  low?: number;
  volume?: number;
  prevVolume?: number;  // 전일거래량
  marketCap?: string;
  per?: number;
  pbr?: number;
  eps?: number;
  dividendYield?: number;
  week52High?: number;
  week52Low?: number;
}

/** KIS 현재가 → 표시용 객체 변환 */
export function kisCurrentPriceToDisplay(
  raw: KisCurrentPrice,
  code: string,
): KisStockDetailDisplay {
  const price = Number(raw.stck_prpr) || 0;
  const change = Number(raw.prdy_vrss) || 0;
  const changePercent = Number(raw.prdy_ctrt) || 0;
  const open = Number(raw.stck_oprc) || undefined;
  const high = Number(raw.stck_hgpr) || undefined;
  const low = Number(raw.stck_lwpr) || undefined;
  const volume = Number(raw.acml_vol) || undefined;
  const per = raw.per ? Number(raw.per) : undefined;
  const pbr = raw.pbr ? Number(raw.pbr) : undefined;
  const eps = raw.eps ? Number(raw.eps) : undefined;
  const name = raw.stck_kor_isnm?.trim() || code;
  let marketCap: string | undefined;
  if (raw.hts_avls) {
    const n = Number(raw.hts_avls);
    if (!Number.isNaN(n) && n >= 1e12) marketCap = `${(n / 1e12).toFixed(1)}조`;
    else if (!Number.isNaN(n) && n >= 1e8) marketCap = `${(n / 1e8).toFixed(0)}억`;
  }
  return {
    name,
    code,
    price,
    change,
    changePercent,
    open,
    high,
    low,
    volume,
    marketCap,
    per,
    pbr,
    eps,
  };
}

/** 해외 현재가 API output → 표시용 (다양한 필드명 지원, 콤마 제거 파싱) */
function parseNum(v: unknown): number | undefined {
  if (v == null) return undefined;
  const n = Number(String(v).replace(/,/g, ""));
  return Number.isNaN(n) ? undefined : n;
}

/** 0이 아닌 숫자만 반환 (가격 필드에서 0은 미제공과 동일) */
function parseNonZero(v: unknown): number | undefined {
  const n = parseNum(v);
  return n != null && n !== 0 ? n : undefined;
}

export function overseasPriceToDisplay(
  raw: Record<string, unknown>,
  code: string,
  name: string,
): KisStockDetailDisplay {
  const price =
    parseNum(raw.last ?? raw.LAST ?? raw.last_price ?? raw.stck_prpr ?? raw.cur_prpr ?? raw.clos_price ?? raw.CLOS) ?? 0;

  // KIS sign 필드: 1=상한 2=상승 3=보합 4=하한 5=하락
  // diff/rate는 절대값일 수 있으므로, sign으로 부호 결정
  const signRaw = String(raw.sign ?? raw.SIGN ?? "").trim();
  const isNeg = signRaw === "4" || signRaw === "5";
  const rawDiff = Math.abs(parseNum(raw.diff ?? raw.DIFF ?? raw.prdy_vrss) ?? 0);
  const rawRate = Math.abs(parseNum(raw.rate ?? raw.RATE ?? raw.prdy_ctrt) ?? 0);
  const change = isNeg ? -rawDiff : rawDiff;
  const changePercent = isNeg ? -rawRate : rawRate;

  // open/high/low: KIS 해외 현재가는 open, high, low 필드 사용. 0이면 미제공 처리
  const open = parseNonZero(raw.open ?? raw.OPEN ?? raw.stck_oprc ?? raw.oprc ?? raw.open_prc ?? raw.open_price);
  const high = parseNonZero(raw.high ?? raw.HIGH ?? raw.stck_hgpr ?? raw.hgpr ?? raw.high_prc ?? raw.high_price);
  const low = parseNonZero(raw.low ?? raw.LOW ?? raw.stck_lwpr ?? raw.lwpr ?? raw.low_prc ?? raw.low_price);
  // 거래량: KIS 해외 현재가는 tvol 필드
  const volume = parseNonZero(raw.tvol ?? raw.TVOL ?? raw.acml_vol ?? raw.EVOL ?? raw.evol ?? raw.volume);
  // 시가총액: KIS 해외 현재가는 emkp (시가총액) 필드 — KIS는 억 단위 원화 반환할 수 있음
  const marketCapRaw = raw.emkp ?? raw.market_cap ?? raw.mcap ?? raw.hts_avls ?? raw.cap ?? raw.cap_val ?? raw.market_value ?? raw.MKTCAP ?? raw.tomv;
  let marketCap: string | undefined;
  const nCap = parseNum(marketCapRaw);
  if (nCap != null && nCap > 0) {
    if (nCap >= 1e12) marketCap = `$${(nCap / 1e12).toFixed(1)}T`;
    else if (nCap >= 1e9) marketCap = `$${(nCap / 1e9).toFixed(1)}B`;
    else if (nCap >= 1e6) marketCap = `$${(nCap / 1e6).toFixed(1)}M`;
    else marketCap = `$${nCap.toLocaleString()}`;
  }
  // PER/PBR/EPS: KIS 해외 현재가는 perx, pbrx, epsx 필드
  const per = parseNonZero(raw.perx ?? raw.per ?? raw.PER);
  const pbr = parseNonZero(raw.pbrx ?? raw.pbr ?? raw.PBR);
  const eps = parseNonZero(raw.epsx ?? raw.eps ?? raw.EPS);
  const dividendYield = parseNonZero(raw.dividend_yield ?? raw.d_rate ?? raw.dyld);
  // 52주 고/저: KIS 해외 현재가는 h52p, l52p 필드
  const week52High = parseNonZero(raw.h52p ?? raw.week52_high ?? raw.high_52w ?? raw.H52P);
  const week52Low = parseNonZero(raw.l52p ?? raw.week52_low ?? raw.low_52w ?? raw.L52P);
  // 전일종가, 전일거래량: 모의투자에서 유일하게 제공되는 추가 정보
  const base = parseNonZero(raw.base ?? raw.BASE ?? raw.base_price);
  const prevVolume = parseNonZero(raw.pvol ?? raw.PVOL ?? raw.prev_vol);

  return {
    name: name || code,
    code,
    price,
    change,
    changePercent,
    base,
    open,
    high,
    low,
    volume,
    prevVolume,
    marketCap,
    per,
    pbr,
    eps,
    dividendYield,
    week52High,
    week52Low,
  };
}

/** KIS 분석 API 응답 (백엔드 POST /api/kis/analysis) */
export interface KisAnalysisResponse {
  /** 50자 이하 요약 */
  summary: string;
  /** 전체 분석 문장 (수치 나열) */
  fullAnalysis: string;
  /** 사용자용 결론: 압력·상황·매도 손실/매수 반등 (있으면 상단 강조 표시) */
  conclusion?: string | null;
}
