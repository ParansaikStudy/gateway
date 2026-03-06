package com.zqksk.api.model;

import lombok.Getter;

@Getter
public enum CacheType {
    USERS("users", 7200, 200),
    AUTHENTICATION_CODES("authentication_codes", 300, 50),
    VERIFICATION_CODES("verification_codes", 300, 50);

    private final String name;
    private final Integer expireAfterWrite;
    private final Integer maximumSize;

    CacheType(String name, Integer expireAfterWrite, Integer maximumSize) {
        this.name = name;
        this.expireAfterWrite = expireAfterWrite;
        this.maximumSize = maximumSize;
    }
}
