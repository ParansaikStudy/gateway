package com.zqksk.api.domain.common;

import java.util.Arrays;

public enum Action {
    CLICK(0, "Click"),
    SEARCH(1, "Search"),
    ADD(2, "Add"),
    REMOVE(3, "Remove"),
    MODIFY(4, "Modify"),
    PUBSUB(5, "Pub/Sub");

    private final int code;
    private final String name;

    Action(int code, String name) {
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
            return Action.valueOf(programType.toUpperCase()).getCode();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid workType: " + programType);
        }
    }

    public static String fromCode(int code) {
        return Arrays.stream(Action.values())
                .filter(programType -> programType.getCode() == code)
                .findFirst()
                .map(Action::getName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
