package com.zqksk.api.domain.user.model.response;

import lombok.Builder;

@Builder
public record ApiKey(
    Long id,
    String key,
    String keyName,
    Long userId
) {
}
