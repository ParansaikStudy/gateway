export interface MarketIndex {
  name: string;
  value: number;
  change: number;
  changePercent: number;
}

export interface Stock {
  rank: number;
  name: string;
  code: string;
  price: number;
  change: number;
  changePercent: number;
  volume: number;
  marketCap: string;
  aiSummary: string;
  aiScore: number; // 0-100
}

export interface HotTheme {
  id: string;
  name: string;
  change: number;
  stocks: string[];
  description: string;
}

export interface StockDetail {
  name: string;
  code: string;
  price: number;
  change: number;
  changePercent: number;
  open: number;
  high: number;
  low: number;
  volume: number;
  marketCap: string;
  per: number;
  pbr: number;
  eps: number;
  dividendYield: number;
  week52High: number;
  week52Low: number;
}

export interface AIAnalysis {
  stockName: string;
  stockCode: string;
  pressure: "상방" | "하방" | "중립";
  pressureScore: number;
  technicalSummary: string;
  maPosition: string;
  historicalUpside: number;
  historicalDownside: number;
  sentiment: "긍정적" | "부정적" | "중립";
  positiveFactors: string[];
  riskFactors: string[];
  resistanceLevel: number;
  supportLevel: number;
}

export const marketIndices: MarketIndex[] = [
  { name: "KOSPI", value: 2687.45, change: 23.18, changePercent: 0.87 },
  { name: "KOSDAQ", value: 872.31, change: -5.42, changePercent: -0.62 },
  { name: "S&P 500", value: 6012.28, change: 44.06, changePercent: 0.74 },
  { name: "NASDAQ", value: 19573.45, change: 162.83, changePercent: 0.84 },
];

export const hotThemes: HotTheme[] = [
  {
    id: "ai-semiconductor",
    name: "AI 반도체",
    change: 4.52,
    stocks: ["삼성전자", "SK하이닉스", "한미반도체"],
    description: "글로벌 AI 투자 확대로 반도체 수요 급증",
  },
  {
    id: "secondary-battery",
    name: "2차전지",
    change: 3.21,
    stocks: ["LG에너지솔루션", "삼성SDI", "에코프로비엠"],
    description: "전기차 시장 성장에 따른 배터리 수요 증가",
  },
  {
    id: "bio-pharma",
    name: "바이오/제약",
    change: 2.87,
    stocks: ["삼성바이오로직스", "셀트리온", "유한양행"],
    description: "신약 개발 파이프라인 호재 및 글로벌 수출 확대",
  },
  {
    id: "robot",
    name: "로봇/자동화",
    change: 2.15,
    stocks: ["레인보우로보틱스", "두산로보틱스", "현대로보틱스"],
    description: "산업용 로봇 수요 증가 및 서비스 로봇 시장 확대",
  },
  {
    id: "defense",
    name: "방산",
    change: 1.94,
    stocks: ["한화에어로스페이스", "LIG넥스원", "한국항공우주"],
    description: "글로벌 지정학적 긴장 고조로 방산 수출 증가",
  },
  {
    id: "nuclear",
    name: "원자력",
    change: 1.73,
    stocks: ["두산에너빌리티", "한전기술", "비에이치아이"],
    description: "소형모듈원전(SMR) 관심 증가 및 원전 정책 전환",
  },
];

