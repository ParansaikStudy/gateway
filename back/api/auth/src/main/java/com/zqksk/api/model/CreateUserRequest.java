package com.zqksk.api.model;

public record CreateUserRequest(
        String employeeNo,
        String password,
        String verificationCode
) {
}
