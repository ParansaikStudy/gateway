package com.zqksk.api.datasource.patient;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.patient.model.Patient;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PatientEntity extends BaseEntity {

    @Column(unique = true, nullable = false, length = 20)
    @Comment("환자 ID")
    private String uid;

    @Column
    @Comment("환자 ID 숫자형")
    private Long patientId;

    @Column(nullable = false, length = 40)
    @Comment("환자명")
    private String name;

    @Column(length = 7)
    @Comment("주민등록번호 앞에서 7자리")
    private String residentRegistrationNumber;

    @Enumerated(EnumType.STRING)
    @Comment("생년월일 구분 - S: 양력, L: 음력")
    @ColumnDefault("'S'")
    private BirthDataDivision birthDateDivision;

    @Column
    @Comment("생년월일")
    private LocalDate birthDate;

    @Column(length = 16)
    @Comment("휴대폰번호")
    private String cellphoneNumber;

    @Column(length = 16)
    @Comment("전화번호")
    private String telephoneNumber;

    @Column(length = 100)
    @Comment("이메일")
    private String email;

    @Column(length = 6)
    @Comment("우편번호")
    private String zipCode;

    @Column(length = 100)
    @Comment("주소")
    private String address;

    @Column(length = 100)
    @Comment("상세주소")
    private String detailAddress;

    @Column
    @Comment("첫 방문일")
    private LocalDate firstVisitDate;

    @Column
    @Comment("마지막 방문일")
    private LocalDate lastVisitDate;

    @Column(length = 20)
    @Comment("의사 ID")
    private String doctorId;

    @Column(length = 20)
    @Comment("치위생사 ID")
    private String hygienistId;

    @Column(length = 16)
    @Comment("보호자 휴대폰번호")
    private String protectorCellphoneNumber;

    @Column(length = 1)
    @Comment("개인정보 동의 여부")
    private String personalInformationAgreeYn;

    @Column(length = 1)
    @Comment("SMS 수신 여부")
    private String smsReceiveYn;

    @Column(length = 50)
    @Comment("의료보험 세대주 ID")
    private String familyId;

    @Column
    @Comment("환자 관계 ID")
    private Long patientRelationId;

    @Column(length = 24)
    @Comment("주민등록번호")
    private String rrn;

    @Column(length = 100)
    @Comment("방문경로ID")
    private String visitPathId;

    @Column
    @Comment("환자 이미지 ID")
    private Long imageId;

    @Column
    @Comment("동의서명ID")
    private String consentSignatureId;

    @Column
    @Comment("환자차트 일련번호")
    private Long chartSerialNumber;

    @Column(length = 1)
    @Comment("삭제여부")
    @ColumnDefault("'N'")
    private String deletedYn;

    @Builder
    public PatientEntity(String uid, Long patientId, String name, String residentRegistrationNumber, BirthDataDivision birthDateDivision, LocalDate birthDate, String cellphoneNumber, String telephoneNumber, String email, String zipCode, String address, String detailAddress, LocalDate firstVisitDate, LocalDate lastVisitDate, String doctorId, String hygienistId, String protectorCellphoneNumber, String personalInformationAgreeYn, String smsReceiveYn, String familyId, Long patientRelationId, String rrn, String visitPathId, Long imageId, String consentSignatureId, Long chartSerialNumber, String deletedYn) {
        this.uid = uid;
        this.patientId = patientId;
        this.name = name;
        this.residentRegistrationNumber = residentRegistrationNumber;
        this.birthDateDivision = birthDateDivision;
        this.birthDate = birthDate;
        this.cellphoneNumber = cellphoneNumber;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.zipCode = zipCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.firstVisitDate = firstVisitDate;
        this.lastVisitDate = lastVisitDate;
        this.doctorId = doctorId;
        this.hygienistId = hygienistId;
        this.protectorCellphoneNumber = protectorCellphoneNumber;
        this.personalInformationAgreeYn = personalInformationAgreeYn;
        this.smsReceiveYn = smsReceiveYn;
        this.familyId = familyId;
        this.patientRelationId = patientRelationId;
        this.rrn = rrn;
        this.visitPathId = visitPathId;
        this.imageId = imageId;
        this.consentSignatureId = consentSignatureId;
        this.chartSerialNumber = chartSerialNumber;
        this.deletedYn = deletedYn;
    }

    public Patient toPatient() {
        return new Patient(
            uid,
            patientId,
            name,
            residentRegistrationNumber,
            birthDateDivision == null ? null : birthDateDivision.name(),
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
