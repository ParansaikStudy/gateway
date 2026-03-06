package com.zqksk.api.domain.user.model;

public record CredentialEmployee(
        String employeeNo,
        String password,
        String openZqkskLogin
) {
}
