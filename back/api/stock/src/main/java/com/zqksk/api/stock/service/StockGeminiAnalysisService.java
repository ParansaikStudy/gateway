package com.zqksk.api.stock.service;

import com.zqksk.api.stock.analysis.AnalysisData;
import com.zqksk.api.stock.analysis.TechnicalIndicatorCalculator;
import com.zqksk.api.stock.client.GeminiClient;
import com.zqksk.api.stock.client.KisApiClient;
import com.zqksk.api.stock.client.KisDailyItem;
import com.zqksk.api.stock.client.KisOverseasApiClient;
import com.zqksk.api.stock.client.KisPriceOutput;
import com.zqksk.api.stock.config.GeminiProperties;
import com.zqksk.api.stock.config.KisProperties;
import com.zqksk.api.stock.model.AnalysisResponse;
import com.zqksk.api.stock.storage.Top100AnalysisStore;
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
 * 요청 시: 한투 실시간 조회 + 저장된 국내 탑100 + 해외 탑100 분석(JSON 파일) → Gemini에 보내
 * "지금 {주식명(티커)}은 하방/상방 압력입니다. ... 따라서 매도 손실율, 매수 수익률 ..." 형식으로 결론 생성 후 반환.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StockGeminiAnalysisService {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final int DAILY_DAYS = 120;

    private final KisProperties kisProperties;
    private final GeminiProperties geminiProperties;
    private final KisApiClient kisApiClient;
    private final KisOverseasApiClient kisOverseasApiClient;
    private final GeminiClient geminiClient;
    private final Top100AnalysisStore top100AnalysisStore;

    /**
     * 국내 종목 코드로 한투 실시간 + 탑100 파일 기반 Gemini 결론 생성.
     * 응답 conclusion 에 사용자에게 보낼 3문장 형식이 들어감.
     */
    public AnalysisResponse analyzeWithGemini(String stockCode) {
        return analyzeWithGemini(stockCode, true);
    }

    /**
     * @param useTop100 true면 탑100 분석 파일 포함, false면 실시간 데이터만 Gemini에 전달 (100위 밖 종목용).
     */
    public AnalysisResponse analyzeWithGemini(String stockCode, boolean useTop100) {
        String code = stockCode != null ? stockCode.trim() : "";
        if (code.isEmpty()) {
            return new AnalysisResponse("오류: 종목 코드가 없습니다.", null, null);
        }
        if (!code.matches("\\d{6}")) {
            return new AnalysisResponse("국내 종목 6자리 코드만 지원합니다.", null, null);
        }
        if (!kisProperties.isConfigured()) {
            return new AnalysisResponse("KIS API 미설정. 분석 불가.", null, null);
        }
//        if (!geminiProperties.isConfigured()) {
//            return new AnalysisResponse("Gemini API 미설정. gemini.api-key 또는 GEMINI_API_KEY 설정 필요.", null, null);
//        }

        try {
            KisPriceOutput price = kisApiClient.inquirePrice(code);
            String endDate = LocalDate.now().format(YYYYMMDD);
            String startDate = LocalDate.now().minusDays(DAILY_DAYS).format(YYYYMMDD);
            List<KisDailyItem> dailyItems = kisApiClient.inquireDailyChart(code, startDate, endDate);
            AnalysisData data = buildAnalysisData(price, dailyItems);
            String realTimeSummary = buildRealTimeSummary(data, false);

            String prompt;
            if (useTop100) {
                String domesticText = top100AnalysisStore.load()
                    .orElse("(저장된 국내 탑100 분석 없음. 배치가 아직 한 번도 실행되지 않았을 수 있습니다.)");
                String overseasText = top100AnalysisStore.loadOverseas()
                    .orElse("(저장된 해외 탑100 분석 없음. 배치가 아직 한 번도 실행되지 않았을 수 있습니다.)");
                prompt = "다음은 **국내** 인기 종목 탑 100에 대한 요약 분석입니다:\n" + domesticText + "\n\n"
                    + "다음은 **해외** 인기 종목 탑 100에 대한 요약 분석입니다:\n" + overseasText + "\n\n"
                    + "아래는 사용자가 요청한 종목(코드 " + code + ")의 실시간 시세·기술지표 요약입니다:\n" + realTimeSummary + "\n\n"
                    + "위 국내·해외 시장 요약과 요청 종목 데이터를 바탕으로, 이 종목에 대해 **반드시 아래 3문장 형식으로만** 답해 주세요. 다른 설명은 붙이지 마세요. 주식명 뒤에 (티커 코드)는 넣지 마세요.\n"
                    + "1) 지금 {주식명}은 [상방/하방] 압력입니다.\n"
                    + "2) [단기선 아래/위], [중기선 근처/위/아래]로 [조정국면/반등 가능성 등] 이러한 상황입니다.\n"
                    + "3) 따라서 현재 매도하면 손실율 약 -x~-y%, 매수하면 반등 시 수익률 약 +a~+b% 가능합니다.";
            } else {
                prompt = "아래는 사용자가 요청한 종목(코드 " + code + ")의 실시간 시세·기술지표 요약입니다.\n"
                    + "이 데이터만 바탕으로 이 종목에 대해 **반드시 아래 3문장 형식으로만** 답해 주세요. 다른 설명은 붙이지 마세요. 주식명 뒤에 (티커 코드)는 넣지 마세요.\n\n"
                    + realTimeSummary + "\n\n"
                    + "1) 지금 {주식명}은 [상방/하방] 압력입니다.\n"
                    + "2) [단기선 아래/위], [중기선 근처/위/아래]로 [조정국면/반등 가능성 등] 이러한 상황입니다.\n"
                    + "3) 따라서 현재 매도하면 손실율 약 -x~-y%, 매수하면 반등 시 수익률 약 +a~+b% 가능합니다.";
            }

            String conclusion = geminiClient.generateContent(prompt);
            if (conclusion == null || conclusion.isBlank()) {
                return new AnalysisResponse("Gemini 분석 결과가 비어 있습니다.", null, null);
            }
            conclusion = removeTickerFromConclusion(conclusion);
            String summary = firstLine(conclusion);
            return new AnalysisResponse(summary, conclusion, conclusion);
        } catch (Exception e) {
            log.warn("Gemini 분석 실패: stockCode={}, error={}", code, e.getMessage());
            return new AnalysisResponse(
                "분석 중 오류가 발생했습니다.",
                "오류: " + e.getMessage(),
                null
            );
        }
    }

    /**
     * 해외 종목(거래소+심볼)으로 한투 해외 API 조회 후 탑100 여부에 따라 Gemini 결론 생성.
     * @param useTop100 true면 국내·해외 탑100 분석 맥락 포함.
     */
    public AnalysisResponse analyzeWithGeminiOverseas(String excd, String symbol, boolean useTop100) {
        if (excd == null || symbol == null || excd.isBlank() || symbol.isBlank()) {
            return new AnalysisResponse("오류: 거래소코드·심볼이 없습니다.", null, null);
        }
        if (!kisProperties.isConfigured()) {
            return new AnalysisResponse("KIS API 미설정. 분석 불가.", null, null);
        }
        try {
            KisPriceOutput price = kisOverseasApiClient.inquireOverseasPrice(excd.trim(), symbol.trim());
            String endDate = LocalDate.now().format(YYYYMMDD);
            List<KisDailyItem> dailyItems = kisOverseasApiClient.inquireOverseasDailyChart(excd.trim(), symbol.trim(), endDate);
            AnalysisData data = buildAnalysisData(price, dailyItems);
            String realTimeSummary = buildRealTimeSummary(data, true);

            String stockLabel = excd + " " + symbol;
            String prompt;
            if (useTop100) {
                String domesticText = top100AnalysisStore.load()
                    .orElse("(저장된 국내 탑100 분석 없음.)");
                String overseasText = top100AnalysisStore.loadOverseas()
                    .orElse("(저장된 해외 탑100 분석 없음.)");
                prompt = "다음은 **국내** 인기 종목 탑 100에 대한 요약 분석입니다:\n" + domesticText + "\n\n"
                    + "다음은 **해외** 인기 종목 탑 100에 대한 요약 분석입니다:\n" + overseasText + "\n\n"
                    + "아래는 사용자가 요청한 해외 종목(" + stockLabel + ")의 실시간 시세·기술지표 요약입니다:\n" + realTimeSummary + "\n\n"
                    + "위 국내·해외 시장 요약과 요청 종목 데이터를 바탕으로, 이 종목에 대해 **반드시 아래 3문장 형식으로만** 답해 주세요. 다른 설명은 붙이지 마세요. 주식명 뒤에 (티커 코드)는 넣지 마세요.\n"
                    + "1) 지금 {주식명}은 [상방/하방] 압력입니다.\n"
                    + "2) [단기선 아래/위], [중기선 근처/위/아래]로 [조정국면/반등 가능성 등] 이러한 상황입니다.\n"
                    + "3) 따라서 현재 매도하면 손실율 약 -x~-y%, 매수하면 반등 시 수익률 약 +a~+b% 가능합니다.";
            } else {
                prompt = "아래는 사용자가 요청한 해외 종목(" + stockLabel + ")의 실시간 시세·기술지표 요약입니다.\n"
                    + "이 데이터만 바탕으로 이 종목에 대해 **반드시 아래 3문장 형식으로만** 답해 주세요. 다른 설명은 붙이지 마세요. 주식명 뒤에 (티커 코드)는 넣지 마세요.\n\n"
                    + realTimeSummary + "\n\n"
                    + "1) 지금 {주식명}은 [상방/하방] 압력입니다.\n"
                    + "2) [단기선 아래/위], [중기선 근처/위/아래]로 [조정국면/반등 가능성 등] 이러한 상황입니다.\n"
                    + "3) 따라서 현재 매도하면 손실율 약 -x~-y%, 매수하면 반등 시 수익률 약 +a~+b% 가능합니다.";
            }

            String conclusion = geminiClient.generateContent(prompt);
            if (conclusion == null || conclusion.isBlank()) {
                return new AnalysisResponse("Gemini 분석 결과가 비어 있습니다.", null, null);
            }
            conclusion = removeTickerFromConclusion(conclusion);
            String summary = firstLine(conclusion);
            return new AnalysisResponse(summary, conclusion, conclusion);
        } catch (Exception e) {
            log.warn("Gemini 해외 분석 실패: excd={}, symbol={}, error={}", excd, symbol, e.getMessage());
            return new AnalysisResponse(
                "분석 중 오류가 발생했습니다.",
                "오류: " + e.getMessage(),
                null
            );
        }
    }

    private AnalysisData buildAnalysisData(KisPriceOutput price, List<KisDailyItem> dailyItems) {
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

    private String buildRealTimeSummary(AnalysisData d, boolean overseas) {
        double cur = parseDouble(d.getPrice().getStck_prpr());
        double changePct = parseDouble(d.getPrice().getPrdy_ctrt());
        StringBuilder sb = new StringBuilder();
        if (overseas) {
            sb.append(String.format(Locale.US, "현재가 $%.2f, 전일대비 %.2f%%. ", cur, changePct));
        } else {
            sb.append(String.format(Locale.US, "현재가 %.0f원, 전일대비 %.2f%%. ", cur, changePct));
        }
        if (d.getMa5() != null && !Double.isNaN(d.getMa5())) {
            sb.append(overseas ? String.format(Locale.US, "5일선 $%.2f, ", d.getMa5()) : String.format(Locale.US, "5일선 %.0f원, ", d.getMa5()));
        }
        if (d.getMa20() != null && !Double.isNaN(d.getMa20())) {
            double gap20 = ((cur - d.getMa20()) / d.getMa20()) * 100;
            sb.append(overseas ? String.format(Locale.US, "20일선 $%.2f(현재가 대비 %.1f%%), ", d.getMa20(), gap20) : String.format(Locale.US, "20일선 %.0f원(현재가 대비 %.1f%%), ", d.getMa20(), gap20));
        }
        if (d.getMa60() != null && !Double.isNaN(d.getMa60())) {
            sb.append(overseas ? String.format(Locale.US, "60일선 $%.2f. ", d.getMa60()) : String.format(Locale.US, "60일선 %.0f원. ", d.getMa60()));
        }
        if (d.getRsi() != null && !Double.isNaN(d.getRsi())) {
            sb.append(String.format(Locale.US, "RSI(14)=%.1f. ", d.getRsi()));
        }
        if (d.getSupportResistance() != null) {
            double r = d.getSupportResistance().getResistance();
            double s = d.getSupportResistance().getSupport();
            if (r > 0 || s > 0) {
                sb.append(overseas ? String.format(Locale.US, "저항 $%.2f, 지지 $%.2f. ", r, s) : String.format(Locale.US, "저항 %.0f, 지지 %.0f. ", r, s));
            }
        }
        return sb.toString();
    }

    /** 응답에서 "(티커 005930)" 형태 제거 */
    private static String removeTickerFromConclusion(String text) {
        if (text == null) return "";
        return text.replaceAll("\\(티커\\s*\\d{6}\\)", "").trim().replaceAll("\\s+", " ");
    }

    private static String firstLine(String text) {
        if (text == null) return "";
        int i = text.indexOf('\n');
        return i > 0 ? text.substring(0, i).trim() : text.trim();
    }

    private static double parseDouble(String s) {
        if (s == null || s.isBlank()) return 0;
        try {
            return Double.parseDouble(s.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
