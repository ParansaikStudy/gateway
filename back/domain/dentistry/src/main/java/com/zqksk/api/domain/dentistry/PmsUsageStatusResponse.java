package com.zqksk.api.domain.dentistry;

import lombok.Builder;

@Builder
public record PmsUsageStatusResponse(
        Integer programType,
        int usageCount
) {
}
