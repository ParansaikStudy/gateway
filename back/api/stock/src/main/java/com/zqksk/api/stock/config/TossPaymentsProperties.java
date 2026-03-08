package com.zqksk.api.stock.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "toss.payments")
public class TossPaymentsProperties {

    /** 토스페이먼츠 시크릿 키 (서버 결제 승인용) */
    private String secretKey = "";
    /** 토스페이먼츠 클라이언트 키 (프론트 결제창용, 응답에 포함 가능) */
    private String clientKey = "";

    public boolean isConfigured() {
        return secretKey != null && !secretKey.isBlank();
    }
}
