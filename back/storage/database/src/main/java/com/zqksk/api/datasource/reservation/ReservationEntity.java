package com.zqksk.api.datasource.reservation;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.datasource.registration.PatientDivision;
import com.zqksk.api.domain.reservation.model.Reservation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationEntity extends BaseEntity {
    @Column(nullable = false, length = 20)
    @Comment("환자 ID")
    private String uid;

    @Column(nullable = false)
    private LocalDateTime reservationDate;

    @Column(nullable = false, length = 36)
    private String reservationId;

    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    @Comment("환자 구분 - N: 신환, O: 구환")
    @ColumnDefault("'N'")
    private PatientDivision patientDivision;

    @Column(length = 50)
    @Comment("환자명")
    private String patientName;

    @Column(length = 16)
    @Comment("휴대폰번호")
    private String patientCellPhoneNumber;

    @Column(length = 20)
    @Comment("의사 ID")
    private String doctorId;

    @Column(length = 20)
    @Comment("치위생사 ID")
    private String hygienistId;

    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    @Comment("예약 구분 - 1: 일반예약, 2: 리콜예약, 3: VIP예약, 4: 전화예약, 5: 전화신환, 6: 소개자예약, 7: 신환예약, 8: 네이버예약")
    @ColumnDefault("'GENERAL'")
    private ReservationDivision reservationDivision;

    @Column(length = 2000)
    @Comment("예약 내용")
    private String reservationContents;

    @Column(length = 1)
    @Enumerated(EnumType.STRING)
    @Comment("예약 상태 - 1: 등록, 2: 이행, 3: 취소, 4: 미이행, 5: 예약삭제, 6: 체크, 7: 삭제")
    @ColumnDefault("'REGISTERED'")
    private ReservationStatus reservationStatus;

    @Column
    @Comment("차트 일련번호")
    private String chartSerialNumber;

    @Builder
    public ReservationEntity(String uid, LocalDateTime reservationDate, String reservationId, PatientDivision patientDivision, String patientName, String patientCellPhoneNumber, String doctorId, String hygienistId, ReservationDivision reservationDivision, String reservationContents, ReservationStatus reservationStatus, String chartSerialNumber) {
        this.uid = uid;
        this.reservationDate = reservationDate;
        this.reservationId = reservationId;
        this.patientDivision = patientDivision;
        this.patientName = patientName;
        this.patientCellPhoneNumber = patientCellPhoneNumber;
        this.doctorId = doctorId;
        this.hygienistId = hygienistId;
        this.reservationDivision = reservationDivision;
        this.reservationContents = reservationContents;
        this.reservationStatus = reservationStatus;
        this.chartSerialNumber = chartSerialNumber;
    }

    public Reservation toReservation() {
        return new Reservation(
            uid,
            reservationDate,
            reservationId,
                patientDivision == null ? null : patientDivision.name(),
            patientName,
            patientCellPhoneNumber,
            doctorId,
            hygienistId,
                reservationDivision == null ? null : reservationDivision.name(),
            reservationContents,
                reservationStatus == null ? null : reservationStatus.name(),
            chartSerialNumber
        );
    }
}
