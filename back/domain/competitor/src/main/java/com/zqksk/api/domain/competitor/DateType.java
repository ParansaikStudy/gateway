package com.zqksk.api.domain.competitor;

public enum DateType {
    작업일시("workDateTime"),
    경쟁사프로그램설치날짜("competitorInstallDatetime");

    private final String value;

    DateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DateType fromValue(String value) {
        for (DateType type : DateType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
