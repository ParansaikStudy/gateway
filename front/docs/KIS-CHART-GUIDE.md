# 차트 컴포넌트 + 한투 OpenAPI 연동 가이드

## 실행 방법

```bash
cd stock_front
npm run dev
```

브라우저에서 `http://localhost:3000` 접속

## 주요 페이지

| 경로 | 설명 |
|------|------|
| `/analysis` | 종목 분석 — 검색 후 차트 + AI 분석 표시 |
| `/settings` | 한국투자증권 API 키 설정 |

---

## 사용 흐름

### 1. Mock 데이터로 바로 확인 (API 키 없이)

1. `/analysis` 페이지 이동
2. 검색바에 아무 종목명 입력 후 검색
3. Mock 차트(삼성전자 60거래일 데이터)가 자동 표시됨
4. 기간 버튼(1일, 1주, 1개월, 3개월, 1년) 클릭 가능 (Mock에서는 같은 데이터)

### 2. 시세·차트 데이터 연동 (한투 API 키 필요)

1. [한국투자증권 OpenAPI](https://apiportal.koreainvestment.com/) 에서 API 키 발급
2. `/settings` 페이지 이동
3. **연결 환경** 선택 (모의 환경 / 실전 환경) — 시세·차트 조회만 지원, 매수/매도 주문 없음
4. App Key, App Secret, 계좌번호 입력 → **저장**
5. 상단에 "API 연결됨" 표시 확인
6. `/analysis` 페이지에서 국내 종목 검색 → KIS API로 현재가·차트 데이터 표시 (현재가 15초마다 갱신)

**실시간 시세:** 연결 시 KIS WebSocket 실시간 체결가(H0STCNT0)를 구독하고, 서버에서 SSE로 브라우저에 푸시합니다. 국내 종목 분석 화면에서 현재가가 실시간으로 갱신되며 "실시간" 배지가 표시됩니다.

### 3. API 키 초기화

- `/settings` 페이지에서 **초기화** 버튼 클릭
- localStorage의 모든 인증 정보(키, 토큰)가 삭제됨
- 다시 Mock 데이터 모드로 전환

---

## 구현 구조

```
src/
├── lib/kis.ts                          # KIS API 타입, 상수, 헬퍼
├── hooks/use-kis.ts                    # React Query 훅 4개
├── components/chart/stock-chart.tsx    # lightweight-charts 라인 차트
├── app/
│   ├── api/kis/
│   │   ├── token/route.ts              # 토큰 발급 프록시
│   │   ├── price/route.ts              # 현재가 조회 프록시
│   │   └── chart/route.ts              # 차트 데이터 프록시
│   ├── analysis/page.tsx               # 종목 분석 (차트 통합)
│   └── settings/page.tsx               # API 설정 페이지
└── mocks/stocks.ts                     # Mock 차트 데이터 추가
```

### 데이터 흐름

```
브라우저 (localStorage에 API 키 저장)
  │
  ├─ API 키 없음 → mockChartData 사용
  │
  └─ API 키 있음
       │
       ├─ useKisToken → POST /api/kis/token → 한투 토큰 발급
       ├─ useKisPrice → POST /api/kis/price → 현재가 조회
       └─ useKisChart → POST /api/kis/chart → 일봉/주봉/월봉 데이터
                              │
                     Next.js API Route (CORS 프록시)
                              │
                     한국투자증권 OpenAPI 서버
```

### React Query 훅 상세

| 훅 | 역할 | 갱신 주기 |
|----|------|-----------|
| `useKisCredentials()` | localStorage 읽기/저장/삭제 | 수동 |
| `useKisToken(credentials)` | 토큰 발급 + 캐싱 | staleTime 23시간 |
| `useKisPrice(code, creds, token)` | 현재가 조회 | 30초 stale, 60초 자동 갱신 |
| `useKisChart(code, period, creds, token)` | 차트 데이터 | staleTime 5분 |

### 차트 기간 옵션

| 버튼 | API 구분코드 | 조회 범위 |
|------|-------------|-----------|
| 1일 | D (일봉) | 최근 30거래일 |
| 1주 | W (주봉) | 최근 30주 |
| 1개월 | M (월봉) | 최근 6개월 |
| 3개월 | M (월봉) | 최근 12개월 |
| 1년 | M (월봉) | 최근 3년 |

### 보안

- API 키는 **브라우저 localStorage**에만 저장
- 서버에 영구 저장되지 않음 (프록시 역할만 수행)
- 공용 PC 사용 시 `/settings`에서 반드시 초기화

---

## 기술 스택

| 라이브러리 | 용도 |
|-----------|------|
| `lightweight-charts` | TradingView 오픈소스 차트 렌더링 |
| `@tanstack/react-query` | 서버 상태 관리 + 자동 갱신 |
| `react-hook-form` + `zod` | 설정 폼 유효성 검증 |
| Next.js API Routes | KIS OpenAPI CORS 프록시 |
