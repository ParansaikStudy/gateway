package com.zqksk.api.domain.pc;

import lombok.Builder;

@Builder
public record PmsUsagePcStatusResponse (
        String programType,
        int usageCount
){
}
