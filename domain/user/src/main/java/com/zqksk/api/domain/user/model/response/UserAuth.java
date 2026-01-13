package com.zqksk.api.domain.user.model.response;

public record UserAuth(
        Long id,
        Long roleId,
        Long userId
) {
}
