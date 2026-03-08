package com.zqksk.api.stock.model;

import com.zqksk.api.stock.client.KisDailyItem;
import com.zqksk.api.stock.client.KisPriceOutput;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {

    @NotBlank(message = "stockCode는 필수입니다.")
    private String stockCode;

    /** 해외 종목 시 백엔드 조회 시 사용. 거래소코드 (NAS:나스닥, NYS:뉴욕, AMS:아멕스 등) */
    private String exchange;

    /** 시장 구분: "KR" 또는 "US". 해외 종목은 "US"를 전달하여 통화 단위($/원) 결정 */
    private String market;

    /** 프론트에서 KIS로 조회한 현재가. 있으면 백엔드 KIS 설정 없이 분석 가능 */
    private KisPriceOutput price;
    /** 프론트에서 KIS로 조회한 일봉 목록. price와 함께 있으면 백엔드 KIS 호출 생략 */
    private List<KisDailyItem> dailyChart;

    /** 해외 종목 여부 판별 */
    public boolean isOverseas() {
        return "US".equalsIgnoreCase(market) || (exchange != null && !exchange.isBlank());
    }
}
