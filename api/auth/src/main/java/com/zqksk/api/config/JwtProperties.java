package com.zqksk.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        long accessExpirationTime,
        long refreshExpirationTime,
        String keyPrefix
) {
}
