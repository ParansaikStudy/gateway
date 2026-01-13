package com.zqksk.api.domain.common;

import java.util.Arrays;

public enum ProgramType {
    두번에(0, "두번에"),
    하나로(1, "하나로"),
    ONECLICK(2, "OneClick"),
    ONECODI(3, "OneCodi"),
    ONEMESSENGER(4, "OneMessenger"),
    ONESERVER(5, "OneServer"),
    ONE2(6, "One2"),
    ONE3(7, "One3"),
    V_CEPH(8, "V-ceph"),
    ONECLICKM(9, "OneClickM"),
    ONEDESKM(10, "OneDeskM"),
    ONEPHONE(11, "OnePhone"),
    ONECHARTSCANM(12, "OneChartScanM"),
    ONECODIM(13, "OneCodiM"),
    DBMIGRATION(14, "DBMigration");

    private final int code;
    private final String name;

    ProgramType(int code, String name) {
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
            return ProgramType.valueOf(programType.toUpperCase()).getCode();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid workType: " + programType);
        }
    }

    public static String fromCode(int code) {
        return Arrays.stream(ProgramType.values())
                .filter(programType -> programType.getCode() == code)
                .findFirst()
                .map(ProgramType::getName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid code: " + code));
    }
}
