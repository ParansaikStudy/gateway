package com.zqksk.api.domain.reservation.service;

import com.zqksk.api.domain.reservation.model.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationFinder {
    private final ReservationRepository reservationRepository;

    public ReservationFinder(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getReservations(LocalDate requestDate) {
        LocalDateTime from = requestDate.atStartOfDay();
        LocalDateTime to = requestDate.atTime(23, 59, 59);
        return reservationRepository.findByReservationDateBetween(from, to);
    }
}
