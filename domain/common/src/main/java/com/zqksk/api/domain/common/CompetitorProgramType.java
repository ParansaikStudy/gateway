package com.zqksk.api.domain.common;

import java.util.Arrays;

public enum CompetitorProgramType {
    두번에(0, "두번에"),
    하나로(1, "하나로"),
    ONECLICK(2, "OneClick"),
    덴트웹(3, "덴트웹"),
    앤드윈(4, "앤드윈"),
    제대로(5, "제대로"),
    아이프로(6, "아이프로"),
    OKPEN(7, "OK-Pen");

    private final int code;
    private final String name;

    CompetitorProgramType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static int getCodeFromString(String programType) {
        try {
            return CompetitorProgramType.valueOf(programType.toUpperCase()).getCode();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid workType: " + programType);
        }
    }

    public static String fromCode(int code) {
        return Arrays.stream(CompetitorProgramType.values())
                .filter(programType -> programType.getCode() == code)
                .findFirst()
                .map(CompetitorProgramType::getName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
