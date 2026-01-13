package com.zqksk.api.domain.user.model.response;

import java.time.LocalDate;

public record Employee(
        Long id,
        String no,
        String name,
        String email,
        String telephone,
        String mobile,
        LocalDate enterDate,
        LocalDate resignDate,
        LocalDate departmentAssignStartDate,
        LocalDate departmentAssignEndDate,
        String departmentCode,
        String dutyCode,
        String businessDutyCode,
        String positionCode,
        String jobGroupCode,
        String statusCode,
        String concurrentPositionStatusYn
) {

}
