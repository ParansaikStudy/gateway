package com.zqksk.api.domain.patient.model;

import java.time.LocalDate;

public record Patient(
        String uid,
        Long patientId,
        String name,
        String residentRegistrationNumber,
        String birthDateDivision,
        LocalDate birthDate,
        String cellphoneNumber,
        String telephoneNumber,
        String email,
        String zipCode,
        String address,
        String detailAddress,
        LocalDate firstVisitDate,
        LocalDate lastVisitDate,
        String doctorId,
        String hygienistId,
        String protectorCellphoneNumber,
        String personalInformationAgreeYn,
        String smsReceiveYn,
        String familyId,
        Long patientRelationId,
        String rrn,
        String visitPathId,
        Long imageId,
        String consentSignatureId,
        Long chartSerialNumber,
        String deletedYn
) {
}
