package com.zqksk.api.domain.pc;

public enum PerfSpecType {
    고사양("고사양"),
    권장사양("권장사양"),
    최소사양("최소사양"),
    저사양("저사양"),
    미지정("미지정");

    private final String value;

    PerfSpecType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PerfSpecType fromValue(String value) {
        for(PerfSpecType type : PerfSpecType.values()){
            if(type.value.equals(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);

    }

}
