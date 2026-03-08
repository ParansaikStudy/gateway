package com.zqksk.api.domain.common;

public enum SearchType {
    선택(""),
    ID("id"),
    시간("workDateTime"),
    프로그램("programTypeName"),
    프로그램_종류("workTypeName"),
    버전("version"),
    메뉴("rootMenu"),
    화면ID("viewId"),
    화면명("viewName"),
    로그("log"),
    로그_상세("logDetail"),
    로그_기타("etc"),
    요양기관번호("hospitalId"),
    병원명("hospitalName"),
    PC명("pcName");

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
