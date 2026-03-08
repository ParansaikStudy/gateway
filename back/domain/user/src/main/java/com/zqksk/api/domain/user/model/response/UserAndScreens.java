package com.zqksk.api.domain.user.model.response;

import java.util.List;

public record UserAndScreens(
   User user,
   UserAuth userAuth,
   List<RoleScreenAccess> screenList
) {
}
