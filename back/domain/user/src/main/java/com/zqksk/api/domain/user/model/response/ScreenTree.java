package com.zqksk.api.domain.user.model.response;

import java.util.List;

public record ScreenTree(
        Long id,
        Long parentId,
        String name,
        String componentName,
        String url,
        List<ScreenTree> screens
) {
}
