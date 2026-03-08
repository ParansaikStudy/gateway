package com.zqksk.api.datasource.patient;

public enum BirthDataDivision {
    /**
     * 음력
     */
    L("L"),

    /**
     * 양력
     */
    S("S");

    private final String name;

    BirthDataDivision(String name) {
        this.name = name;
    }

    public static BirthDataDivision from(String value) {
        return valueOf(value);
    }
}
