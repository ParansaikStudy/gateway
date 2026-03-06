package com.zqksk.api.domain.competitor;

public enum SearchType {
    선택(""),
    시간("workDateTime"),
    요양기관번호("hospitalId"),
    치과명("hospitalName"),
    MAC_주소("macAddress"),
    PC_이름("pcName"),
    프로그램_종류("programType"),
    경쟁사_프로그램("competitorId");

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
