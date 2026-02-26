package com.zqksk.stock.model;

import java.time.LocalDate;

public record StockPrice(
        LocalDate date,
        double open,
        double high,
        double low,
        double close,
        long volume
) {
    public double changePercent(StockPrice previous) {
        if (previous == null || previous.close() == 0) return 0;
        return ((this.close - previous.close) / previous.close) * 100;
    }

    public String changeDirection(StockPrice previous) {
        double pct = changePercent(previous);
        if (pct > 0) return "▲";
        if (pct < 0) return "▼";
        return "─";
    }
}
