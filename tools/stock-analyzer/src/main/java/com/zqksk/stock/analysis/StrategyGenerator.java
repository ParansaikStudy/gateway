package com.zqksk.stock.analysis;

import com.zqksk.stock.model.StockInfo;
import com.zqksk.stock.model.StockPrice;
import com.zqksk.stock.model.StrategyResult;
import com.zqksk.stock.model.TechnicalResult;
import com.zqksk.stock.model.TechnicalResult.TrendDirection;

import java.util.List;

/**
 * 매매 전략 생성기
 * - 추천 점수 산출 (-100 ~ +100)
 * - 분할 매수 구간 설정
 * - 손절/목표가 제안
 * - 리스크 기반 비중 조언
 */
public class StrategyGenerator {

    public StrategyResult generate(StockInfo info, TechnicalResult tech, List<StockPrice> prices) {
        double current = info.currentPrice();

        // ─── 추천 점수 산출 ─────────────────────────────
        int score = calculateRecommendScore(info, tech);
        String label = recommendLabel(score);

        // ─── 목표주가 범위 ──────────────────────────────
        // 단기: 지지선~저항선 기반
        double targetShortLow = !tech.supportLevels().isEmpty()
                ? tech.supportLevels().getFirst() : current * 0.93;
        double targetShortHigh = !tech.resistanceLevels().isEmpty()
                ? tech.resistanceLevels().getFirst() : current * 1.06;

        // 중기: 이평선 기반 + 성장률 반영
        double targetMidLow = Math.max(tech.sma50() * 0.95, targetShortLow * 1.1);
        double targetMidHigh = tech.sma50() > 0
                ? tech.sma50() * 1.25 : current * 1.35;

        // 장기: 52주 고점 기반
        double targetLongLow = current * 1.4;
        double targetLongHigh = info.week52High() > current
                ? info.week52High() * 1.1 : current * 1.8;

        // ─── 분할 매수 구간 ─────────────────────────────
        // 1차: 최근 지지선 근처
        double entry1;
        String entry1Reason;
        if (!tech.supportLevels().isEmpty()) {
            entry1 = tech.supportLevels().getFirst();
            entry1Reason = String.format("(최근 저점 및 강력 지지선). 해당 가격대에서 지지력을 확인하며 비중의 30%% 이내 진입.");
        } else {
            entry1 = round(current * 0.95);
            entry1Reason = "(현재가 대비 -5%% 구간). 단기 조정 시 분할 매수 진입.";
        }

        // 2차: 반전 확인 후
        double entry2;
        String entry2Reason;
        if (tech.ema12() > 0 && tech.ema12() > current) {
            entry2 = round(tech.ema12());
            entry2Reason = String.format("$%.0f 돌파 및 안착 확인 시 (EMA 12 상향 돌파). 추세 반전 신호 확인 후 추가 20~30%% 비중 확대.", tech.ema12());
        } else {
            entry2 = round(current * 1.03);
            entry2Reason = "현재가 대비 +3% 돌파 시. 추세 반전 확인 후 비중 확대.";
        }

        // ─── 손절 / 익절 ────────────────────────────────
        double stopLoss;
        String stopLossReason;
        if (tech.supportLevels().size() >= 2) {
            stopLoss = tech.supportLevels().getLast();
            stopLossReason = String.format("$%.0f(최근 저점) 종가 기준 이탈 시 비중 축소, $%.0f(볼린저 하단) 붕괴 시 전량 매도 고려.",
                    stopLoss, tech.bollingerLower());
        } else {
            stopLoss = round(current * 0.9);
            stopLossReason = String.format("$%.0f(현재가 -10%%) 이탈 시 손절.", stopLoss);
        }

        double tpShort = !tech.resistanceLevels().isEmpty()
                ? tech.resistanceLevels().getFirst() : round(current * 1.08);
        double tpMid = round(targetMidHigh * 0.95);
        double tpLong = round(targetLongLow);

        // ─── 리스크 평가 ────────────────────────────────
        String riskLevel;
        double maxWeight;
        String positionAdvice;

        double beta = info.beta() > 0 ? info.beta() : estimateBeta(prices);

        if (beta > 1.5 || tech.shortTermTrend() == TrendDirection.STRONG_DOWN) {
            riskLevel = "높음";
            maxWeight = 5;
            positionAdvice = String.format("중립 비중 유지. 베타 %.2f의 높은 변동성을 고려하여 전체 포트폴리오의 5~10%% 이내로 제한하며, 시장 급락 시 리스크 관리에 집중해야 합니다.", beta);
        } else if (beta > 1.0 || tech.midTermTrend() == TrendDirection.DOWN) {
            riskLevel = "중간";
            maxWeight = 10;
            positionAdvice = "적정 비중 유지. 포트폴리오의 10~15% 이내 배분을 권고합니다.";
        } else {
            riskLevel = "낮음";
            maxWeight = 20;
            positionAdvice = "적극 비중 가능. 포트폴리오의 15~20% 배분을 고려할 수 있습니다.";
        }

        return new StrategyResult(
                score, label,
                round(targetShortLow), round(targetShortHigh),
                round(targetMidLow), round(targetMidHigh),
                round(targetLongLow), round(targetLongHigh),
                round(entry1), entry1Reason, 30,
                round(entry2), entry2Reason, 25,
                round(stopLoss), stopLossReason,
                round(tpShort), round(tpMid), round(tpLong),
                riskLevel, maxWeight, positionAdvice
        );
    }

    private int calculateRecommendScore(StockInfo info, TechnicalResult tech) {
        int score = tech.technicalScore();

        // 52주 고점 대비 위치 반영
        double fromHigh = info.fromWeek52High();
        if (fromHigh < -40) score += 15;      // 고점 대비 40% 이상 하락 = 기회
        else if (fromHigh < -20) score += 8;
        else if (fromHigh > -5) score -= 5;   // 고점 근처 = 주의

        // 52주 저점 대비
        double fromLow = info.fromWeek52Low();
        if (fromLow < 20) score += 10;        // 저점 근처 = 기회
        else if (fromLow > 200) score -= 10;  // 저점 대비 3배 = 과열 주의

        // PE 반영
        if (info.trailingPE() > 0) {
            if (info.trailingPE() > 100) score -= 15;
            else if (info.trailingPE() > 50) score -= 8;
            else if (info.trailingPE() < 15) score += 10;
        }

        return Math.max(-100, Math.min(100, score));
    }

    private String recommendLabel(int score) {
        if (score >= 60) return "적극 매수 권고";
        if (score >= 30) return "우량한 펀더멘털 기반의 매수 권고";
        if (score >= 10) return "우량한 펀더멘털 기반의 조건부 매수 및 관망";
        if (score >= -10) return "관망 (방향성 확인 필요)";
        if (score >= -30) return "약세 구간, 신규 매수 자제";
        if (score >= -60) return "하락 추세, 비중 축소 권고";
        return "강한 하락 추세, 현금 확보 우선";
    }

    private double estimateBeta(List<StockPrice> prices) {
        if (prices.size() < 30) return 1.0;
        // 간이 변동성 기반 베타 추정
        double sumChange = 0;
        int count = 0;
        for (int i = prices.size() - 30; i < prices.size(); i++) {
            double change = Math.abs(prices.get(i).changePercent(prices.get(i - 1)));
            sumChange += change;
            count++;
        }
        double avgDailyVol = sumChange / count;
        // 시장 평균 일 변동률 ~1% 가정
        return Math.round(avgDailyVol / 1.0 * 100.0) / 100.0;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
