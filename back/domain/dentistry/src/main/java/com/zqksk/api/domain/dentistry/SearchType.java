package com.zqksk.api.domain.dentistry;

public enum SearchType {
    선택(""),
    치과명("hospitalName"),
    요양기관번호("hospitalId"),
    프로그램종류("programType"),
    주소("address"),
    전화번호("telephone"),
    계약상태("pmsContractState"),
    사업자등록번호("businessRegistrationNumber");

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
