package com.zqksk.api.stock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Google Gemini API 설정.
 * 환경변수 GEMINI_API_KEY 또는 application.yml의 gemini.api-key 로 설정.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "gemini")
public class GeminiProperties {

    /** Gemini API Key (Google AI Studio) */
    private String apiKey = "";

    /** 모델명 (models/ 제외). 예: gemini-2.5-flash, gemini-2.0-flash, gemini-2.5-pro */
    private String model = "gemini-2.5-flash";

    public boolean isConfigured() {
        return apiKey == null || apiKey.isBlank();
    }
}
