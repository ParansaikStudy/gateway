package com.zqksk.stock.model;

public record StockInfo(
        String symbol,
        String name,
        String exchange,
        String currency,
        double currentPrice,
        double previousClose,
        double marketCap,
        double week52High,
        double week52Low,
        double beta,
        double trailingPE,
        double forwardPE,
        double operatingMargin,
        double debtToEquity
) {
    public double changePercent() {
        if (previousClose == 0) return 0;
        return ((currentPrice - previousClose) / previousClose) * 100;
    }

    public String changeDirection() {
        double pct = changePercent();
        if (pct > 0) return "▲";
        if (pct < 0) return "▼";
        return "─";
    }

    public double fromWeek52High() {
        if (week52High == 0) return 0;
        return ((currentPrice - week52High) / week52High) * 100;
    }

    public double fromWeek52Low() {
        if (week52Low == 0) return 0;
        return ((currentPrice - week52Low) / week52Low) * 100;
    }

    public String marketCapFormatted() {
        if (marketCap >= 1_000_000_000_000.0) {
            return String.format("%.2f조 달러", marketCap / 1_000_000_000_000.0);
        } else if (marketCap >= 100_000_000.0) {
            return String.format("%.2f억 달러", marketCap / 100_000_000.0);
        }
        return String.format("%.0f 달러", marketCap);
    }
}