export const aiPickStocks: Stock[] = [
  {
    rank: 1,
    name: "삼성전자",
    code: "005930",
    price: 72400,
    change: 1800,
    changePercent: 2.55,
    volume: 18234521,
    marketCap: "432.1조",
    aiSummary: "AI 반도체 HBM 수요 확대, 외국인 순매수 전환",
    aiScore: 92,
  },
  {
    rank: 2,
    name: "SK하이닉스",
    code: "000660",
    price: 178500,
    change: 5500,
    changePercent: 3.18,
    volume: 5423891,
    marketCap: "129.8조",
    aiSummary: "HBM3E 양산 본격화, 엔비디아 공급 확대 수혜",
    aiScore: 89,
  },
  {
    rank: 3,
    name: "현대자동차",
    code: "005380",
    price: 234000,
    change: 3000,
    changePercent: 1.3,
    volume: 1832456,
    marketCap: "50.2조",
    aiSummary: "전기차 라인업 확대 및 미국 공장 가동률 상승",
    aiScore: 85,
  },
  {
    rank: 4,
    name: "NAVER",
    code: "035420",
    price: 218500,
    change: -2500,
    changePercent: -1.13,
    volume: 987654,
    marketCap: "35.8조",
    aiSummary: "AI 검색 전환 비용 증가, 단기 조정 구간",
    aiScore: 72,
  },
  {
    rank: 5,
    name: "카카오",
    code: "035720",
    price: 42650,
    change: 850,
    changePercent: 2.03,
    volume: 3456789,
    marketCap: "18.9조",
    aiSummary: "광고 매출 회복세, AI 서비스 출시 기대감",
    aiScore: 78,
  },
  {
    rank: 6,
    name: "LG에너지솔루션",
    code: "373220",
    price: 368000,
    change: 8000,
    changePercent: 2.22,
    volume: 456123,
    marketCap: "86.2조",
    aiSummary: "북미 전기차 배터리 수주 확대, IRA 보조금 수혜",
    aiScore: 83,
  },
];

export interface SearchableStock {
  name: string;
  code: string;
  market: "KR" | "US";
}

export const searchableStocks: SearchableStock[] = [
  // 한국 주요 종목
  { name: "삼성전자", code: "005930", market: "KR" },
  { name: "SK하이닉스", code: "000660", market: "KR" },
  { name: "현대자동차", code: "005380", market: "KR" },
  { name: "기아", code: "000270", market: "KR" },
  { name: "NAVER", code: "035420", market: "KR" },
  { name: "카카오", code: "035720", market: "KR" },
  { name: "LG에너지솔루션", code: "373220", market: "KR" },
  { name: "삼성바이오로직스", code: "207940", market: "KR" },
  { name: "셀트리온", code: "068270", market: "KR" },
  { name: "삼성SDI", code: "006400", market: "KR" },
  { name: "포스코홀딩스", code: "005490", market: "KR" },
  { name: "현대모비스", code: "012330", market: "KR" },
  { name: "LG화학", code: "051910", market: "KR" },
  { name: "한화에어로스페이스", code: "012450", market: "KR" },
  { name: "두산에너빌리티", code: "034020", market: "KR" },
  { name: "크래프톤", code: "259960", market: "KR" },
  { name: "KB금융", code: "105560", market: "KR" },
  { name: "신한지주", code: "055550", market: "KR" },
  { name: "하이브", code: "352820", market: "KR" },
  { name: "에코프로비엠", code: "247540", market: "KR" },
  // 미국 주요 종목
  { name: "엔비디아", code: "NVDA", market: "US" },
  { name: "애플", code: "AAPL", market: "US" },
  { name: "마이크로소프트", code: "MSFT", market: "US" },
  { name: "아마존", code: "AMZN", market: "US" },
  { name: "알파벳(구글)", code: "GOOGL", market: "US" },
  { name: "메타", code: "META", market: "US" },
  { name: "테슬라", code: "TSLA", market: "US" },
  { name: "브로드컴", code: "AVGO", market: "US" },
  { name: "AMD", code: "AMD", market: "US" },
  { name: "넷플릭스", code: "NFLX", market: "US" },
  { name: "코스트코", code: "COST", market: "US" },
  { name: "일라이릴리", code: "LLY", market: "US" },
  { name: "팔란티어", code: "PLTR", market: "US" },
  { name: "마이크론", code: "MU", market: "US" },
  { name: "ASML", code: "ASML", market: "US" },
];

