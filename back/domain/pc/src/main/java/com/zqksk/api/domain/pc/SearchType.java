package com.zqksk.api.domain.pc;

public enum SearchType {
    선택(""),
    치과명("hospitalName"),
    요양기관번호("hospitalId"),
    프로그램종류("programType"),
    CPU("cpu"),
    MEMORY("memory"),
    OS("os"),
    설치날짜("lastDateTime");

    private final String value;

    SearchType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SearchType fromValue(String value) {
        for (SearchType type : SearchType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
