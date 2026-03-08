package com.zqksk.api.domain.user.model.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Department(
        Long id,
        String code,
        String name,
        String upperCode,
        String costCenterCode,
        String costCenterName,
        LocalDate effectiveStartDate,
        LocalDate effectiveEndDate,
        String effectiveEndYn,
        Integer departmentLevelOrder,
        Integer sortOrder
) {
}
