package com.zqksk.api.model;

import lombok.Getter;

@Getter
public enum AuthorizedCookie {
    ACCESS_TOKEN("oat"),
    REFRESH_TOKEN("ort");

    private final String cookieName;

    AuthorizedCookie(String value) {
        this.cookieName = value;
    }
}
