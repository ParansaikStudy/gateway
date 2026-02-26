package com.zqksk.stock.analysis;

import com.zqksk.stock.model.StockPrice;
import com.zqksk.stock.model.TechnicalResult;
import com.zqksk.stock.model.TechnicalResult.TrendDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 기술적 지표 계산기
 * - SMA (5, 20, 50, 200일)
 * - EMA (12일)
 * - RSI (14일)
 * - 볼린저 밴드 (20일, 2σ)
 * - 지지선 / 저항선
 * - 추세 판단
 * - 차트 패턴 인식
 */
public class TechnicalCalculator {

    public TechnicalResult analyze(List<StockPrice> prices) {
        if (prices.size() < 200) {
            // 200일 미만이면 가능한 범위에서만 계산
        }

        double currentPrice = prices.getLast().close();

        // 이동평균선
        double sma5 = sma(prices, 5);
        double sma20 = sma(prices, 20);
        double sma50 = sma(prices, 50);
        double sma200 = sma(prices, 200);
        double ema12 = ema(prices, 12);

        // RSI
        double rsi14 = rsi(prices, 14);
        int rsiTrendDays = rsiTrend(prices, 14);

        // 볼린저 밴드
        double[] bollinger = bollingerBands(prices, 20, 2.0);

        // 지지선 / 저항선
        List<Double> supports = findSupportLevels(prices, currentPrice);
        List<Double> resistances = findResistanceLevels(prices, currentPrice);

        // 추세 판단
        TrendDirection shortTrend = analyzeTrend(prices, 10, sma5, ema12);
        TrendDirection midTrend = analyzeMidTrend(prices, sma20, sma50);
        TrendDirection longTrend = analyzeLongTrend(prices, sma200);

        // 차트 패턴
        String[] pattern = detectChartPattern(prices, sma20, sma50, sma200);

        // 기술 점수 산출
        int techScore = calculateTechnicalScore(
                currentPrice, sma5, sma20, sma50, sma200,
                rsi14, bollinger, shortTrend, midTrend, longTrend
        );

        return new TechnicalResult(
                round(sma5), round(sma20), round(sma50), round(sma200), round(ema12),
                round(rsi14), rsiTrendDays,
                round(bollinger[0]), round(bollinger[1]), round(bollinger[2]),
                supports, resistances,
                shortTrend, midTrend, longTrend,
                pattern[0], pattern[1],
                techScore
        );
    }

    // ─── SMA ────────────────────────────────────────────────

    private double sma(List<StockPrice> prices, int period) {
        if (prices.size() < period) return 0;
        double sum = 0;
        for (int i = prices.size() - period; i < prices.size(); i++) {
            sum += prices.get(i).close();
        }
        return sum / period;
    }

    // ─── EMA ────────────────────────────────────────────────

    private double ema(List<StockPrice> prices, int period) {
        if (prices.size() < period) return 0;
        double multiplier = 2.0 / (period + 1);
        double emaValue = sma(prices.subList(0, period), period);

        for (int i = period; i < prices.size(); i++) {
            emaValue = (prices.get(i).close() - emaValue) * multiplier + emaValue;
        }
        return emaValue;
    }

    // ─── RSI ────────────────────────────────────────────────

