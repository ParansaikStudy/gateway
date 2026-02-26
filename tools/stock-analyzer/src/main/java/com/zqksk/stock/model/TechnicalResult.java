package com.zqksk.stock.model;

import java.util.List;

public record TechnicalResult(
        // 이동평균선
        double sma5,
        double sma20,
        double sma50,
        double sma200,
        double ema12,

        // RSI
        double rsi14,
        int rsiTrendDays,       // RSI 추세 일수

        // 볼린저 밴드
        double bollingerUpper,
        double bollingerMiddle,
        double bollingerLower,

        // 지지선 / 저항선
        List<Double> supportLevels,
        List<Double> resistanceLevels,

        // 추세 판단
        TrendDirection shortTermTrend,
        TrendDirection midTermTrend,
        TrendDirection longTermTrend,

        // 차트 패턴
        String chartPattern,
        String patternReliability,

        // 점수
        int technicalScore     // -100 ~ +100
) {
    public enum TrendDirection {
        STRONG_UP("강한 상승"),
        UP("상승"),
        SIDEWAYS("횡보"),
        DOWN("하락"),
        STRONG_DOWN("강한 하락");

        private final String korean;

        TrendDirection(String korean) {
            this.korean = korean;
        }

        public String korean() {
            return korean;
        }
    }

    public String rsiSignal() {
        if (rsi14 >= 70) return "과매수 (매도 신호)";
        if (rsi14 >= 60) return "강세";
        if (rsi14 >= 40) return "중립";
        if (rsi14 >= 30) return "약세";
        return "과매도 (매수 신호)";
    }

    public String rsiTrendDescription() {
        String direction = rsiTrendDays > 0 ? "상승추세" : "하락추세";
        return String.format("%s(%d)", direction, Math.abs(rsiTrendDays));
    }

    public String priceVsSmaDescription(double currentPrice) {
        int aboveCount = 0;
        if (currentPrice > sma5) aboveCount++;
        if (currentPrice > sma20) aboveCount++;
        if (currentPrice > sma50) aboveCount++;
        if (currentPrice > sma200) aboveCount++;

        return switch (aboveCount) {
            case 4 -> "모든 이평선 위 - 강세 구간";
            case 3 -> "주요 이평선 위 - 상승 우위";
            case 2 -> "이평선 혼조 - 방향 탐색 중";
            case 1 -> "대부분 이평선 아래 - 약세 구간";
            default -> "모든 이평선 아래 - 하락 추세";
        };
    }
}
