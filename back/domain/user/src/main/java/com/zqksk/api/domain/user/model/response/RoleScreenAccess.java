package com.zqksk.api.domain.user.model.response;

public record RoleScreenAccess(
        Long id,
        Long roleId,
        Long screenId
) {
}
