package com.zqksk.api.domain.reservation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateReservation(
    @NotBlank
    @Schema(description = "환자 ID", example = "1", maxLength = 20, type = "string")
    String uid,

    @NotNull
    @Schema(description = "예약 일시", example = "2021-01-01T10:36:28", type = "date")
    LocalDateTime reservationDate,

    @NotBlank
    @Schema(description = "예약 ID", example = "1", maxLength = 36, type = "string")
    String reservationId,

    @Schema(description = "환자 구분 - N: 신환, O: 구환", example = "N", type = "string", allowableValues = {"N", "O"})
    String patientDivision,

    @Schema(description = "환자명", example = "홍길동", maxLength = 50, type = "string")
    String patientName,

    @Schema(description = "휴대폰번호", example = "010-1234-5678", maxLength = 16, type = "string")
    String patientCellPhoneNumber,

    @Schema(description = "의사 ID", example = "1", maxLength = 20, type = "string")
    String doctorId,

    @Schema(description = "치위생사 ID", example = "1", maxLength = 20, type = "string")
    String hygienistId,

    @Schema(description = "예약 구분 - 1: 일반예약, 2: 리콜예약, 3: VIP예약, 4: 전화예약, 5: 전화신환, 6: 소개자예약, 7: 신환예약, 8: 네이버예약", example = "GENERAL", type = "string", allowableValues = {"GENERAL", "RECALL", "VIP", "PHONE", "PHONE_NEW", "INTRODUCER", "NEW_PATIENT", "NAVER"})
    String reservationDivision,

    @Schema(description = "예약 내용", example = "예약 내용", maxLength = 2000, type = "string")
    String reservationContents,

    @Schema(description = "예약 상태 - 1: 등록, 2: 이행, 3: 취소, 4: 미이행, 5: 예약삭제, 6: 체크, 7: 삭제", example = "REGISTERED", type = "string", allowableValues = {"REGISTERED", "FULFILLMENT", "CANCEL", "NOT_FULFILLMENT", "DELETE_RESERVATION", "CHECK", "DELETE"})
    String reservationStatus,

    @Schema(description = "차트 일련번호", example = "1", type = "string")
    String chartSerialNumber
) {
    public Reservation toReservation() {
        return new Reservation(
            uid,
            reservationDate,
            reservationId,
            patientDivision,
            patientName,
            patientCellPhoneNumber,
            doctorId,
            hygienistId,
            reservationDivision,
            reservationContents,
            reservationStatus,
            chartSerialNumber
        );
    }
}
