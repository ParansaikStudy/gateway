package com.zqksk.api.domain.patient.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreatePatient(
    @NotBlank
    @Schema(description = "환자 ID", example = "1", maxLength = 20, type = "string")
    String uid,

    @NotNull
    @Schema(description = "환자 ID 숫자형", example = "1", type = "number")
    Long patientId,

    @NotBlank
    @Schema(description = "환자명", example = "홍길동", maxLength = 40, type = "string")
    String name,

    @Schema(description = "주민등록번호 앞에서 7자리", example = "9901011", maxLength = 7, type = "string")
    String residentRegistrationNumber,

    @Schema(description = "생년월일 구분 - S: 양력, L: 음력", example = "S", type = "string", allowableValues = {"S", "L"})
    String birthDateDivision,

    @Schema(description = "생년월일", example = "1999-01-01")
    LocalDate birthDate,

    @Schema(description = "휴대폰번호", example = "010-1234-5678", maxLength = 16, type = "string")
    String cellphoneNumber,

    @Schema(description = "전화번호", example = "02-1234-5678", maxLength = 16, type = "string")
    String telephoneNumber,

    @Schema(description = "이메일", example = "zqksk@zqksk.com", maxLength = 100, type = "string")
    String email,

    @Schema(description = "우편번호", example = "05212", maxLength = 6, type = "string")
    String zipCode,

    @Schema(description = "주소", example = "서울시 강남구 역삼동 123-456", maxLength = 100, type = "string")
    String address,

    @Schema(description = "상세주소", example = "2층 201호", maxLength = 100, type = "string")
    String detailAddress,

    @Schema(description = "첫 방문일", example = "2021-01-01")
    LocalDate firstVisitDate,

    @Schema(description = "마지막 방문일", example = "2021-01-01")
    LocalDate lastVisitDate,

    @Schema(description = "의사 ID", example = "1", maxLength = 20, type = "string")
    String doctorId,

    @Schema(description = "치위생사 ID", example = "1", maxLength = 20, type = "string")
    String hygienistId,

    @Schema(description = "보호자 휴대폰번호", example = "010-1234-5678", maxLength = 16, type = "string")
    String protectorCellphoneNumber,

    @Schema(description = "개인정보 동의 여부", example = "Y", maxLength = 1, type = "string", allowableValues = {"Y", "N"})
    String personalInformationAgreeYn,

    @Schema(description = "SMS 수신 여부", example = "Y", maxLength = 1, type = "string", allowableValues = {"Y", "N"})
    String smsReceiveYn,

    @Schema(description = "의료보험 세대주 ID", example = "1", maxLength = 50, type = "string")
    String familyId,

    @Schema(description = "환자 관계 ID", example = "1", maxLength = 20, type = "number")
    Long patientRelationId,

    @Schema(description = "주민등록번호", example = "990101-1234567", maxLength = 24, type = "string")
    String rrn,

    @Schema(description = "방문경로 ID", example = "1", maxLength = 100, type = "string")
    String visitPathId,

    @Schema(description = "환자 이미지 ID", example = "1", type = "number")
    Long imageId,

    @Schema(description = "동의서명 ID", example = "1", maxLength = 20, type = "string")
    String consentSignatureId,

    @Schema(description = "환자차트 일련번호", example = "1", type = "number")
    Long chartSerialNumber,

    @Schema(description = "삭제여부", example = "N", maxLength = 1, type = "string", allowableValues = {"Y", "N"})
    String deletedYn
) {
    public Patient toPatient() {
        return new Patient(
            uid,
            patientId,
            name,
            residentRegistrationNumber,
            birthDateDivision,
            birthDate,
            cellphoneNumber,
            telephoneNumber,
            email,
            zipCode,
            address,
            detailAddress,
            firstVisitDate,
            lastVisitDate,
            doctorId,
            hygienistId,
            protectorCellphoneNumber,
            personalInformationAgreeYn,
            smsReceiveYn,
            familyId,
            patientRelationId,
            rrn,
            visitPathId,
            imageId,
            consentSignatureId,
            chartSerialNumber,
            deletedYn
        );
    }
}
