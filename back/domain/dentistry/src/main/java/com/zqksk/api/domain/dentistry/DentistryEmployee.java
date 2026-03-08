package com.zqksk.api.domain.dentistry;

import lombok.Builder;

@Builder
public record DentistryEmployee(
        Long id,
        String hospitalId,
        int pgType,
        String employeeNo,
        String employeeName,
        String medicalSpecialty,
        String profile
) {
}
