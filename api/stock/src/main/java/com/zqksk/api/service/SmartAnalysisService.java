package com.zqksk.api.service;

import com.zqksk.api.kis.KisDailyItem;
import com.zqksk.api.kis.KisClient;
import com.zqksk.api.kis.KisPriceOutput;
import com.zqksk.api.config.KisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmartAnalysisService {

    private static final int MA_SHORT_DAYS = 5;
    private static final int MA_MID_DAYS = 20;
    private static final int CHART_DAYS = 60;

    private final KisProperties kisProperties;
    private final KisClient kisClient;

    public AnalysisResult analyze(String stockCode, String stockName) {
        if (kisProperties.appKey() == null || kisProperties.appKey().isBlank()) {
            throw new IllegalStateException("KIS API 키가 설정되지 않았습니다. KIS_APP_KEY, KIS_APP_SECRET 환경변수를 설정하세요.");
        }
        String token = kisClient.getAccessToken();
        KisPriceOutput price = kisClient.getPrice(token, stockCode);
        String startDate = KisClient.toStartDate(CHART_DAYS);
        String endDate = KisClient.toEndDate();
        List<KisDailyItem> dailyItems = kisClient.getDailyChart(token, stockCode, startDate, endDate);

        long currentPrice = parseLong(price.stckPrpr(), 0L);
        if (dailyItems.isEmpty()) {
            return buildFallbackResult(stockName, stockCode, currentPrice);
        }

        // 일봉은 최신이 앞에 올 수 있음 (KIS 문서 확인). 역순으로 오래된 것부터 정렬해 인덱스 0 = 과거
        List<KisDailyItem> ordered = new ArrayList<>(dailyItems);
        ordered.sort((a, b) -> a.stckBsopDate().compareTo(b.stckBsopDate()));

        List<Long> closes = ordered.stream()
            .map(k -> parseLong(k.stckClpr(), 0L))
            .filter(v -> v > 0)
            .toList();
        if (closes.isEmpty()) {
            return buildFallbackResult(stockName, stockCode, currentPrice);
        }

        double ma5 = computeMA(closes, MA_SHORT_DAYS);
        double ma20 = computeMA(closes, MA_MID_DAYS);

        double diff5 = currentPrice - ma5;
        double diff20 = currentPrice - ma20;
        double pct5 = ma5 > 0 ? (diff5 / ma5) * 100 : 0;
        double pct20 = ma20 > 0 ? (diff20 / ma20) * 100 : 0;

        // 단기선(5일) 아래면 하방, 위면 상방. 중기선(20일) 근처는 ±2% 이내
        boolean belowShort = currentPrice < ma5;
        boolean nearMid = Math.abs(pct20) <= 2.0;
        String pressure = belowShort ? "하방" : (currentPrice > ma20 ? "상방" : "하방");
        String situation;
        if (belowShort && nearMid) {
            situation = "단기선 아래/중기선 근처로 조정국면";
        } else if (belowShort) {
            situation = "단기선 아래로 조정국면";
        } else {
            situation = "단기선 위 중기선 근처로 상승 모멘텀";
        }

        // 휴리스틱: 조정국면일 때 매도 손실 -5~-8%, 매수 반등 시 +10~+15%
        int lossMin = -8;
        int lossMax = -5;
        int gainMin = 10;
        int gainMax = 15;
        if ("상방".equals(pressure)) {
            lossMin = -3;
            lossMax = 0;
            gainMin = 5;
            gainMax = 10;
        }

        String displayName = (stockName != null && !stockName.isBlank()) ? stockName : stockCode;
        String summary = buildSummary(displayName);
        String fullAnalysis = String.format(
            "지금 %s(%s)은 %s 압력입니다. %s 이러한 상황입니다.%n%n따라서 현재 매도하면 손실율 약 %d~%d%%, 매수하면 반등 시 수익률 약 +%d~+%d%% 가능합니다.",
            displayName, stockCode, pressure, situation, lossMin, lossMax, gainMin, gainMax
        );

        return new AnalysisResult(summary, fullAnalysis);
    }

    private static String buildSummary(String displayName) {
        String base = displayName + " 15분 전 가격 기준 일봉·이동평균선 매매 가이드";
        return base.length() > 50 ? base.substring(0, 50) : base;
    }

    private static double computeMA(List<Long> closes, int n) {
        if (closes.size() < n) return closes.isEmpty() ? 0 : closes.get(closes.size() - 1);
        long sum = 0;
        for (int i = closes.size() - n; i < closes.size(); i++) {
            sum += closes.get(i);
        }
        return (double) sum / n;
    }

    private static long parseLong(String s, long fallback) {
        if (s == null || s.isBlank()) return fallback;
        try {
            return Long.parseLong(s.replaceAll("[^0-9-]", ""));
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static AnalysisResult buildFallbackResult(String stockName, String stockCode, long currentPrice) {
        String displayName = (stockName != null && !stockName.isBlank()) ? stockName : stockCode;
        String summary = buildSummary(displayName);
        String full = String.format(
            "지금 %s(%s)은 중립 압력입니다. 일봉 데이터 부족으로 이동평균선 분석 제한적입니다.%n%n따라서 현재 매도하면 손실율 약 -3~0%%, 매수하면 반등 시 수익률 약 +5~+10%% 가능합니다.",
            displayName, stockCode
        );
        return new AnalysisResult(summary, full);
    }

    public record AnalysisResult(String summary, String fullAnalysis) {}
}
