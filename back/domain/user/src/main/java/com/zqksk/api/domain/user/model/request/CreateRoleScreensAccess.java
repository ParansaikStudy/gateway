package com.zqksk.api.domain.user.model.request;

import java.util.List;

public record CreateRoleScreensAccess(
        Long roleId,

        List<Long> screenIdList

) {
}
