package com.zqksk.api.domain.registration.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateRegistration(
    @NotBlank
    @Schema(description = "환자 ID", example = "1", maxLength = 20, type = "string")
    String uid,

    @NotNull
    @Schema(description = "접수 일시", example = "2021-01-01T10:36:28", type = "date")
    LocalDateTime registrationDate,

    @NotBlank
    @Schema(description = "접수 ID", example = "1", maxLength = 36, type = "string")
    String registrationId,

    @Schema(description = "접수 순번", example = "1", type = "number")
    Long registrationSequence,

    @Schema(description = "환자 구분 - N: 신환, O: 구환", example = "N", type = "string", allowableValues = {"N", "O"})
    String patientDivision,

    @Schema(description = "환자 상태", example = "1", type = "number")
    Long patientStatus,

    @Schema(description = "치과별 구분값", example = "7779999996123456", maxLength = 16, type = "number")
    Long dentalId,

    @Schema(description = "의사 ID", example = "1", maxLength = 20, type = "string")
    String doctorId,

    @Schema(description = "치위생사 ID", example = "1", maxLength = 20, type = "string")
    String hygienistId,

    @Schema(description = "체어 ID", example = "1", type = "number")
    Long chairId,

    @Schema(description = "예약 일시", example = "2024-05-01 10:36:28")
    LocalDateTime reservationDate,

    @Schema(description = "접수 상태", example = "1", type = "string")
    String registrationStatus
) {
    public Registration toRegistration() {
        return new Registration(
            uid,
            registrationDate,
            registrationId,
            registrationSequence,
            patientDivision,
            patientStatus,
            dentalId,
            doctorId,
            hygienistId,
            chairId,
            reservationDate,
            registrationStatus
        );
    }
}
