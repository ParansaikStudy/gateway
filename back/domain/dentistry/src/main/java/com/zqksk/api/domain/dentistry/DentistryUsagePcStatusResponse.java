package com.zqksk.api.domain.dentistry;

import lombok.Builder;

@Builder
public record DentistryUsagePcStatusResponse(
        String programType,
        int usageCount,
        int contractCount
){
}
