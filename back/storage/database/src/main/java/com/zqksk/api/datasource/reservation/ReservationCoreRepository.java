package com.zqksk.api.datasource.reservation;

import com.zqksk.api.datasource.registration.PatientDivision;
import com.zqksk.api.domain.reservation.model.Reservation;
import com.zqksk.api.domain.reservation.service.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationCoreRepository implements ReservationRepository {
    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationCoreRepository(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    public void save(Reservation reservation) {
        ReservationEntity reservationEntity = new ReservationEntity(
                reservation.uid(),
                reservation.reservationDate(),
                reservation.reservationId(),
                reservation.patientDivision() == null || reservation.patientDivision().isEmpty() ? null : PatientDivision.from(reservation.patientDivision().toUpperCase()),
                reservation.patientName(),
                reservation.patientCellPhoneNumber(),
                reservation.doctorId(),
                reservation.hygienistId(),
                reservation.reservationDivision() == null || reservation.reservationDivision().isEmpty() ? null : ReservationDivision.from(reservation.reservationDivision()),
                reservation.reservationContents(),
                reservation.reservationStatus() == null || reservation.reservationStatus().isEmpty() ? null : ReservationStatus.from(reservation.reservationStatus()),
                reservation.chartSerialNumber()
        );

        reservationJpaRepository.save(reservationEntity);
    }

    @Override
    public List<Reservation> findByReservationDateBetween(LocalDateTime from, LocalDateTime to) {
        return reservationJpaRepository.findByReservationDateBetween(from, to).stream()
                .map(ReservationEntity::toReservation)
                .toList();
    }
}
