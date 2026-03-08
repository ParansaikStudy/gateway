package com.zqksk.api.stock.service;

import com.zqksk.api.stock.analysis.AnalysisData;
import com.zqksk.api.stock.analysis.TechnicalIndicatorCalculator;
import com.zqksk.api.stock.client.KisApiClient;
import com.zqksk.api.stock.client.KisDailyItem;
import com.zqksk.api.stock.client.KisOverseasApiClient;
import com.zqksk.api.stock.client.KisPriceOutput;
import com.zqksk.api.stock.config.KisProperties;
import com.zqksk.api.stock.model.AnalysisRequest;
import com.zqksk.api.stock.model.AnalysisResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * KIS Open API 현재가·일봉을 조회하고, 기술적 지표(MA, RSI, MACD, 변동성, 볼린저 등)를
 * 공식에 따라 계산한 뒤 수치 기반 전문 분석 문장을 생성합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockAnalysisService {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int DAILY_DAYS = 120; // 약 6개월 일봉

    private final KisProperties kisProperties;
    private final KisApiClient kisApiClient;
    private final KisOverseasApiClient kisOverseasApiClient;

    public AnalysisResponse analyze(AnalysisRequest request) {
        String stockCode = request != null && request.getStockCode() != null
            ? request.getStockCode().trim() : "";
        if (stockCode.isEmpty()) {
            log.warn("분석 요청: stockCode가 비어 있음");
            return new AnalysisResponse("오류: 종목 코드가 없습니다.", "종목 코드를 입력해 주세요.", null);
        }

        boolean overseas = request.isOverseas();

        // 프론트에서 시세·일봉을 보냈으면 백엔드 KIS 설정 없이 분석 (국내/해외 구분 없음, KIS에서 제공하는 종목이면 분석)
        if (request.getPrice() != null && request.getDailyChart() != null && !request.getDailyChart().isEmpty()) {
            try {
                AnalysisData data = computeFromProvidedData(request.getPrice(), request.getDailyChart());
                String summary = buildSummary(data, stockCode, overseas);
                String fullAnalysis = buildFullAnalysis(data, stockCode, overseas);
                String conclusion = buildUserConclusion(data, stockCode, overseas);
                return new AnalysisResponse(summary, fullAnalysis, conclusion);
            } catch (Exception e) {
                log.warn("분석 실패(프론트 전달 데이터): stockCode={}, error={}", stockCode, e.getMessage());
                return new AnalysisResponse(
                    "분석 중 오류가 발생했습니다.",
                    "전달된 시세·일봉 데이터 처리 중 오류: " + e.getMessage(),
                    null
                );
            }
        }

        // 백엔드에서 KIS로 직접 조회: 국내(6자리) 또는 해외(stockCode + exchange)
        if (!stockCode.matches("\\d{6}")) {
            String exchange = request.getExchange() != null ? request.getExchange().trim() : "";
            if (exchange.isEmpty()) {
                return new AnalysisResponse(
                    "해외 종목은 거래소 코드가 필요합니다.",
                    "해외 종목 분석 시 exchange(거래소코드)를 함께 보내주세요. 예: NAS(나스닥), NYS(뉴욕), AMS(아멕스).",
                    null
                );
            }
            if (!kisProperties.isConfigured()) {
                return new AnalysisResponse(
                    "KIS API 미설정. 분석 불가.",
                    "설정에서 한국투자증권 API를 연결한 뒤 다시 요청해 주세요.",
                    null
                );
            }
            try {
                AnalysisData data = fetchOverseasAndCompute(stockCode, exchange);
                String summary = buildSummary(data, stockCode, true);
                String fullAnalysis = buildFullAnalysis(data, stockCode, true);
                String conclusion = buildUserConclusion(data, stockCode, true);
                return new AnalysisResponse(summary, fullAnalysis, conclusion);
            } catch (Exception e) {
                log.warn("해외 분석 실패: symbol={}, exchange={}, error={}", stockCode, exchange, e.getMessage());
                return new AnalysisResponse(
                    "분석 중 오류가 발생했습니다.",
                    "해외 시세 조회 또는 지표 계산 중 오류: " + e.getMessage() + ". 종목코드와 거래소 코드를 확인해 주세요.",
                    null
                );
            }
        }

        if (!kisProperties.isConfigured()) {
            return new AnalysisResponse(
                "KIS API 미설정. 분석 불가.",
                "설정에서 한국투자증권 API를 연결한 뒤, 종목 분석 화면에서 다시 요청해 주세요. (연결한 계정의 시세·일봉으로 분석합니다.)",
                null
            );
        }

        try {
            AnalysisData data = fetchAndCompute(stockCode);
            String summary = buildSummary(data, stockCode, false);
            String fullAnalysis = buildFullAnalysis(data, stockCode, false);
            String conclusion = buildUserConclusion(data, stockCode, false);
            return new AnalysisResponse(summary, fullAnalysis, conclusion);
        } catch (Exception e) {
            log.warn("분석 실패: stockCode={}, error={}", stockCode, e.getMessage());
            return new AnalysisResponse(
                "분석 중 오류가 발생했습니다.",
                "시세 조회 또는 지표 계산 중 오류: " + e.getMessage() + ". 종목코드와 API 설정을 확인해 주세요.",
                null
            );
        }
    }

    // ─── 가격 포맷 헬퍼 ────────────────────────────────────────────────

    /** 해외: $1,234.56 / 국내: 1,234원 */
    private static String fmtPrice(double v, boolean overseas) {
        if (overseas) {
            return String.format(Locale.US, "$%,.2f", v);
        }
        return String.format(Locale.US, "%,.0f원", v);
    }

    /** 해외: $12.34 (절대값 변동) / 국내: 12,345원 */
    private static String fmtChange(double v, boolean overseas) {
        if (overseas) {
            return String.format(Locale.US, "$%,.2f", Math.abs(v));
        }
        return String.format(Locale.US, "%,.0f원", Math.abs(v));
    }

    // ─── 결론 ────────────────────────────────────────────────────────

    /** 상세 분석 데이터로 사용자용 결론 문장 생성 (압력·상황·매도 손실/매수 반등) */
    private String buildUserConclusion(AnalysisData d, String stockCode, boolean overseas) {
        double cur = parseDouble(d.getPrice().getStck_prpr());
        Double ma5 = d.getMa5();
        Double ma20 = d.getMa20();
        Double rsi = d.getRsi();
        TechnicalIndicatorCalculator.MacdResult macd = d.getMacd();
        TechnicalIndicatorCalculator.SupportResistance sr = d.getSupportResistance();

        String pressure = "중립";
        if (rsi != null && !Double.isNaN(rsi)) {
            if (rsi >= 70) pressure = "상방";
            else if (rsi <= 30) pressure = "하방";
            else if (ma20 != null && !Double.isNaN(ma20)) {
                pressure = cur >= ma20 ? "상방" : "하방";
            }
        } else if (ma20 != null && !Double.isNaN(ma20)) {
            pressure = cur >= ma20 ? "상방" : "하방";
        }

        StringBuilder situation = new StringBuilder();
        if (ma5 != null && !Double.isNaN(ma5)) {
            situation.append(cur < ma5 ? "단기선 아래" : "단기선 위");
        }
        if (ma20 != null && !Double.isNaN(ma20)) {
            double gap20Pct = Math.abs((cur - ma20) / ma20) * 100;
            if (situation.length() > 0) situation.append("/");
            if (gap20Pct < 5) situation.append("중기선 근처");
            else situation.append(cur >= ma20 ? "중기선 위" : "중기선 아래");
        }
        if (rsi != null && !Double.isNaN(rsi) && rsi >= 65) {
            situation.append("로 조정국면");
        } else if (rsi != null && !Double.isNaN(rsi) && rsi <= 35) {
            situation.append("로 반등 가능성");
        } else {
            situation.append("로, 이러한 상황");
        }
        situation.append("입니다.");

        String lossRange = "약 -5~-10%";
        String upsideRange = "약 +10~+15%";
        if (sr != null) {
            double support = sr.getSupport();
            double resistance = sr.getResistance();
            if (support > 0) {
                double downPct = (cur - support) / cur * 100;
                if (downPct <= 8) lossRange = "약 -5~-8%";
                else if (downPct <= 15) lossRange = "약 -8~-15%";
                else if (downPct <= 25) lossRange = "약 -15~-25%";
                else lossRange = String.format(Locale.US, "약 -%.0f~-%.0f%%", Math.min(downPct - 3, 40), Math.min(downPct + 3, 50));
            }
            if (resistance > cur) {
                double upPct = (resistance - cur) / cur * 100;
                if (upPct <= 5) upsideRange = String.format(Locale.US, "약 +%.0f~+%.0f%%", Math.max(upPct - 2, 0), upPct + 2);
                else if (upPct <= 15) upsideRange = "약 +10~+15%";
                else if (upPct <= 25) upsideRange = String.format(Locale.US, "약 +%.0f~+%.0f%%", upPct - 3, upPct + 3);
                else upsideRange = String.format(Locale.US, "약 +%.0f~+%.0f%%", upPct - 5, upPct + 5);
            }
        }

        return String.format(Locale.KOREA,
            "지금 {주식명(티커 %s)}은 %s 압력입니다.\n%s\n따라서 현재 매도하면 손실율 %s, 매수하면 반등 시 수익률 %s 가능합니다.",
            stockCode, pressure, situation, lossRange, upsideRange);
    }

    // ─── 데이터 계산 ────────────────────────────────────────────────

    /** 프론트에서 전달한 현재가·일봉으로 지표 계산 (백엔드 KIS 호출 없음) */
    private AnalysisData computeFromProvidedData(KisPriceOutput price, List<KisDailyItem> dailyChart) {
        List<KisDailyItem> sorted = new ArrayList<>(dailyChart);
        sorted.sort((a, b) -> (a.getStck_bsop_date() != null && b.getStck_bsop_date() != null)
            ? a.getStck_bsop_date().compareTo(b.getStck_bsop_date())
            : 0);

        List<Double> closes = new ArrayList<>();
        List<Double> highs = new ArrayList<>();
        List<Double> lows = new ArrayList<>();
        for (KisDailyItem d : sorted) {
            closes.add(parseDouble(d.getStck_clpr()));
            highs.add(parseDouble(d.getStck_hgpr()));
            lows.add(parseDouble(d.getStck_lwpr()));
        }

        Double ma5 = closes.size() >= 5 ? TechnicalIndicatorCalculator.ma(closes, 5) : null;
        Double ma20 = closes.size() >= 20 ? TechnicalIndicatorCalculator.ma(closes, 20) : null;
        Double ma60 = closes.size() >= 60 ? TechnicalIndicatorCalculator.ma(closes, 60) : null;
        Double rsi = closes.size() >= 15 ? TechnicalIndicatorCalculator.rsi14(closes) : null;
        TechnicalIndicatorCalculator.MacdResult macd = TechnicalIndicatorCalculator.macd(closes);
        Double volatility20 = closes.size() >= 21 ? TechnicalIndicatorCalculator.volatility(closes, 20) : null;
        TechnicalIndicatorCalculator.BollingerResult bollinger = TechnicalIndicatorCalculator.bollingerBands(closes);
        TechnicalIndicatorCalculator.SupportResistance sr = TechnicalIndicatorCalculator.supportResistance(highs, lows, Math.min(20, highs.size()));

        return AnalysisData.builder()
            .price(price)
            .dailyItems(Collections.unmodifiableList(dailyChart))
            .closes(Collections.unmodifiableList(closes))
            .highs(Collections.unmodifiableList(highs))
            .lows(Collections.unmodifiableList(lows))
            .ma5(ma5)
            .ma20(ma20)
            .ma60(ma60)
            .rsi(rsi)
            .macd(macd)
            .volatility20(volatility20)
            .bollinger(bollinger)
            .supportResistance(sr)
            .build();
    }

    /** 해외 종목: KIS 해외 API로 현재가·기간별시세 조회 후 지표 계산 */
    private AnalysisData fetchOverseasAndCompute(String symbol, String exchange) {
        KisPriceOutput price = kisOverseasApiClient.inquireOverseasPrice(exchange, symbol);
        String endDate = LocalDate.now().format(YYYYMMDD);
        List<KisDailyItem> dailyItems = kisOverseasApiClient.inquireOverseasDailyChart(exchange, symbol, endDate);
        if (dailyItems == null || dailyItems.isEmpty()) {
            throw new IllegalStateException("해외 일봉 데이터가 없습니다. 종목코드·거래소를 확인해 주세요.");
        }
        return computeFromProvidedData(price, dailyItems);
    }

    private AnalysisData fetchAndCompute(String stockCode) {
        KisPriceOutput price = kisApiClient.inquirePrice(stockCode);
        String endDate = LocalDate.now().format(YYYYMMDD);
        String startDate = LocalDate.now().minusDays(DAILY_DAYS).format(YYYYMMDD);
        List<KisDailyItem> dailyItems = kisApiClient.inquireDailyChart(stockCode, startDate, endDate);

        // 일봉은 API에서 최신순으로 올 수 있음 → 과거→현재 순으로 정렬
        List<KisDailyItem> sorted = new ArrayList<>(dailyItems);
        sorted.sort((a, b) -> (a.getStck_bsop_date() != null && b.getStck_bsop_date() != null)
            ? a.getStck_bsop_date().compareTo(b.getStck_bsop_date())
            : 0);

        List<Double> closes = new ArrayList<>();
        List<Double> highs = new ArrayList<>();
        List<Double> lows = new ArrayList<>();
        for (KisDailyItem d : sorted) {
            closes.add(parseDouble(d.getStck_clpr()));
            highs.add(parseDouble(d.getStck_hgpr()));
            lows.add(parseDouble(d.getStck_lwpr()));
        }

        Double ma5 = closes.size() >= 5 ? TechnicalIndicatorCalculator.ma(closes, 5) : null;
        Double ma20 = closes.size() >= 20 ? TechnicalIndicatorCalculator.ma(closes, 20) : null;
        Double ma60 = closes.size() >= 60 ? TechnicalIndicatorCalculator.ma(closes, 60) : null;
        Double rsi = closes.size() >= 15 ? TechnicalIndicatorCalculator.rsi14(closes) : null;
        TechnicalIndicatorCalculator.MacdResult macd = TechnicalIndicatorCalculator.macd(closes);
        Double volatility20 = closes.size() >= 21 ? TechnicalIndicatorCalculator.volatility(closes, 20) : null;
        TechnicalIndicatorCalculator.BollingerResult bollinger = TechnicalIndicatorCalculator.bollingerBands(closes);
        TechnicalIndicatorCalculator.SupportResistance sr = TechnicalIndicatorCalculator.supportResistance(highs, lows, Math.min(20, highs.size()));

        return AnalysisData.builder()
            .price(price)
            .dailyItems(Collections.unmodifiableList(dailyItems))
            .closes(Collections.unmodifiableList(closes))
            .highs(Collections.unmodifiableList(highs))
            .lows(Collections.unmodifiableList(lows))
            .ma5(ma5)
            .ma20(ma20)
            .ma60(ma60)
            .rsi(rsi)
            .macd(macd)
            .volatility20(volatility20)
            .bollinger(bollinger)
            .supportResistance(sr)
            .build();
    }

    private static double parseDouble(String s) {
        if (s == null || s.isBlank()) return 0;
        try {
            return Double.parseDouble(s.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ─── 요약 ────────────────────────────────────────────────────────

    private String buildSummary(AnalysisData d, String stockCode, boolean overseas) {
        double cur = parseDouble(d.getPrice().getStck_prpr());
        StringBuilder sb = new StringBuilder();
        if (d.getMa20() != null && !Double.isNaN(d.getMa20())) {
            double gap = ((cur - d.getMa20()) / d.getMa20()) * 100;
            sb.append(String.format(Locale.US, "현재가 %s, 20일선 대비 %.1f%%. ", fmtPrice(cur, overseas), gap));
        } else {
            sb.append(String.format(Locale.US, "현재가 %s. ", fmtPrice(cur, overseas)));
        }
        if (d.getRsi() != null && !Double.isNaN(d.getRsi())) {
            sb.append(String.format(Locale.US, "RSI(14)=%.1f. ", d.getRsi()));
        }
        if (d.getVolatility20() != null && !Double.isNaN(d.getVolatility20())) {
            sb.append(String.format(Locale.US, "20일 변동성 %.2f%%. ", d.getVolatility20()));
        }
        String per = d.getPrice().getPer();
        if (per != null && !per.isBlank() && !"-".equals(per)) {
            try {
                double perVal = Double.parseDouble(per.replace(",", ""));
                if (perVal > 0) sb.append(String.format(Locale.US, "PER %.1f배. ", perVal));
            } catch (NumberFormatException ignored) { }
        }
        String out = sb.toString().trim();
        return out.length() > 50 ? out.substring(0, 47) + "…" : out;
    }

    // ─── 상세 분석 ──────────────────────────────────────────────────

    private String buildFullAnalysis(AnalysisData d, String stockCode, boolean overseas) {
        KisPriceOutput p = d.getPrice();
        double cur = parseDouble(p.getStck_prpr());
        double open = parseDouble(p.getStck_oprc());
        double high = parseDouble(p.getStck_hgpr());
        double low = parseDouble(p.getStck_lwpr());
        double change = parseDouble(p.getPrdy_vrss());
        double changePct = parseDouble(p.getPrdy_ctrt());
        String sign = "2".equals(p.getPrdy_vrss_sign()) ? "+" : ("5".equals(p.getPrdy_vrss_sign()) ? "" : "");

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "【현재가·등락】 현재가 %s, 전일 대비 %s%s(%s%.2f%%). ",
            fmtPrice(cur, overseas), sign, fmtChange(change, overseas), sign, changePct));
        // 시가/고가/저가: 0이면 '미제공' 표시 (장 시작 전/해외 시간외)
        if (open > 0) sb.append(String.format(Locale.US, "시가 %s, ", fmtPrice(open, overseas)));
        if (high > 0) sb.append(String.format(Locale.US, "고가 %s, ", fmtPrice(high, overseas)));
        if (low > 0) sb.append(String.format(Locale.US, "저가 %s. ", fmtPrice(low, overseas)));

        if (d.getMa5() != null && !Double.isNaN(d.getMa5())) {
            sb.append(String.format(Locale.US, "【이동평균】 5일선 %s, ", fmtPrice(d.getMa5(), overseas)));
        }
        if (d.getMa20() != null && !Double.isNaN(d.getMa20())) {
            double gap20 = ((cur - d.getMa20()) / d.getMa20()) * 100;
            sb.append(String.format(Locale.US, "20일선 %s(현재가 대비 %.1f%%), ", fmtPrice(d.getMa20(), overseas), gap20));
        }
        if (d.getMa60() != null && !Double.isNaN(d.getMa60())) {
            sb.append(String.format(Locale.US, "60일선 %s. ", fmtPrice(d.getMa60(), overseas)));
        }

        if (d.getRsi() != null && !Double.isNaN(d.getRsi())) {
            String rsiStr = d.getRsi() >= 70 ? "과매수권" : (d.getRsi() <= 30 ? "과매도권" : "중립");
            sb.append(String.format(Locale.US, "【RSI(14)】 %.1f(%s). ", d.getRsi(), rsiStr));
        }
        if (d.getMacd() != null) {
            sb.append(String.format(Locale.US, "【MACD】 MACD선 %.2f, 시그널 %.2f, 히스토그램 %.2f. ",
                d.getMacd().getMacdLine(), d.getMacd().getSignalLine(), d.getMacd().getHistogram()));
        }
        if (d.getVolatility20() != null && !Double.isNaN(d.getVolatility20())) {
            sb.append(String.format(Locale.US, "【20일 변동성】 일일 수익률 표준편차 %.2f%%. ", d.getVolatility20()));
        }
        if (d.getBollinger() != null) {
            sb.append(String.format(Locale.US, "【볼린저밴드】 상단 %s, 중간 %s, 하단 %s, 폭 %.1f%%. ",
                fmtPrice(d.getBollinger().getUpper(), overseas),
                fmtPrice(d.getBollinger().getMiddle(), overseas),
                fmtPrice(d.getBollinger().getLower(), overseas),
                d.getBollinger().getBandwidth()));
        }
        if (d.getSupportResistance() != null) {
            double resistance = d.getSupportResistance().getResistance();
            double support = d.getSupportResistance().getSupport();
            // 0이면 미제공
            if (resistance > 0 || support > 0) {
                sb.append("【20일 구간】 ");
                if (resistance > 0) sb.append(String.format(Locale.US, "저항 %s, ", fmtPrice(resistance, overseas)));
                if (support > 0) sb.append(String.format(Locale.US, "지지 %s. ", fmtPrice(support, overseas)));
            }
        }

        String per = p.getPer();
        String pbr = p.getPbr();
        String eps = p.getEps();
        String cap = p.getHts_avls();
        String unit = overseas ? "$" : "원";
        if ((per != null && !per.isBlank() && !"-".equals(per)) || (pbr != null && !pbr.isBlank() && !"-".equals(pbr))) {
            sb.append("【밸류에이션】 ");
            if (per != null && !per.isBlank() && !"-".equals(per)) {
                try {
                    sb.append(String.format(Locale.US, "PER %s배, ", per));
                } catch (Exception ignored) { }
            }
            if (pbr != null && !pbr.isBlank() && !"-".equals(pbr)) sb.append("PBR ").append(pbr).append("배, ");
            if (eps != null && !eps.isBlank() && !"-".equals(eps)) {
                if (overseas) sb.append("EPS $").append(eps).append(", ");
                else sb.append("EPS ").append(eps).append("원, ");
            }
            if (cap != null && !cap.isBlank()) {
                if (overseas) sb.append("시총 $").append(cap).append(". ");
                else sb.append("시총 ").append(cap).append("원. ");
            }
        }

        sb.append("위 수치는 한국투자증권 Open API 현재가·일봉 데이터와 이동평균(MA), RSI(14), MACD(12,26,9), 20일 수익률 표준편차(변동성), 볼린저밴드(20,2), 20일 고저(지지·저항) 공식으로 계산한 결과입니다.");
        return sb.toString();
    }
}
