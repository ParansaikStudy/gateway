# 주식 투자 커뮤니티 플랫폼 설계서

> 작성일: 2026-02-22
> 상태: 설계 완료 / 회의 후 구현 예정
> 
1. 데이터 분석 생태계

Python은 데이터 분석에 특화된 라이브러리가 압도적으로 많습니다.

┌──────────────────┬──────────────────────────┬───────────────────────────────┐
│       영역       │          Python          │             Java              │
├──────────────────┼──────────────────────────┼───────────────────────────────┤
│ 데이터 처리      │ pandas, numpy            │ 직접 구현 or Apache Commons   │
├──────────────────┼──────────────────────────┼───────────────────────────────┤
│ 시각화           │ matplotlib, plotly       │ JFreeChart (제한적)           │
├──────────────────┼──────────────────────────┼───────────────────────────────┤
│ 금융 데이터 수집 │ yfinance, pykrx          │ 별도 API 클라이언트 직접 구현 │
├──────────────────┼──────────────────────────┼───────────────────────────────┤
│ 머신러닝         │ scikit-learn, tensorflow │ DL4J (커뮤니티 작음)          │
├──────────────────┼──────────────────────────┼───────────────────────────────┤
│ 통계 분석        │ scipy, statsmodels       │ Apache Commons Math           │
└──────────────────┴──────────────────────────┴───────────────────────────────┘

2. 개발 속도 & 생산성

- Python은 같은 분석 로직을 Java 대비 1/3 ~ 1/5 코드량으로 작성 가능
- 예: 주식 데이터 가져와서 이동평균 계산하는 작업
    - Python: pandas로 3~5줄
    - Java: DTO 정의, 파싱, 계산 로직 등 수십 줄

3. 분석 단위 처리 속도

"한 가지 분석에 대해 시간이 빠르다"는 이유:

- numpy/pandas의 내부는 C/Fortran으로 구현되어 있어서 벡터 연산이 매우 빠름
- Java는 반복문(for-loop) 기반으로 처리해야 하는 경우가 많음
- 특히 행렬 연산, 통계 계산, 시계열 분석 같은 수치 연산에서 Python(numpy)이 유리

4. 인사이트가 많다는 의미

- 금융/퀀트 분야의 레퍼런스, 논문, 오픈소스 전략이 대부분 Python으로 작성됨
- Stack Overflow, GitHub 등에서 주식 분석 관련 자료가 Python 중심
- 검증된 분석 기법을 바로 가져다 쓸 수 있음 (Java는 직접 포팅해야 함)

5. 현재 프로젝트 아키텍처 관점

현재 게이트웨이 프로젝트는 Java/Spring 기반이므로, 구조적으로는:

[Spring Gateway (Java)] → API 호출 → [Python 분석 서비스] → 결과 반환

- Java: API 서버, 인증, 라우팅 등 서비스 인프라 담당
- Python: 주식 데이터 수집 + 분석 엔진 담당

이렇게 역할을 분리하면 각 언어의 강점을 살릴 수 있습니다.


핵심 기능 (예시)

┌─────────────┬────────────────┬──────────────────────────────────────────────┐
│    구분     │      기능      │                     설명                     │
├─────────────┼────────────────┼──────────────────────────────────────────────┤
│ 데이터 수집 │ 종목 시세 조회 │ 실시간/일별 주가 데이터 수집                 │
├─────────────┼────────────────┼──────────────────────────────────────────────┤
│ 데이터 수집 │ 재무제표 조회  │ 기업 재무 정보 수집                          │
├─────────────┼────────────────┼──────────────────────────────────────────────┤
│ 분석        │ 기술적 분석    │ 이동평균, RSI, MACD 등 보조지표 계산         │
├─────────────┼────────────────┼──────────────────────────────────────────────┤
│ 분석        │ 종목 비교      │ 여러 종목 간 수익률/지표 비교                │
├─────────────┼────────────────┼──────────────────────────────────────────────┤
│ 제공        │ 분석 결과 API  │ Spring Gateway를 통해 프론트엔드에 결과 전달 │
└─────────────┴────────────────┴──────────────────────────────────────────────┘


## Context

기존 치과 관리 시스템(Spring Boot 4.0 마이크로서비스)의 인프라를 재활용하여 **AI 투자 분석 보고서가 포함된 주식 투자 커뮤니티 플랫폼**의 백엔드를 구축한다. 프론트엔드는 별도 프로젝트로 진행하며, 기술적 분석은 Python 마이크로서비스로 분리한다.

---

## 1. 전체 시스템 아키텍처

