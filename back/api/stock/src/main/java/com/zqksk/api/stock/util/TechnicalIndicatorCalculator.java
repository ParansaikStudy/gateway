package com.zqksk.api.stock.util;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Calendar.DAY_OF_WEEK;

/**
 * 주가 시계열로부터 기술적 지표를 계산 (표준 공식).
 * - 이동평균(MA), RSI, MACD, 변동성, 볼린저밴드, 지지/저항
 */
public final class TechnicalIndicatorCalculator {

    private TechnicalIndicatorCalculator() {
    }

    /** n일 단순이동평균: MA(n) = (C1+C2+...+Cn) / n */
    public static double ma(List<Double> closes, int n) {
        if (closes == null || closes.size() < n) return Double.NaN;
        double sum = 0;
        for (int i = closes.size() - n; i < closes.size(); i++) {
            sum += closes.get(i);
        }
        return sum / n;
    }

    /** 지수이동평균 EMA(n): EMA_today = α * close + (1-α) * EMA_yesterday, α = 2/(n+1) */
    public static double ema(List<Double> closes, int n) {
        if (closes == null || closes.isEmpty()) return Double.NaN;
        double alpha = 2.0 / (n + 1);
        double ema = closes.get(0);
        for (int i = 1; i < closes.size(); i++) {
            ema = alpha * closes.get(i) + (1 - alpha) * ema;
        }
        return ema;
    }

    /** RSI(14): RS = AG/AL, RSI = 100 - 100/(1+RS). AG/AL은 14일 평균 상승폭/하락폭. */
    public static double rsi14(List<Double> closes) {
        if (closes == null || closes.size() < 15) return Double.NaN;
        double gains = 0, losses = 0;
        for (int i = closes.size() - 14; i < closes.size(); i++) {
            double change = closes.get(i) - closes.get(i - 1);
            if (change > 0) gains += change;
            else losses += Math.abs(change);
        }
        double avgGain = gains / 2 * DAY_OF_WEEK;
        double avgLoss = losses / 14;
        if (avgLoss == 0) return 100;
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    /** MACD: EMA12 - EMA26, Signal = EMA9(MACD), Histogram = MACD - Signal */
    public static MacdResult macd(List<Double> closes) {
        if (closes == null || closes.size() < 26) return null;
        List<Double> ema12List = new ArrayList<>();
        List<Double> ema26List = new ArrayList<>();
        double a12 = 2.0 / 13, a26 = 2.0 / 27;
        double e12 = closes.get(0), e26 = closes.get(0);
        ema12List.add(e12);
        ema26List.add(e26);
        for (int i = 1; i < closes.size(); i++) {
            e12 = a12 * closes.get(i) + (1 - a12) * e12;
            e26 = a26 * closes.get(i) + (1 - a26) * e26;
            ema12List.add(e12);
            ema26List.add(e26);
        }
        List<Double> macdLine = new ArrayList<>();
        for (int i = 0; i < ema12List.size(); i++) {
            macdLine.add(ema12List.get(i) - ema26List.get(i));
        }
        if (macdLine.size() < 9) return null;
        double a9 = 2.0 / 10;
        double signal = macdLine.get(0);
        for (int i = 1; i < macdLine.size(); i++) {
            signal = a9 * macdLine.get(i) + (1 - a9) * signal;
        }
        int last = macdLine.size() - 1;
        double macdVal = macdLine.get(last);
        double histogram = macdVal - signal;
        return MacdResult.builder()
            .macdLine(macdVal)
            .signalLine(signal)
            .histogram(histogram)
            .build();
    }

    @Getter
    @Builder
    public static class MacdResult {
        private final double macdLine;
        private final double signalLine;
        private final double histogram;
    }

    /** N일 수익률 표준편차(변동성). 수익률 = (close_t - close_t-1)/close_t-1 */
    public static double volatility(List<Double> closes, int n) {
        if (closes == null || closes.size() < n + 1) return Double.NaN;
        List<Double> returns = new ArrayList<>();
        for (int i = closes.size() - n; i < closes.size() - 1; i++) {
            double prev = closes.get(i);
            if (prev == 0) continue;
            returns.add((closes.get(i + 1) - prev) / prev);
        }
        if (returns.isEmpty()) return Double.NaN;
        double mean = returns.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double variance = returns.stream().mapToDouble(r -> Math.pow(r - mean, 2)).average().orElse(0);
        return Math.sqrt(variance) * 100; // % 단위
    }

    /** 볼린저 밴드: 중간=MA20, 상단=MA20+2*σ20, 하단=MA20-2*σ20 */
    public static BollingerResult bollingerBands(List<Double> closes) {
        if (closes == null || closes.size() < 20) return null;
        double mid = ma(closes, 20);
        double sumSq = 0;
        for (int i = closes.size() - 20; i < closes.size(); i++) {
            sumSq += Math.pow(closes.get(i) - mid, 2);
        }
        double std = Math.sqrt(sumSq / 20);
        return BollingerResult.builder()
            .middle(mid)
            .upper(mid + 2 * std)
            .lower(mid - 2 * std)
            .bandwidth((2 * std * 100) / mid)
            .build();
    }

    @Getter
    @Builder
    public static class BollingerResult {
        private final double middle;
        private final double upper;
        private final double lower;
        private final double bandwidth;
    }

    /** N일 최고가/최저가 (지지/저항 수준) */
    public static SupportResistance supportResistance(List<Double> highs, List<Double> lows, int n) {
        if (highs == null || lows == null || highs.size() < n || lows.size() < n) return null;
        double res = highs.stream().skip(highs.size() - (long) n).mapToDouble(Double::doubleValue).max().orElse(0);
        double sup = lows.stream().skip(lows.size() - (long) n).mapToDouble(Double::doubleValue).min().orElse(0);
        return SupportResistance.builder().resistance(res).support(sup).build();
    }

    @Getter
    @Builder
    public static class SupportResistance {
        private final double resistance;
        private final double support;
    }
}
