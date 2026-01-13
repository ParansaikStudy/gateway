package com.zqksk.api.domain.user.model.request;

public record NewScreen(
        Long id,
        Long parentId,
        String name,
        String componentName,
        String url
) {
}
