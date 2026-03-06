package com.zqksk.api.domain.common;

import java.util.Arrays;

public enum AccessLogEventType {
    기본(0),
    전환속도(11),
    동기화(12);

    private final int code;

    AccessLogEventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static int getCodeFromString(String eventType) {
        try {
            return AccessLogEventType.valueOf(eventType.toUpperCase()).getCode();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid log level: " + eventType);
        }
    }

    public static String fromCode(int code) {
        return Arrays.stream(AccessLogEventType.values())
                .filter(eventType -> eventType.getCode() == code)
                .findFirst()
                .map(Enum::name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
