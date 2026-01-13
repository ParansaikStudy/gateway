package com.zqksk.api.domain.reservation.service;

import com.zqksk.api.domain.reservation.model.CreateReservation;
import com.zqksk.api.domain.reservation.model.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationAppender reservationAppender;
    private final ReservationFinder reservationFinder;

    public ReservationService(ReservationAppender reservationAppender, ReservationFinder reservationFinder) {
        this.reservationAppender = reservationAppender;
        this.reservationFinder = reservationFinder;
    }

    public void createReservation(CreateReservation reservation) {
        reservationAppender.append(reservation);
    }

    public List<Reservation> getReservations(LocalDate requestDate) {
        return reservationFinder.getReservations(requestDate);
    }
}
