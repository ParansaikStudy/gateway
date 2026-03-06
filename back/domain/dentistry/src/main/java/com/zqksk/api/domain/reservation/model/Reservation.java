package com.zqksk.api.domain.reservation.model;

import java.time.LocalDateTime;

public record Reservation(
    String uid,
    LocalDateTime reservationDate,
    String reservationId,
    String patientDivision,
    String patientName,
    String patientCellPhoneNumber,
    String doctorId,
    String hygienistId,
    String reservationDivision,
    String reservationContents,
    String reservationStatus,
    String chartSerialNumber
) {
}
