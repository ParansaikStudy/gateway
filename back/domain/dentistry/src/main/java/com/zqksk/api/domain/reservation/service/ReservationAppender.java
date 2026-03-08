package com.zqksk.api.domain.reservation.service;

import com.zqksk.api.domain.reservation.model.CreateReservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationAppender {
    private final ReservationRepository reservationRepository;

    public ReservationAppender(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public void append(CreateReservation createReservation) {
        reservationRepository.save(createReservation.toReservation());
    }
}
