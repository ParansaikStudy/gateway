package com.zqksk.api.stock.dto.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 해외 탑100 종목 거래소·심볼 (배치에서 저장, 카카오 스킬에서 해외 종목 검색용).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Top100OverseasNameCodeItem {
    /** 거래소코드 (NAS, NYS, AMS 등) */
    private String excd;
    /** 종목 심볼 (AAPL, NVDA 등) */
    private String symbol;
}
