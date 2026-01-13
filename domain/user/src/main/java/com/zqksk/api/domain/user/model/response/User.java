package com.zqksk.api.domain.user.model.response;

public record User(
        Long id,
        String employeeNo,
        String name,
        String email,
        String departmentName,
        String positionName
) {
}
