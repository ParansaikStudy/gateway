package com.zqksk.stock.model;

public record StrategyResult(
        // 추천 점수
        int recommendScore,          // -100 ~ +100
        String recommendLabel,       // "조건부 매수 및 관망" 등

        // 목표주가
        double targetShortTermLow,   // 단기 목표 하단
        double targetShortTermHigh,  // 단기 목표 상단
        double targetMidTermLow,     // 중기 목표 하단
        double targetMidTermHigh,    // 중기 목표 상단
        double targetLongTermLow,    // 장기 목표 하단
        double targetLongTermHigh,   // 장기 목표 상단

        // 분할 매수
        double entry1Price,          // 1차 진입가
        String entry1Reason,
        double entry1WeightPercent,  // 1차 비중 %
        double entry2Price,          // 2차 진입가
        String entry2Reason,
        double entry2WeightPercent,  // 2차 비중 %

        // 손절 / 익절
        double stopLossPrice,
        String stopLossReason,
        double takeProfitShort,
        double takeProfitMid,
        double takeProfitLong,

        // 리스크
        String riskLevel,            // "높음", "중간", "낮음"
        double maxPortfolioWeight,   // 최대 포트폴리오 비중 %
        String positionAdvice        // 포지션 조언
) {
    public String entryStrategyDescription(double currentPrice) {
        if (recommendScore >= 50) {
            return String.format("현재 가격($%.2f)에서 적극적 매수를 권고합니다. 기술적·펀더멘털 모두 긍정적입니다.", currentPrice);
        } else if (recommendScore >= 20) {
            return String.format("현재 가격($%.2f)에서는 관망 또는 소량 분할 매수만 권고합니다. 기술적 하락 추세가 진행 중이므로 바닥 확인이 우선입니다.", currentPrice);
        } else if (recommendScore >= -20) {
            return String.format("현재 가격($%.2f)에서는 관망을 권고합니다. 추가 하락 가능성이 있으며, 지지선 확인 후 진입을 고려하세요.", currentPrice);
        } else {
            return String.format("현재 가격($%.2f)에서 매수는 위험합니다. 명확한 반등 신호가 나올 때까지 현금 보유를 권고합니다.", currentPrice);
        }
    }
}
