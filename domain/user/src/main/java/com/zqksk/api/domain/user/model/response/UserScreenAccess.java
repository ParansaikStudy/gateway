package com.zqksk.api.domain.user.model.response;

public record UserScreenAccess(
        Long id,
        Long userId,
        Long screenId
) {
}
