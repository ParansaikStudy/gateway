package com.zqksk.api.stock.analysis;

import com.zqksk.api.stock.client.KisDailyItem;
import com.zqksk.api.stock.client.KisPriceOutput;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/** 분석에 사용된 원시 데이터 및 계산된 지표. */
@Getter
@Builder
public class AnalysisData {

    private final KisPriceOutput price;
    private final List<KisDailyItem> dailyItems;
    private final List<Double> closes;
    private final List<Double> highs;
    private final List<Double> lows;

    private final Double ma5;
    private final Double ma20;
    private final Double ma60;
    private final Double rsi;
    private final TechnicalIndicatorCalculator.MacdResult macd;
    private final Double volatility20;
    private final TechnicalIndicatorCalculator.BollingerResult bollinger;
    private final TechnicalIndicatorCalculator.SupportResistance supportResistance;
}