```
                          Client (Web/App) - 별도 프로젝트
                                 │
                                 v
┌────────────────────────────────────────────────────────────────┐
│                   API Gateway (Port 7003)                      │
│                 Spring Cloud Gateway (WebFlux)                 │
│                                                                │
│  /auth/**          -> lb://ZQKSK-AUTH-SERVICE                  │
│  /stock-api/**     -> lb://ZQKSK-STOCK-API-SERVICE    (NEW)   │
│  /community-api/** -> lb://ZQKSK-COMMUNITY-API-SERVICE (NEW)  │
└──────┬──────────────┬───────────────────┬──────────────────────┘
       │              │                   │
       v              v                   v
┌──────────┐  ┌──────────────┐  ┌─────────────────┐
│Auth (7002)│  │Stock API     │  │Community API    │
│ JWT/유저   │  │(7004)        │  │(7005)           │
│ 인증/권한  │  │ 종목 CRUD    │  │ 게시글/댓글     │
│           │  │ 가격/지표    │  │ 좋아요/팔로우   │
│           │  │ AI보고서     │  │                 │
│           │  │ 포트폴리오   │  │                 │
└─────┬─────┘  └──────┬───────┘  └────────┬────────┘
      │               │                   │
      └───────────────┼───────────────────┘
                      │ JPA
                      v
              ┌──────────────┐     ┌─────────────────────┐
              │  MariaDB     │     │ Python Analysis     │
              │  (:3310)     │     │ Service (Port 8000) │
              └──────────────┘     │ FastAPI + pandas-ta │
                                   │ + Claude API        │
                                   └─────────────────────┘
                                            ^
                                            │ HTTP (WebClient)
                                   Stock API Service ┘
```

### 서비스 목록

| 서비스 | 포트 | 기술스택 | 역할 | 상태 |
|--------|------|----------|------|------|
| Discovery | 7001 | Spring Cloud Eureka | 서비스 디스커버리 | 기존 유지 |
| Auth | 7002 | Spring Boot + JWT | 인증/권한/사용자 관리 | 기존 유지 |
| Gateway | 7003 | Spring Cloud Gateway (WebFlux) | API 라우팅, 인증 필터 | 라우팅 수정 |
| Stock API | 7004 | Spring Boot | 종목/가격/지표/분석/포트폴리오 | **신규** |
| Community API | 7005 | Spring Boot | 게시글/댓글/좋아요/팔로우 | **신규** |
| Analysis | 8000 | Python FastAPI | 기술적 분석, AI 보고서 생성 | **신규** |
| MariaDB | 3310 | MariaDB 11 | 데이터 저장소 | 기존 유지 |

---

## 2. Java vs Python 역할 분담

| 영역 | Java (Spring Boot) | Python (FastAPI) | 결정 |
|------|-------------------|------------------|------|
| 웹 API, CRUD, 인증 | Spring Boot | - | **Java** |
| 기술적 지표 계산 (RSI, 볼린저, MA) | ta4j 가능 | pandas-ta (더 편리) | **Python** |
| AI 보고서 생성 (Claude API 호출) | 가능 | SDK 지원 우수 | **Python** |
| 뉴스 감성분석 | 복잡 | 생태계 풍부 | **Python** |
| 주식 데이터 수집 | 가능 | yfinance 등 편리 | **Python** |
| 커뮤니티 기능 | Spring Boot | - | **Java** |
| 포트폴리오 관리 | Spring Boot | - | **Java** |

**결론**: Java(Spring Boot)로 플랫폼 핵심(API, 인증, CRUD), Python(FastAPI)으로 분석 엔진. Python 서비스는 Eureka에 등록하지 않고 고정 URL로 호출.

---

## 3. 기존 코드 재활용/신규/제거 구분

### 재활용 (그대로 유지)
- `api/gateway` - 라우팅 설정만 수정
- `api/discovery` - 변경 없음
- `api/auth` - 변경 없음 (JWT, 쿠키 인증)
- `domain/user` - 사용자 관리
- `domain/log` - 로그 관리
- `storage/database` - DB 설정, BaseEntity, JPA/QueryDSL 인프라
- `support/*` - 예외처리, ApiResponse, 로깅, 메일 전부 유지

### 신규 생성
- `domain/stock` - 종목, 가격, 기술적 지표
- `domain/community` - 게시글, 댓글, 좋아요, 팔로우
- `domain/analysis` - AI 분석 보고서
- `domain/portfolio` - 관심종목, 보유종목
- `api/stock-service` - 종목/분석/포트폴리오 API 서버
- `api/community-service` - 커뮤니티 API 서버
- `tools/analysis-service` - Python 분석 엔진

### 제거 (마지막 Phase에서)
- `domain/dentistry`, `domain/pc`, `domain/customer`, `domain/competitor`, `domain/notices`
- 해당 storage 엔티티/레포지토리
- `domain/common`의 치과 관련 enum

---

## 4. 신규 도메인 모듈 설계

> 기존 프로젝트 패턴 준수
> - `*Finder`(조회), `*Appender`(생성), `*Modifier`(수정)
> - `*Repository`(인터페이스 - 도메인 레이어)
> - `*Service`(인터페이스, default throw UnsupportedOperationException)
> - `New*`(생성 DTO), Java record 사용

### 4.1 `domain/stock` - 종목/가격/지표

