package com.zqksk.api.model;

public record AuthenticationTokenCache(
        String key,
        String value,
        long ttl
) {
}
