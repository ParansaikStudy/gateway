package com.zqksk.api.model;

public record ChangePasswordRequest(
        String employeeNo,
        String password,
        String verificationCode
) {
}
