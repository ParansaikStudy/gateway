package com.zqksk.api.datasource.reservation;

public enum ReservationStatus {
    REGISTERED("1"),
    FULFILLMENT("2"),
    CANCEL("3"),
    NOT_FULFILLMENT("4"),
    DELETE_RESERVATION("5"),
    CHECK("6"),
    DELETE("7");

    private final String name;

    ReservationStatus(String name) {
        this.name = name;
    }

    public static ReservationStatus from(String value) {
        return valueOf(value);
    }
}
