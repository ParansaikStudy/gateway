package com.zqksk.api.stock.define.indecies;

import lombok.Getter;

import java.util.List;

@Getter
public enum MA implements Indecies {

    MA5(5, "5일선 %s(현재가 대비 %.1f%%), "),
    MA15(15, "20일선 %s(현재가 대비 %.1f%%), "),
    MA20(20, "20일선 %s(현재가 대비 %.1f%%), "),
    MA30(20, "20일선 %s(현재가 대비 %.1f%%), "),
    MA60(60, "20일선 %s(현재가 대비 %.1f%%), "),
    ;

    private final int period;
    private final String AnalyticMessage;

    MA(int score, String analyticMessage) {
        this.period = score;
        AnalyticMessage = analyticMessage;
    }

    @Override
    public Double calculate(List<Double> closes) {
        if (closes == null || closes.size() < period) {
            return null;
        }

        double sum = 0;
        for (int i = closes.size() - period; i < closes.size(); i++) {
            sum += closes.get(i);
        }

        return sum / period;
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
}
