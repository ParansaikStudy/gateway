package com.zqksk.api.domain.registration.model;

import java.time.LocalDateTime;

public record Registration(
    String uid,
    LocalDateTime registrationDate,
    String registrationId,
    Long registrationSequence,
    String patientDivision,
    Long patientStatus,
    Long dentalId,
    String doctorId,
    String hygienistId,
    Long chairId,
    LocalDateTime reservationDate,
    String registrationStatus
) {
}