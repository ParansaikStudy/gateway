package com.zqksk.api.domain.user.model.response;

public record Screen(
        Long id,
        Long parentId,
        String name,
        String componentName,
        String url
) {
}
