package com.zqksk.api.domain.user.model.request;

import lombok.Builder;

@Builder
public record CreateUser(
        String employeeNo,
        String email,
        String password,
        String name,
        String departmentName,
        String positionName
) {
}
