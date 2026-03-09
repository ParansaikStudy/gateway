package com.zqksk.api.stock.dto.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 탑500 해외 종목별 분석 1건 (cron에서 Gemini 분석 후 저장, 스킬에서 즉시 조회용).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Top500OverseasAnalysisItem {
    private String excd;
    private String symbol;
    /** 사용자에게 리턴하는 3문장 형식 분석 텍스트 */
    private String analysis;
}
