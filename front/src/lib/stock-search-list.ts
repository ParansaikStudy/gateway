/**
 * 종목 검색용 이름↔코드 매핑 (실제 시세/차트/분석은 KIS API 사용)
 * 사용자가 "엔비디아", "애플" 등으로 검색해 코드를 찾을 수 있게 함.
 */
export interface SearchableStock {
  name: string;
  code: string;
  market: "KR" | "US";
}

export const STOCK_SEARCH_LIST: SearchableStock[] = [
  // 국내
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
  // 미국
  { name: "엔비디아", code: "NVDA", market: "US" },
  { name: "애플", code: "AAPL", market: "US" },
  { name: "마이크로소프트", code: "MSFT", market: "US" },
  { name: "아마존", code: "AMZN", market: "US" },
  { name: "알파벳", code: "GOOGL", market: "US" },
  { name: "구글", code: "GOOGL", market: "US" },
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

/** 이름 또는 코드로 검색 (최대 10건) */
export function searchStocks(keyword: string): SearchableStock[] {
  if (!keyword.trim()) return [];
  const q = keyword.trim().toLowerCase();
  return STOCK_SEARCH_LIST.filter(
    (s) =>
      s.name.toLowerCase().includes(q) ||
      s.code.toLowerCase().startsWith(q),
  ).slice(0, 10);
}
