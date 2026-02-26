package com.zqksk.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kis")
public record KisProperties(
    String appKey,
    String appSecret,
    String environment
) {
    public static final String BASE_URL_PRACTICE = "https://openapivts.koreainvestment.com:29443";
    public static final String BASE_URL_PRODUCTION = "https://openapi.koreainvestment.com:9443";

    public String getBaseUrl() {
        return "production".equalsIgnoreCase(environment) ? BASE_URL_PRODUCTION : BASE_URL_PRACTICE;
    }
}
