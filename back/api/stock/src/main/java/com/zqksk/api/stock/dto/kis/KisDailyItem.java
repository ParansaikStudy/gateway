package com.zqksk.api.stock.dto.kis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * KIS 일봉 차트 한 건 (inquire-daily-itemchartprice output2).
 * API에서 알 수 없는 필드(acml_tr_pbmn 등)는 무시.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KisDailyItem {

    /** 영업일자 (YYYYMMDD) */
    private String stck_bsop_date;
    /** 종가 */
    private String stck_clpr;
    /** 시가 */
    private String stck_oprc;
    /** 고가 */
    private String stck_hgpr;
    /** 저가 */
    private String stck_lwpr;
    /** 거래량 */
    private String acml_vol;
    /** 전일 대비 */
    private String prdy_vrss;
    /** 전일 대비 부호 */
    private String prdy_vrss_sign;
}
