package com.zqksk.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "open-zqksk")
public record OpenZqkskProperties(
        String queryId,
        String queryPassword,
        String scheme,
        String host,
        String path
) {
}
