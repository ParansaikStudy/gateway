package com.zqksk.api.stock.define.indecies;

import lombok.Getter;

import java.util.List;

@Getter
public enum RSI implements Indecies {

    RSI15(15),
    ;

    private final int period;

    RSI(int period) {
        this.period = period;
    }

    @Override
    public Double calculate(List<Double> closes) {
        if (closes == null || closes.size() < 15) return Double.NaN;
        double gains = 0, losses = 0;
        for (int i = closes.size() - 14; i < closes.size(); i++) {
            double change = closes.get(i) - closes.get(i - 1);
            if (change > 0) gains += change;
            else losses += Math.abs(change);
        }
        double avgGain = gains / 14;
        double avgLoss = losses / 14;
        if (avgLoss == 0) return 100D;
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }
}
