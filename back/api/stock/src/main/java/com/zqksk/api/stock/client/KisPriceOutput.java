package com.zqksk.api.stock.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * KIS 국내주식 현재가 API 응답 output (inquire-price).
 * API에서 알 수 없는 필드(iscd_stat_cls_code 등)는 무시.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KisPriceOutput {

    /** 현재가 */
    private String stck_prpr;
    /** 전일 대비 */
    private String prdy_vrss;
    /** 전일 대비 부호 (1:상한 2:상승 3:보합 4:하한 5:하락) */
    private String prdy_vrss_sign;
    /** 전일 대비율 */
    private String prdy_ctrt;
    /** 시가 */
    private String stck_oprc;
    /** 고가 */
    private String stck_hgpr;
    /** 저가 */
    private String stck_lwpr;
    /** 누적 거래량 */
    private String acml_vol;
    /** 누적 거래대금 */
    @JsonProperty("acml_tr_pbmn")
    private String acmlTrPbmn;
    /** 시가총액 */
    private String hts_avls;
    /** PER */
    private String per;
    /** PBR */
    private String pbr;
    /** EPS */
    private String eps;
    /** 상한가 */
    private String stck_mxpr;
    /** 하한가 */
    private String stck_llam;
    /** 종목명 (KIS 현재가 API 응답에 있으면 채워짐) */
    private String prdt_name;
}
