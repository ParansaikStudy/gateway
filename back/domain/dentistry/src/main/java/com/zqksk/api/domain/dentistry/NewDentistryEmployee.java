package com.zqksk.api.domain.dentistry;

import lombok.Builder;

@Builder
public record NewDentistryEmployee(
        String hospitalId,
        int pgType,
        String employeeNo,
        String employeeName,
        String medicalSpecialty,
        byte[] profile
) {
}