```
domain/stock/src/main/java/com/zqksk/api/domain/stock/
├── Stock.java                    (record: id, symbol, name, exchange, sector, marketCap, listingDate...)
├── NewStock.java                 (record: creation DTO)
├── StockPrice.java               (record: id, stockId, symbol, tradeDate, open, high, low, close, volume, changePercent...)
├── NewStockPrice.java
├── StockQuote.java               (record: 실시간 시세 - currentPrice, changeAmount, changePercent, timestamp)
├── TechnicalIndicator.java       (record: ma5/20/60/120, rsi14, bollingerUpper/Middle/Lower, 52wHigh/Low, support, resistance)
├── StockSearchRequest.java
├── StockSearchRequestWithPage.java
├── StockRepository.java          (interface)
├── StockPriceRepository.java     (interface)
├── TechnicalIndicatorRepository.java (interface)
├── StockFinder.java              (@Component)
├── StockAppender.java            (@Component)
├── StockPriceFinder.java         (@Component)
├── StockPriceAppender.java       (@Component)
├── TechnicalIndicatorFinder.java (@Component)
├── TechnicalIndicatorAppender.java (@Component)
└── StockService.java             (interface)
```

**주요 모델 필드:**

```java
// Stock.java
record Stock(Long id, String symbol, String name, String exchange,
             String sector, String industry, Long marketCap,
             LocalDate listingDate, String logoUrl, LocalDateTime lastUpdated)

// StockPrice.java
record StockPrice(Long id, Long stockId, String symbol, LocalDate tradeDate,
                  double open, double high, double low, double close,
                  double previousClose, long volume,
                  double changeAmount, double changePercent)

// TechnicalIndicator.java
record TechnicalIndicator(Long id, Long stockId, String symbol, LocalDate tradeDate,
                          Double ma5, Double ma20, Double ma60, Double ma120,
                          Double rsi14, Double bollingerUpper, Double bollingerMiddle,
                          Double bollingerLower, Double fiftyTwoWeekHigh,
                          Double fiftyTwoWeekLow, Double supportLevel, Double resistanceLevel)
```

### 4.2 `domain/community` - 게시글/댓글/좋아요/팔로우

```
domain/community/src/main/java/com/zqksk/api/domain/community/
├── post/
│   ├── Post.java                 (record: id, userId, authorName, title, content, stockSymbol, likeCount, commentCount, viewCount)
│   ├── NewPost.java
│   ├── PostSearchRequestWithPage.java
│   ├── PostRepository.java
│   ├── PostFinder.java, PostAppender.java, PostModifier.java
│   └── PostService.java
├── comment/
│   ├── Comment.java              (record: id, postId, userId, content, parentCommentId, likeCount)
│   ├── NewComment.java
│   ├── CommentRepository.java
│   ├── CommentFinder.java, CommentAppender.java
│   └── CommentService.java
├── like/
│   ├── PostLike.java, CommentLike.java, NewPostLike.java
│   ├── LikeRepository.java
│   └── LikeAppender.java, LikeFinder.java
└── follow/
    ├── Follow.java, NewFollow.java
    ├── FollowRepository.java
    ├── FollowFinder.java, FollowAppender.java
    └── FollowService.java
```

### 4.3 `domain/analysis` - AI 분석 보고서

```
domain/analysis/src/main/java/com/zqksk/api/domain/analysis/
├── AnalysisReport.java           (record: 4페이지 보고서 전체)
├── NewAnalysisReport.java
├── AnalysisRequest.java          (record: symbol, stockName, analysisType)
├── AnalysisReportRepository.java
├── AnalysisReportFinder.java, AnalysisReportAppender.java
└── AnalysisReportService.java
```

**AnalysisReport 필드 구조:**

```java
record AnalysisReport(
    Long id, String symbol, String stockName,

    // Page 1: 투자 분석 보고서
    int investmentScore,           // -100 ~ 100
    String investmentGrade,        // "매수", "중립", "매도"
    String coreRationale,
    double targetPriceShortTerm,   // 단기 (1-3개월)
    double targetPriceMidTerm,     // 중기 (6-12개월)
    double targetPriceLongTerm,    // 장기 (1-3년)
    String investmentSummary,

    // Page 2: 기술적 분석 보고서
    String chartPattern,           // "상승 채널", "이중 바닥" 등
    String trendAnalysis,
    String momentumAnalysis,
    double confidenceRating,       // 0-100
    String technicalSummary,

    // Page 3: 리스크 분석 보고서
    String riskLevel,              // HIGH, MEDIUM, LOW
    String keyRisks,               // JSON array
    String volatilityAnalysis,
    String riskSummary,

    // Page 4: 뉴스 분석 보고서
    String newsHeadlines,          // JSON array
    String newsSentiment,          // POSITIVE, NEUTRAL, NEGATIVE
    String eventCalendar,          // JSON
    String newsSummary,

    // 메타데이터
    String modelVersion,
    LocalDateTime generatedAt,
    LocalDateTime expiresAt        // 캐시 만료 (기본 24시간)
)
```

### 4.4 `domain/portfolio` - 관심종목/보유종목

