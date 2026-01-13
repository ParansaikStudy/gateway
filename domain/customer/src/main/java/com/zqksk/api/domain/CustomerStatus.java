package com.zqksk.api.domain;

import lombok.Getter;

import java.util.List;

@Getter
public enum CustomerStatus {
    ACTIVE(new String[]{"0", "1"}),
    INACTIVE(new String[]{"M", "N", "X", "F"}),
    ALL(new String[]{"0", "1", "F", "M", "N", "X"});

    private List<String> statusCodes;

    CustomerStatus(String[] strings) {
        this.statusCodes = List.of(strings);
    }
}
