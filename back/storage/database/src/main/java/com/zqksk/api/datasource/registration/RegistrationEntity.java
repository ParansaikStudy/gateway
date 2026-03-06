package com.zqksk.api.datasource.registration;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.registration.model.Registration;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RegistrationEntity extends BaseEntity {

    @Column(nullable = false, length = 20)
    @Comment("환자 ID")
    private String uid;

    @Column(nullable = false)
    @Comment("접수 일시")
    private LocalDateTime registrationDate;

    @Column(nullable = false, length = 36)
    @Comment("접수 ID")
    private String registrationId;

    @Column
    @Comment("접수 순번")
    private Long registrationSequence;

    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    @Comment("환자 구분 - N: 신환, O: 구환")
    @ColumnDefault("'N'")
    private PatientDivision patientDivision;

    @Column
    @Comment("환자 상태")
    private Long patientStatus;

    @Column
    @Comment("치과별 구분값")
    private Long dentalId;

    @Column(length = 20)
    @Comment("의사 ID")
    private String doctorId;

    @Column(length = 20)
    @Comment("치위생사 ID")
    private String hygienistId;

    @Column
    @Comment("체어 ID")
    private Long chairId;

    @Column
    @Comment("예약 일시")
    private LocalDateTime reservationDate;

    @Column
    @Comment("접수 상태")
    private String registrationStatus;

    @Builder
    public RegistrationEntity(String uid, LocalDateTime registrationDate, String registrationId, Long registrationSequence, PatientDivision patientDivision, Long patientStatus, Long dentalId, String doctorId, String hygienistId, Long chairId, LocalDateTime reservationDate, String registrationStatus) {
        this.uid = uid;
        this.registrationDate = registrationDate;
        this.registrationId = registrationId;
        this.registrationSequence = registrationSequence;
        this.patientDivision = patientDivision;
        this.patientStatus = patientStatus;
        this.dentalId = dentalId;
        this.doctorId = doctorId;
        this.hygienistId = hygienistId;
        this.chairId = chairId;
        this.reservationDate = reservationDate;
        this.registrationStatus = registrationStatus;
    }

    public Registration toRegistration() {
        return new Registration(
                uid,
                registrationDate,
                registrationId,
                registrationSequence,
                patientDivision == null ? null : patientDivision.name(),
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
