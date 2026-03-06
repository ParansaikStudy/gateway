package com.zqksk.api.domain.common;

import java.util.Arrays;

public enum EventType {
    DEBUG(0),
    INFO(1),
    WARNING(2),
    ERROR(3),
    FATAL(4);

    private final int code;

    EventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static int getCodeFromString(String eventType) {
        try {
            return EventType.valueOf(eventType.toUpperCase()).getCode();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid log level: " + eventType);
        }
    }

    public static String fromCode(int code) {
        return Arrays.stream(EventType.values())
                .filter(eventType -> eventType.getCode() == code)
                .findFirst()
                .map(Enum::name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
