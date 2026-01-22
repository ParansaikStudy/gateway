package com.zqksk.api.model;

public record AuthenticationRequest(
        String employeeNo,
        String inputCode
) {
}
