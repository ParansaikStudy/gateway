package com.zqksk.api.domain.patient.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record RequestPatient(
        @Schema(description = "환자 UID", example = "1", maxLength = 20, type = "string")
        String uid,

        @Schema(description = "환자 이름", example = "1", maxLength = 40, type = "string")
        String name,

        @Schema(description = "환자 생년월일", example = "990101", maxLength = 6, type = "string")
        @Pattern(regexp = "\\d{6}", message = "yyMMdd")
        String birthDate
) {
}
