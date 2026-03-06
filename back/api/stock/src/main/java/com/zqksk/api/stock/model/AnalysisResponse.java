package com.zqksk.api.stock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프론트엔드 KisAnalysisResponse와 동일 형식.
 * summary: 50자 이하 요약, fullAnalysis: 전체 분석 문장, conclusion: 사용자용 결론(압력·상황·매도/매수)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResponse {

    private String summary;
    private String fullAnalysis;
    /** 사용자용 결론: "지금 {주식명(티커)}은 ~ / 단기선~ / 매도 손실 ~%, 매수 반등 ~%" */
    private String conclusion;
}
