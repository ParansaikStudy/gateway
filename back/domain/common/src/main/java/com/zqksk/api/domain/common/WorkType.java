package com.zqksk.api.domain.common;

import java.util.Arrays;

public enum WorkType {
    SERVER(0),
    CLIENT(1);

    private final int code;

    WorkType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static int getCodeFromString(String workType) {
        try {
            return WorkType.valueOf(workType.toUpperCase()).getCode();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid workType: " + workType);
        }
    }

    public static String fromCode(int code) {
        return Arrays.stream(WorkType.values())
                .filter(workType -> workType.getCode() == code)
                .findFirst()
                .map(Enum::name)
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
