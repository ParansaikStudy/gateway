package com.zqksk.api.config;

import lombok.Getter;

@Getter
public enum ApiServer {
    AUTH_SERVER("AUTH-SERVER", "/auth/**", "lb://ZQKSK-AUTH-SERVICE"),
    STOCK_SERVER("STOCK-SERVER", "/api/stock/**", "lb://ZQKSK-STOCK-SERVICE"),
    EXTERNAL_API_SERVER("EXTERNAL-API-SERVER", "/external-api/**", "lb://ZQKSK-EXTERNAL-API-SERVICE"),
    INTERNAL_API_SERVER("INTERNAL-API-SERVER", "/internal-api/**", "lb://ZQKSK-INTERNAL-API-SERVICE"),
    PUSH_API_SERVER("PUSH_API_SERVER", "/push/**", "lb://ZQKSK-PUSH-API-SERVICE");

    private final String id;
    private final String path;
    private final String uri;

    ApiServer(String id, String path, String uri) {
        this.id = id;
        this.path = path;
        this.uri = uri;
    }
}
