package com.zqksk.api.datasource.reservation;

public enum ReservationDivision {
    GENERAL("1"),
    RECALL("2"),
    VIP("3"),
    PHONE("4"),
    PHONE_NEW("5"),
    INTRODUCER("6"),
    NEW("7"),
    NAVER("8");

    private final String name;

    ReservationDivision(String name) {
        this.name = name;
    }

    public static ReservationDivision from(String value) {
        return valueOf(value);
    }
}
