package com.zqksk.api.domain.user.model.response;

import java.util.List;

public record UserPrivileges(
        User user,
        List<Role> roles,
        List<ScreenTree> screens
) {
}