    private double rsi(List<StockPrice> prices, int period) {
        if (prices.size() < period + 1) return 50;

        double gainSum = 0, lossSum = 0;
        for (int i = prices.size() - period; i < prices.size(); i++) {
            double change = prices.get(i).close() - prices.get(i - 1).close();
            if (change > 0) gainSum += change;
            else lossSum += Math.abs(change);
        }

        double avgGain = gainSum / period;
        double avgLoss = lossSum / period;

        if (avgLoss == 0) return 100;
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    private int rsiTrend(List<StockPrice> prices, int period) {
        if (prices.size() < period + 10) return 0;
        double currentRsi = rsi(prices, period);
        // 5일 전 RSI 계산
        List<StockPrice> older = prices.subList(0, prices.size() - 5);
        double olderRsi = rsi(older, period);

        int days = 0;
        if (currentRsi > olderRsi) {
            // 상승 추세 일수 세기
            for (int i = prices.size() - 1; i > prices.size() - 20 && i > period; i--) {
                List<StockPrice> sub = prices.subList(0, i + 1);
                List<StockPrice> subPrev = prices.subList(0, i);
                if (rsi(sub, period) >= rsi(subPrev, period)) days++;
                else break;
            }
        } else {
            for (int i = prices.size() - 1; i > prices.size() - 20 && i > period; i--) {
                List<StockPrice> sub = prices.subList(0, i + 1);
                List<StockPrice> subPrev = prices.subList(0, i);
                if (rsi(sub, period) <= rsi(subPrev, period)) days--;
                else break;
            }
        }
        return days;
    }

    // ─── 볼린저 밴드 ─────────────────────────────────────────

    private double[] bollingerBands(List<StockPrice> prices, int period, double numStd) {
        double middle = sma(prices, period);
        if (prices.size() < period) return new double[]{0, 0, 0};

        double sumSq = 0;
        for (int i = prices.size() - period; i < prices.size(); i++) {
            double diff = prices.get(i).close() - middle;
            sumSq += diff * diff;
        }
        double std = Math.sqrt(sumSq / period);

        return new double[]{
                middle + numStd * std,  // upper
                middle,                  // middle
                middle - numStd * std   // lower
        };
    }

    // ─── 지지선 / 저항선 ─────────────────────────────────────

    private List<Double> findSupportLevels(List<StockPrice> prices, double currentPrice) {
        List<Double> supports = new ArrayList<>();
        int lookback = Math.min(prices.size(), 120);
        List<StockPrice> recent = prices.subList(prices.size() - lookback, prices.size());

        // 1. 최근 저점들 탐색 (피벗 로우)
        for (int i = 2; i < recent.size() - 2; i++) {
            double low = recent.get(i).low();
            if (low < recent.get(i - 1).low() && low < recent.get(i - 2).low()
                    && low < recent.get(i + 1).low() && low < recent.get(i + 2).low()) {
                if (low < currentPrice) {
                    supports.add(round(low));
                }
            }
        }

        // 2. 라운드 넘버 (심리적 지지선)
        double roundBase = Math.pow(10, Math.floor(Math.log10(currentPrice)));
        double roundStep = roundBase >= 100 ? 10 : (roundBase >= 10 ? 5 : 1);
        double roundSupport = Math.floor(currentPrice / roundStep) * roundStep;
        if (roundSupport < currentPrice && !supports.contains(roundSupport)) {
            supports.add(roundSupport);
        }

        // 3. 주요 이평선 근처
        double sma50val = sma(prices, 50);
        double sma200val = sma(prices, 200);
        if (sma50val > 0 && sma50val < currentPrice) supports.add(round(sma50val));
        if (sma200val > 0 && sma200val < currentPrice) supports.add(round(sma200val));

        // 중복 제거 & 정렬 (현재가에 가까운 순)
        supports = supports.stream().distinct()
                .sorted((a, b) -> Double.compare(Math.abs(currentPrice - a), Math.abs(currentPrice - b)))
                .limit(3)
                .sorted(Collections.reverseOrder())
                .toList();

        return supports;
    }

    private List<Double> findResistanceLevels(List<StockPrice> prices, double currentPrice) {
        List<Double> resistances = new ArrayList<>();
        int lookback = Math.min(prices.size(), 120);
        List<StockPrice> recent = prices.subList(prices.size() - lookback, prices.size());

        // 1. 최근 고점들 탐색 (피벗 하이)
        for (int i = 2; i < recent.size() - 2; i++) {
            double high = recent.get(i).high();
            if (high > recent.get(i - 1).high() && high > recent.get(i - 2).high()
                    && high > recent.get(i + 1).high() && high > recent.get(i + 2).high()) {
                if (high > currentPrice) {
                    resistances.add(round(high));
                }
            }
        }

        // 2. 라운드 넘버
        double roundBase = Math.pow(10, Math.floor(Math.log10(currentPrice)));
        double roundStep = roundBase >= 100 ? 10 : (roundBase >= 10 ? 5 : 1);
        double roundResistance = Math.ceil(currentPrice / roundStep) * roundStep;
        if (roundResistance > currentPrice && !resistances.contains(roundResistance)) {
            resistances.add(roundResistance);
        }

        // 3. 주요 이평선 근처
        double sma20val = sma(prices, 20);
        double sma50val = sma(prices, 50);
        if (sma20val > 0 && sma20val > currentPrice) resistances.add(round(sma20val));
        if (sma50val > 0 && sma50val > currentPrice) resistances.add(round(sma50val));

        resistances = resistances.stream().distinct()
                .sorted((a, b) -> Double.compare(Math.abs(currentPrice - a), Math.abs(currentPrice - b)))
                .limit(3)
                .sorted()
                .toList();

        return resistances;
    }

    // ─── 추세 분석 ───────────────────────────────────────────

    private TrendDirection analyzeTrend(List<StockPrice> prices, int days, double sma5, double ema12) {
        if (prices.size() < days + 1) return TrendDirection.SIDEWAYS;
        double current = prices.getLast().close();
        double past = prices.get(prices.size() - days - 1).close();
        double changePct = ((current - past) / past) * 100;

        boolean aboveSma5 = current > sma5;
        boolean aboveEma12 = current > ema12;

        if (changePct > 5 && aboveSma5 && aboveEma12) return TrendDirection.STRONG_UP;
        if (changePct > 1 && (aboveSma5 || aboveEma12)) return TrendDirection.UP;
        if (changePct < -5 && !aboveSma5 && !aboveEma12) return TrendDirection.STRONG_DOWN;
        if (changePct < -1 && (!aboveSma5 || !aboveEma12)) return TrendDirection.DOWN;
        return TrendDirection.SIDEWAYS;
    }

    private TrendDirection analyzeMidTrend(List<StockPrice> prices, double sma20, double sma50) {
        double current = prices.getLast().close();
        boolean aboveSma20 = current > sma20;
        boolean aboveSma50 = current > sma50;
        boolean sma20AboveSma50 = sma20 > sma50;

        // 20일선이 50일선보다 급격히 하회
        if (sma20 > 0 && sma50 > 0) {
            double gap = ((sma20 - sma50) / sma50) * 100;
            if (gap < -5 && !aboveSma20) return TrendDirection.STRONG_DOWN;
            if (gap < -1 && !aboveSma20) return TrendDirection.DOWN;
            if (gap > 5 && aboveSma20) return TrendDirection.STRONG_UP;
            if (gap > 1 && aboveSma20) return TrendDirection.UP;
        }

        if (aboveSma20 && aboveSma50) return TrendDirection.UP;
        if (!aboveSma20 && !aboveSma50) return TrendDirection.DOWN;
        return TrendDirection.SIDEWAYS;
    }

    private TrendDirection analyzeLongTrend(List<StockPrice> prices, double sma200) {
        if (sma200 == 0) return TrendDirection.SIDEWAYS;
        double current = prices.getLast().close();
        double pctFromSma200 = ((current - sma200) / sma200) * 100;

        if (pctFromSma200 > 20) return TrendDirection.STRONG_UP;
        if (pctFromSma200 > 5) return TrendDirection.UP;
        if (pctFromSma200 < -20) return TrendDirection.STRONG_DOWN;
        if (pctFromSma200 < -5) return TrendDirection.DOWN;
        return TrendDirection.SIDEWAYS;
    }

    // ─── 차트 패턴 인식 ──────────────────────────────────────

    private String[] detectChartPattern(List<StockPrice> prices, double sma20, double sma50, double sma200) {
        double current = prices.getLast().close();
        int size = prices.size();

        // 최근 30일 고점/저점 추세
        double recentHigh = Double.MIN_VALUE, recentLow = Double.MAX_VALUE;
        double olderHigh = Double.MIN_VALUE, olderLow = Double.MAX_VALUE;
        int half = Math.min(30, size / 2);

        for (int i = size - half; i < size; i++) {
            recentHigh = Math.max(recentHigh, prices.get(i).high());
            recentLow = Math.min(recentLow, prices.get(i).low());
        }
        for (int i = Math.max(0, size - half * 2); i < size - half; i++) {
            olderHigh = Math.max(olderHigh, prices.get(i).high());
            olderLow = Math.min(olderLow, prices.get(i).low());
        }

        boolean belowAllMa = current < sma20 && current < sma50 && (sma200 == 0 || current < sma200);
        boolean lowerHighs = recentHigh < olderHigh;
        boolean lowerLows = recentLow < olderLow;
        boolean higherLows = recentLow > olderLow;

        // 베어리시 채널 (하락 채널)
        if (lowerHighs && lowerLows && belowAllMa) {
            return new String[]{
                    "베어리시 채널(Bearish Channel) 지속형",
                    "높음(High)"
            };
        }

        // 하락 쐐기 (긍정적 반전 가능)
        if (lowerHighs && higherLows && belowAllMa) {
            return new String[]{
                    "하락 쐐기(Falling Wedge) - 반전 가능성",
                    "중간(Medium)"
            };
        }

        // 이중 바닥 가능성
        if (higherLows && !lowerHighs && current > sma20) {
            return new String[]{
                    "이중 바닥(Double Bottom) 형성 시도",
                    "중간(Medium)"
            };
        }

        // 강세 돌파
        if (current > sma20 && current > sma50 && recentHigh > olderHigh) {
            return new String[]{
                    "상승 돌파(Bullish Breakout) 시도",
                    "중간(Medium)"
            };
        }

        // 바닥 다지기
        if (Math.abs(recentHigh - recentLow) / recentLow * 100 < 10 && belowAllMa) {
            return new String[]{
                    "바닥 다지기(Basing Pattern) 구간",
                    "낮음(Low)"
            };
        }

        // 기본: 하락 추세 조정
        if (belowAllMa) {
            return new String[]{
                    "하락 추세 속 기술적 반등 국면",
                    "낮음(Low)"
            };
        }

        return new String[]{
                "방향 탐색 중 (혼조 구간)",
                "낮음(Low)"
        };
    }

    // ─── 기술 점수 ───────────────────────────────────────────

    private int calculateTechnicalScore(
            double price, double sma5, double sma20, double sma50, double sma200,
            double rsi, double[] bollinger,
            TrendDirection shortT, TrendDirection midT, TrendDirection longT
    ) {
        int score = 0;

        // 이평선 위치 (각 ±10)
        if (price > sma5) score += 5; else score -= 5;
        if (price > sma20) score += 10; else score -= 10;
        if (price > sma50) score += 10; else score -= 10;
        if (sma200 > 0) { if (price > sma200) score += 15; else score -= 15; }

        // RSI 점수 (±15)
        if (rsi >= 30 && rsi <= 70) score += 5;
        if (rsi < 30) score += 15;  // 과매도 = 매수 기회
        if (rsi > 70) score -= 15;  // 과매수 = 위험

        // 볼린저 밴드 (±10)
        if (price < bollinger[2]) score += 10;      // 하단 밑 = 매수 기회
        else if (price > bollinger[0]) score -= 10;  // 상단 위 = 과열

        // 추세 점수 (각 ±10)
        score += trendScore(shortT) * 2;
        score += trendScore(midT) * 3;
        score += trendScore(longT) * 2;

        return Math.max(-100, Math.min(100, score));
    }

    private int trendScore(TrendDirection trend) {
        return switch (trend) {
            case STRONG_UP -> 5;
            case UP -> 3;
            case SIDEWAYS -> 0;
            case DOWN -> -3;
            case STRONG_DOWN -> -5;
        };
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
