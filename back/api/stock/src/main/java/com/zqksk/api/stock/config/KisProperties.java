package com.zqksk.api.stock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 한국투자증권 Open API 설정.
 * 설정되지 않으면 분석 시 KIS 데이터 없이 제한된 분석만 제공.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "kis")
public class KisProperties {

    /** KIS Open API App Key */
    private String appKey = "";
    /** KIS Open API App Secret */
    private String appSecret = "";
    /** practice | production */
    private String environment = "practice";

    public boolean isConfigured() {
        return appKey != null && !appKey.isBlank() && appSecret != null && !appSecret.isBlank();
    }
}