export const mockStockDetail: StockDetail = {
  name: "삼성전자",
  code: "005930",
  price: 72400,
  change: 1800,
  changePercent: 2.55,
  open: 71000,
  high: 73200,
  low: 70800,
  volume: 18234521,
  marketCap: "432.1조",
  per: 12.8,
  pbr: 1.35,
  eps: 5656,
  dividendYield: 1.94,
  week52High: 88800,
  week52Low: 53000,
};

export interface ChartDataPoint {
  time: string;
  value: number;
}

/** 종목코드 + 기간별 시드로 고정된 의사난수 (같은 종목·기간이면 동일한 mock) */
function seededRandom(seed: number): number {
  const x = Math.sin(seed) * 10000;
  return x - Math.floor(x);
}

/** 기간별 mock 포인트 수 및 일자 스텝 */
const MOCK_PERIOD_CONFIG: Record<string, { count: number; dayStep: number; baseScale: number }> = {
  "1D": { count: 30, dayStep: 1, baseScale: 1 },
  "1W": { count: 30, dayStep: 7, baseScale: 1 },
  "1M": { count: 30, dayStep: 30, baseScale: 1 },
  "3M": { count: 36, dayStep: 30, baseScale: 1 },
  "1Y": { count: 36, dayStep: 30, baseScale: 1 },
};

/**
 * 종목·기간별로 다른 mock 차트 데이터 (1D/1W/1M 등 구분, 종목 변경 시에도 차트 변경)
 */
export function getMockChartData(
  stockCode: string,
  period: "1D" | "1W" | "1M" | "3M" | "1Y",
): ChartDataPoint[] {
  const config = MOCK_PERIOD_CONFIG[period] ?? MOCK_PERIOD_CONFIG["1D"];
  const seedFromCode = stockCode.split("").reduce((acc, c) => acc + c.charCodeAt(0), 0);
  const basePrice = stockCode.length >= 4 ? 50000 + (seedFromCode % 50000) : 72400;
  const data: ChartDataPoint[] = [];
  const d = new Date();
  d.setDate(d.getDate() - config.count * config.dayStep);

  let price = basePrice * (0.85 + seededRandom(seedFromCode + 1) * 0.15);
  let step = 0;

  while (data.length < config.count) {
    const seed = seedFromCode + period.charCodeAt(0) + step * 7;
    const change = (seededRandom(seed) - 0.48) * basePrice * 0.02;
    price = Math.max(price + change, basePrice * 0.5);

    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    data.push({
      time: `${y}-${m}-${day}`,
      value: Math.round(price),
    });
    d.setDate(d.getDate() + config.dayStep);
    step++;
  }
  return data;
}

/** @deprecated 기간·종목 구분 없이 단일 mock. getMockChartData(stockCode, period) 사용 권장 */
export const mockChartData: ChartDataPoint[] = getMockChartData("005930", "1D");

export const mockAIAnalysis: AIAnalysis = {
  stockName: "삼성전자",
  stockCode: "005930",
  pressure: "상방",
  pressureScore: 72,
  technicalSummary:
    "5일, 20일, 60일 이동평균선 모두 정배열 상태. MACD 골든크로스 발생 후 상승 모멘텀 유지. RSI 62로 과매수 구간 진입 전 단계.",
  maPosition: "정배열 (5일 > 20일 > 60일)",
  historicalUpside: 12.5,
  historicalDownside: -4.2,
  sentiment: "긍정적",
  positiveFactors: [
    "HBM3E 양산 본격화로 고부가가치 메모리 매출 비중 확대",
    "외국인 순매수 5거래일 연속 유입",
    "글로벌 AI 인프라 투자 사이클 수혜 기대",
    "52주 최고가 대비 18% 할인된 가격대",
  ],
  riskFactors: [
    "미중 반도체 갈등 심화 시 수출 규제 리스크",
    "메모리 반도체 가격 하락 전환 가능성",
    "환율 변동에 따른 수익성 영향",
  ],
  resistanceLevel: 85000,
  supportLevel: 67000,
};
