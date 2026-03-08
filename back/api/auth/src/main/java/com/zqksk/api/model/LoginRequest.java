package com.zqksk.api.model;

public record LoginRequest(
        String employeeNo,
        String password
) {
}