```
domain/portfolio/src/main/java/com/zqksk/api/domain/portfolio/
├── Watchlist.java                (record: id, userId, symbol, stockName, memo, addedAt)
├── NewWatchlist.java
├── UserHolding.java              (record: id, userId, symbol, averageCost, quantity, purchaseDate, note)
├── NewUserHolding.java
├── WatchlistRepository.java, UserHoldingRepository.java
├── WatchlistFinder.java, WatchlistAppender.java
├── UserHoldingFinder.java, UserHoldingAppender.java
└── PortfolioService.java
```

---

## 5. DB 스키마 설계 (MariaDB)

> MariaDB 유지 이유: 기존 인프라(HikariCP, JPA, QueryDSL, Docker) 전부 MariaDB 기반.
> 미국 주식 약 6,000종목 x 252거래일/년 = 약 150만 행/년 → MariaDB로 충분.

### 5.1 Stock 테이블

```sql
CREATE TABLE stock (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol       VARCHAR(10)  NOT NULL UNIQUE,
    name         VARCHAR(200) NOT NULL,
    exchange     VARCHAR(20)  NOT NULL COMMENT 'NASDAQ, NYSE, AMEX',
    sector       VARCHAR(100),
    industry     VARCHAR(200),
    market_cap   BIGINT,
    listing_date DATE,
    logo_url     VARCHAR(500),
    last_updated DATETIME,
    created_at   DATETIME NOT NULL,
    updated_at   DATETIME,
    INDEX idx_stock_symbol (symbol),
    INDEX idx_stock_name (name),
    INDEX idx_stock_sector (sector)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE stock_price (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_id       BIGINT      NOT NULL,
    symbol         VARCHAR(10) NOT NULL,
    trade_date     DATE        NOT NULL,
    open_price     DOUBLE      NOT NULL,
    high_price     DOUBLE      NOT NULL,
    low_price      DOUBLE      NOT NULL,
    close_price    DOUBLE      NOT NULL,
    previous_close DOUBLE,
    volume         BIGINT      NOT NULL,
    change_amount  DOUBLE GENERATED ALWAYS AS (close_price - previous_close) STORED,
    change_percent DOUBLE GENERATED ALWAYS AS (
        CASE WHEN previous_close > 0
             THEN ((close_price - previous_close) / previous_close) * 100
             ELSE 0 END
    ) STORED,
    created_at     DATETIME NOT NULL,
    updated_at     DATETIME,
    UNIQUE KEY uk_symbol_date (symbol, trade_date),
    INDEX idx_stock_price_symbol (symbol),
    INDEX idx_stock_price_date (trade_date),
    FOREIGN KEY (stock_id) REFERENCES stock(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE technical_indicator (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_id            BIGINT      NOT NULL,
    symbol              VARCHAR(10) NOT NULL,
    trade_date          DATE        NOT NULL,
    ma5                 DOUBLE,
    ma20                DOUBLE,
    ma60                DOUBLE,
    ma120               DOUBLE,
    rsi14               DOUBLE,
    bollinger_upper     DOUBLE,
    bollinger_middle    DOUBLE,
    bollinger_lower     DOUBLE,
    fifty_two_week_high DOUBLE,
    fifty_two_week_low  DOUBLE,
    support_level       DOUBLE,
    resistance_level    DOUBLE,
    created_at          DATETIME NOT NULL,
    updated_at          DATETIME,
    UNIQUE KEY uk_indicator_symbol_date (symbol, trade_date),
    INDEX idx_indicator_symbol (symbol),
    FOREIGN KEY (stock_id) REFERENCES stock(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 5.2 Community 테이블

```sql
CREATE TABLE post (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    title         VARCHAR(300) NOT NULL,
    content       LONGTEXT     NOT NULL,
    stock_symbol  VARCHAR(10)  COMMENT '특정 종목 커뮤니티에 연결',
    like_count    INT          NOT NULL DEFAULT 0,
    comment_count INT          NOT NULL DEFAULT 0,
    view_count    INT          NOT NULL DEFAULT 0,
    delete_yn     VARCHAR(1)   DEFAULT 'N',
    created_at    DATETIME     NOT NULL,
    updated_at    DATETIME,
    INDEX idx_post_user (user_id),
    INDEX idx_post_stock (stock_symbol),
    INDEX idx_post_created (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE comment (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id           BIGINT NOT NULL,
    user_id           BIGINT NOT NULL,
    content           TEXT   NOT NULL,
    parent_comment_id BIGINT COMMENT 'NULL이면 최상위, 값 있으면 대댓글',
    like_count        INT    NOT NULL DEFAULT 0,
    delete_yn         VARCHAR(1) DEFAULT 'N',
    created_at        DATETIME NOT NULL,
    updated_at        DATETIME,
    INDEX idx_comment_post (post_id),
    FOREIGN KEY (post_id) REFERENCES post(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE post_like (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id    BIGINT   NOT NULL,
    user_id    BIGINT   NOT NULL,
    created_at DATETIME NOT NULL,
    UNIQUE KEY uk_post_like (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES post(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE comment_like (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment_id BIGINT   NOT NULL,
    user_id    BIGINT   NOT NULL,
    created_at DATETIME NOT NULL,
    UNIQUE KEY uk_comment_like (comment_id, user_id),
    FOREIGN KEY (comment_id) REFERENCES comment(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE follow (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    follower_id BIGINT   NOT NULL COMMENT '팔로우 하는 사람',
    followee_id BIGINT   NOT NULL COMMENT '팔로우 받는 사람',
    created_at  DATETIME NOT NULL,
    UNIQUE KEY uk_follow (follower_id, followee_id),
    INDEX idx_follow_follower (follower_id),
    INDEX idx_follow_followee (followee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 5.3 Analysis 테이블

```sql
CREATE TABLE analysis_report (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol                  VARCHAR(10)  NOT NULL,
    stock_name              VARCHAR(200),

    -- Page 1: 투자 분석
    investment_score        INT          COMMENT '-100 ~ 100',
    investment_grade        VARCHAR(20)  COMMENT 'STRONG_BUY, BUY, NEUTRAL, SELL, STRONG_SELL',
    core_rationale          TEXT,
    target_price_short_term DOUBLE,
    target_price_mid_term   DOUBLE,
    target_price_long_term  DOUBLE,
    investment_summary      LONGTEXT,

    -- Page 2: 기술적 분석
    chart_pattern           VARCHAR(100),
    trend_analysis          TEXT,
    momentum_analysis       TEXT,
    confidence_rating       DOUBLE,
    technical_summary       LONGTEXT,

    -- Page 3: 리스크 분석
    risk_level              VARCHAR(20),
    key_risks               LONGTEXT     COMMENT 'JSON array',
    volatility_analysis     TEXT,
    risk_summary            LONGTEXT,

    -- Page 4: 뉴스 분석
    news_headlines          LONGTEXT     COMMENT 'JSON array',
    news_sentiment          VARCHAR(20),
    event_calendar          LONGTEXT     COMMENT 'JSON',
    news_summary            LONGTEXT,

    -- 메타데이터
    model_version           VARCHAR(50),
    generated_at            DATETIME     NOT NULL,
    expires_at              DATETIME     NOT NULL,
    created_at              DATETIME     NOT NULL,
    updated_at              DATETIME,
    INDEX idx_report_symbol (symbol),
    INDEX idx_report_generated (generated_at DESC),
    INDEX idx_report_expires (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### 5.4 Portfolio 테이블

```sql
CREATE TABLE watchlist (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT      NOT NULL,
    symbol     VARCHAR(10) NOT NULL,
    memo       VARCHAR(500),
    created_at DATETIME    NOT NULL,
    updated_at DATETIME,
    UNIQUE KEY uk_watchlist (user_id, symbol),
    INDEX idx_watchlist_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_holding (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT      NOT NULL,
    symbol        VARCHAR(10) NOT NULL,
    average_cost  DOUBLE      NOT NULL,
    quantity      INT         NOT NULL,
    purchase_date DATE,
    note          VARCHAR(500),
    created_at    DATETIME    NOT NULL,
    updated_at    DATETIME,
    INDEX idx_holding_user (user_id),
    INDEX idx_holding_symbol (symbol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 6. Python 분석 서비스 상세 설계

### 6.1 프로젝트 구조

```
tools/analysis-service/
├── Dockerfile
├── requirements.txt
│     fastapi==0.115.*
│     uvicorn==0.34.*
│     pandas==2.2.*
│     pandas-ta==0.3.*
│     numpy==2.2.*
│     anthropic==0.52.*
│     httpx==0.28.*
│     pydantic==2.10.*
│     cachetools==5.5.*
│     python-dotenv==1.0.*
├── app/
│   ├── main.py                   (FastAPI app, startup, health check)
│   ├── config.py                 (환경변수: POLYGON_API_KEY, ANTHROPIC_API_KEY)
│   ├── api/
│   │   └── routes.py             (API 엔드포인트 정의)
│   ├── models/
│   │   ├── request.py            (Pydantic: AnalysisRequest)
│   │   └── response.py           (Pydantic: AnalysisReportResponse)
│   ├── services/
│   │   ├── stock_data.py         (Polygon.io에서 OHLCV + 회사정보 수집)
│   │   ├── technical.py          (pandas-ta로 RSI, MACD, 볼린저, MA, 지지/저항선 계산)
│   │   ├── ai_report.py          (Claude API로 4페이지 보고서 생성)
│   │   └── news.py               (뉴스 수집 + 감성분석)
│   └── prompts/
│       ├── investment_analysis.txt  (투자 분석 프롬프트 템플릿)
│       ├── technical_analysis.txt   (기술적 분석 프롬프트 템플릿)
│       ├── risk_analysis.txt        (리스크 분석 프롬프트 템플릿)
│       └── news_analysis.txt        (뉴스 분석 프롬프트 템플릿)
└── tests/
```

### 6.2 API 엔드포인트

| Method | Path | 설명 | 예상 응답시간 |
|--------|------|------|------------|
| `POST` | `/api/v1/analysis/full` | 4페이지 전체 보고서 생성 | 10-30초 |
| `POST` | `/api/v1/analysis/technical` | 기술적 분석만 | 2-5초 |
| `POST` | `/api/v1/analysis/news` | 뉴스 분석만 | 5-15초 |
| `GET` | `/api/v1/indicators/{symbol}` | 기술적 지표 조회 | 1-3초 |
| `GET` | `/health` | 헬스체크 | 즉시 |

### 6.3 AI 보고서 생성 흐름

```
1. Stock API Service (Java) -> POST /api/v1/analysis/full {symbol: "PLTR"}

2. Python 서비스 내부 처리:
   a. Polygon.io에서 1년치 일봉(OHLCV) 데이터 수집
   b. pandas-ta로 기술적 지표 계산
      - 이동평균선: MA5, MA20, MA60, MA120
      - RSI(14), MACD
      - 볼린저밴드 (상단/중단/하단)
      - 지지선/저항선
   c. Polygon.io 뉴스 API로 최근 2주 뉴스 수집
   d. 수집된 모든 데이터를 Claude API에 전달
      - 구조화된 JSON 출력(Structured Output) 요청
      - 4개 섹션별 분석 생성
   e. Claude 응답을 보고서 JSON으로 파싱

3. Python -> Stock API Service로 결과 반환

4. Java: MariaDB analysis_report 테이블에 저장 (24시간 캐시)
```

### 6.4 Java에서 Python 호출 (WebClient)

```java
// api/stock-service/.../client/AnalysisServiceClient.java
@Service
public class AnalysisServiceClient {
    private final WebClient webClient;

    public AnalysisServiceClient(
        @Value("${analysis-service.url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Mono<AnalysisReportResponse> requestFullAnalysis(String symbol, String stockName) {
        return webClient.post()
            .uri("/api/v1/analysis/full")
            .bodyValue(new AnalysisRequest(symbol, stockName, "FULL"))
            .retrieve()
            .bodyToMono(AnalysisReportResponse.class)
            .timeout(Duration.ofSeconds(60));
    }
}
```

---

## 7. 외부 API 연동

### 7.1 주식 데이터 API 비교

| API | 무료 제한 | 데이터 범위 | 뉴스 | 추천도 |
|-----|-----------|------------|------|--------|
| **Polygon.io** | 5 calls/min | OHLCV, 회사정보, 뉴스 | O | **1순위** |
| Alpha Vantage | 25 calls/day | OHLCV + 기술적 지표 내장 | X | 2순위 (대안) |
| Finnhub | 60 calls/min | 실시간 시세, 뉴스, 감성 | O | 3순위 (보조) |
| yfinance | 비공식, 무제한 | 과거 데이터, 기본정보 | X | 개발/테스트용 |

### 7.2 데이터 수집 전략

```
일일 배치 (매일 06:00 KST = 전날 미장 마감 후)
├── @Scheduled로 전날 종가 데이터 수집
├── Polygon.io grouped daily endpoint (1 call로 전체 종목)
└── stock_price 테이블에 저장

주간 배치 (매주 일요일)
├── 종목 메타데이터(시가총액, 섹터 등) 동기화
└── stock 테이블 업데이트

온디맨드 (사용자 요청 시)
├── AI 보고서 생성 시 Python 서비스에서 실시간 수집
└── 5분 TTL 인메모리 캐시 적용
```

### 7.3 캐싱 전략

| 레이어 | 도구 | TTL | 대상 |
|--------|------|-----|------|
| L1 | Python in-memory (TTLCache) | 5분 | 외부 API 원시 응답 |
| L2 | MariaDB | 영구 | 일봉 데이터, 기술적 지표 |
| L3 | Spring Caffeine Cache | 60초 | 인기 종목 최신 시세 |
| L4 | MariaDB (expires_at 컬럼) | 24시간 | AI 분석 보고서 |

---

## 8. API 서비스 모듈 설계

### 8.1 `api/stock-service` (Port 7004)

```
api/stock-service/src/main/java/com/zqksk/api/
├── StockServiceApplication.java
├── config/
│   ├── AppConfig.java
│   └── AnalysisServiceProperties.java      (@ConfigurationProperties)
├── client/
│   └── AnalysisServiceClient.java          (WebClient -> Python 서비스)
├── controller/v1/
│   ├── StockController.java                (GET /stock-api/v1/stocks/**)
│   ├── StockPriceController.java           (GET /stock-api/v1/prices/**)
│   ├── AnalysisController.java             (GET /stock-api/v1/analysis/**)
│   ├── WatchlistController.java            (CRUD /stock-api/v1/watchlist/**)
│   └── PortfolioController.java            (CRUD /stock-api/v1/portfolio/**)
├── scheduler/
│   └── StockDataScheduler.java             (@Scheduled 일일/주간 배치)
└── service/
    ├── StockQueryService.java
    ├── StockCommandService.java
    ├── AnalysisOrchestrationService.java   (캐시 확인 -> Python 호출 -> DB 저장)
    └── StockDataSyncService.java           (외부 API -> DB 배치 삽입)
```

**주요 API:**

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| `GET` | `/stock-api/v1/stocks/{symbol}` | 공개 | 종목 상세 정보 |
| `GET` | `/stock-api/v1/stocks/search?q=` | 공개 | 종목 검색 |
| `GET` | `/stock-api/v1/prices/{symbol}` | 공개 | 가격 히스토리 |
| `GET` | `/stock-api/v1/prices/{symbol}/latest` | 공개 | 최신 시세 |
| `GET` | `/stock-api/v1/analysis/{symbol}` | 인증 | AI 분석 보고서 조회/생성 |
| `POST` | `/stock-api/v1/analysis/{symbol}/refresh` | 인증 | 보고서 강제 갱신 |
| `GET` | `/stock-api/v1/watchlist` | 인증 | 내 관심종목 목록 |
| `POST` | `/stock-api/v1/watchlist` | 인증 | 관심종목 추가 |
| `DELETE` | `/stock-api/v1/watchlist/{symbol}` | 인증 | 관심종목 제거 |
| `GET` | `/stock-api/v1/portfolio` | 인증 | 내 보유종목 목록 |
| `POST` | `/stock-api/v1/portfolio` | 인증 | 보유종목 추가 |

### 8.2 `api/community-service` (Port 7005)

```
api/community-service/src/main/java/com/zqksk/api/
├── CommunityServiceApplication.java
├── controller/v1/
│   ├── PostController.java              (CRUD /community-api/v1/posts/**)
│   ├── CommentController.java           (CRUD /community-api/v1/comments/**)
│   ├── LikeController.java             (POST/DELETE /community-api/v1/likes/**)
│   └── FollowController.java           (POST/DELETE /community-api/v1/follows/**)
└── service/
    ├── PostOrchestrationService.java
    ├── CommentOrchestrationService.java
    └── FeedService.java
```

**주요 API:**

| Method | Path | 인증 | 설명 |
|--------|------|------|------|
| `GET` | `/community-api/v1/posts` | 공개 | 게시글 목록 (페이징) |
| `GET` | `/community-api/v1/posts?stock=PLTR` | 공개 | 종목별 커뮤니티 |
| `GET` | `/community-api/v1/posts/{id}` | 공개 | 게시글 상세 |
| `POST` | `/community-api/v1/posts` | 인증 | 게시글 작성 |
| `PUT` | `/community-api/v1/posts/{id}` | 인증 | 게시글 수정 |
| `DELETE` | `/community-api/v1/posts/{id}` | 인증 | 게시글 삭제 |
| `GET` | `/community-api/v1/posts/{id}/comments` | 공개 | 댓글 목록 |
| `POST` | `/community-api/v1/comments` | 인증 | 댓글 작성 |
| `POST` | `/community-api/v1/likes/post/{id}` | 인증 | 게시글 좋아요 |
| `DELETE` | `/community-api/v1/likes/post/{id}` | 인증 | 좋아요 취소 |
| `POST` | `/community-api/v1/follows/{userId}` | 인증 | 팔로우 |
| `DELETE` | `/community-api/v1/follows/{userId}` | 인증 | 언팔로우 |

---

## 9. Storage 레이어 변경사항

### 9.1 새로 추가할 파일

```
storage/database/src/main/java/com/zqksk/api/datasource/
├── stock/
│   ├── StockEntity.java, StockJpaRepository.java, StockCoreRepository.java
│   ├── StockPriceEntity.java, StockPriceJpaRepository.java, StockPriceCoreRepository.java
│   └── TechnicalIndicatorEntity.java, TechnicalIndicatorJpaRepository.java
├── community/
│   ├── PostEntity.java, PostJpaRepository.java, PostCoreRepository.java
│   ├── CommentEntity.java, CommentJpaRepository.java, CommentCoreRepository.java
│   ├── PostLikeEntity.java, CommentLikeEntity.java, LikeJpaRepository.java, LikeCoreRepository.java
│   └── FollowEntity.java, FollowJpaRepository.java, FollowCoreRepository.java
├── analysis/
│   └── AnalysisReportEntity.java, AnalysisReportJpaRepository.java, AnalysisReportCoreRepository.java
└── portfolio/
    ├── WatchlistEntity.java, WatchlistJpaRepository.java, WatchlistCoreRepository.java
    └── UserHoldingEntity.java, UserHoldingJpaRepository.java, UserHoldingCoreRepository.java
```

### 9.2 `storage/database/build.gradle.kts` 변경

```kotlin
dependencies {
    // 제거:
    // implementation(project(":domain:customer"))
    // implementation(project(":domain:dentistry"))
    // implementation(project(":domain:pc"))
    // implementation(project(":domain:competitor"))
    // implementation(project(":domain:notices"))

    // 유지:
    implementation(project(":domain:user"))
    implementation(project(":domain:log"))
    implementation(project(":domain:common"))

    // 추가:
    implementation(project(":domain:stock"))
    implementation(project(":domain:community"))
    implementation(project(":domain:analysis"))
    implementation(project(":domain:portfolio"))
}
```

---

## 10. Gateway 수정사항

### 10.1 `api/gateway/.../ApiServer.java`

```java
public enum ApiServer {
    AUTH_SERVER("AUTH-SERVER", "/auth/**", "lb://ZQKSK-AUTH-SERVICE"),
    STOCK_API_SERVER("STOCK-API-SERVER", "/stock-api/**", "lb://ZQKSK-STOCK-API-SERVICE"),
    COMMUNITY_API_SERVER("COMMUNITY-API-SERVER", "/community-api/**", "lb://ZQKSK-COMMUNITY-API-SERVICE");
}
```

### 10.2 `api/gateway/.../AuthorizationFilter.java`

공개 경로 추가:
```java
|| path.contains("/stock-api/v1/stocks")     // 종목 데이터 공개
|| path.contains("/stock-api/v1/prices")     // 가격 데이터 공개
```

---

## 11. Docker Compose 추가

```yaml
  # Stock API Service
  stock-service:
    build:
      context: .
      dockerfile: docker/stock-service/Dockerfile
    container_name: zqksk-stock-service
    ports: ["7004:7004"]
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=mariadb
      - POLYGON_API_KEY=${POLYGON_API_KEY}
    depends_on:
      mariadb: { condition: service_healthy }
      discovery: { condition: service_healthy }

  # Community API Service
  community-service:
    build:
      context: .
      dockerfile: docker/community-service/Dockerfile
    container_name: zqksk-community-service
    ports: ["7005:7005"]
    environment:
      - SPRING_PROFILES_ACTIVE=docker
      - DB_HOST=mariadb
    depends_on:
      mariadb: { condition: service_healthy }
      discovery: { condition: service_healthy }

  # Python Analysis Service
  analysis-service:
    build:
      context: ./tools/analysis-service
      dockerfile: Dockerfile
    container_name: zqksk-analysis-service
    ports: ["8000:8000"]
    environment:
      - POLYGON_API_KEY=${POLYGON_API_KEY}
      - ANTHROPIC_API_KEY=${ANTHROPIC_API_KEY}
    deploy:
      resources:
        limits: { memory: 2G }
```

---

## 12. 수정 대상 기존 파일 목록

| 파일 | 변경 내용 |
|------|-----------|
| `settings.gradle.kts` | 신규 모듈 6개 include 추가 |
| `storage/database/build.gradle.kts` | 치과 domain 의존성 -> 주식 domain 의존성 교체 |
| `api/gateway/.../config/ApiServer.java` | 라우팅 엔트리 추가 |
| `api/gateway/.../filter/AuthorizationFilter.java` | 공개 경로 수정 |
| `docker-compose.yml` | 3개 서비스 컨테이너 추가 |
| `domain/common` | 치과 enum 제거, 주식 enum 추가 |

---

## 13. 구현 순서 (Phase별)

```
Phase 0: 준비 (설정 파일 수정, API 키 발급)
    │
    v
Phase 1: Stock 도메인 + Storage (핵심 데이터 모델)
    │
    ├──────────────────────────┐
    v                          v
Phase 2: Stock API 서비스     Phase 4: Community (병행 가능)
    │                          │
    v                          v
Phase 3: Python + AI 보고서   Phase 5: Portfolio (병행 가능)
    │                          │
    └──────────┬───────────────┘
               v
          Phase 6: 치과 코드 정리/제거
```

| Phase | 내용 | 완료 후 가능한 것 |
|-------|------|-------------------|
| **0** | settings.gradle.kts 수정, Polygon.io/Anthropic 계정 발급 | 빌드 환경 준비 |
| **1** | `domain/stock` + `storage/database` 엔티티/레포지토리 | 종목/가격 데이터 저장 |
| **2** | `api/stock-service` + Gateway 연동 + 데이터 수집 스케줄러 | 종목 검색, 가격 조회 API |
| **3** | Python `tools/analysis-service` + `domain/analysis` + AI 보고서 | AI 분석 보고서 생성/조회 |
| **4** | `domain/community` + `api/community-service` | 게시글, 댓글, 좋아요, 팔로우 |
| **5** | `domain/portfolio` + Watchlist/Holding API | 관심종목, 보유종목 관리 |
| **6** | 치과 관련 코드 전부 제거, 정리 | 최종 정리 완료 |

> Phase 2+4, Phase 3+5는 각각 병행 개발 가능 (Phase 1 완료 후)

---

## 14. 검증 방법

| Phase | 검증 |
|-------|------|
| 1 | H2 인메모리 DB로 CoreRepository 단위 테스트 |
| 2 | `curl /stock-api/v1/stocks/AAPL` 종목 조회 확인, 배치 스케줄러 실행 확인 |
| 3 | `curl /stock-api/v1/analysis/PLTR` AI 보고서 생성 확인 (4페이지 JSON 구조) |
| 4 | `curl /community-api/v1/posts` 게시글 CRUD 확인 |
| 5 | `curl /stock-api/v1/watchlist` 관심종목 추가/삭제 확인 |
| 전체 | `docker-compose up` 으로 모든 서비스 통합 테스트 |
