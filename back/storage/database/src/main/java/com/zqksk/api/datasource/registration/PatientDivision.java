package com.zqksk.api.datasource.registration;

public enum PatientDivision {
    N("N"),
    O("O"),;

    private final String name;

    PatientDivision(String name) {
        this.name = name;
    }

    public static PatientDivision from(String value) {
        return valueOf(value);
    }
}
