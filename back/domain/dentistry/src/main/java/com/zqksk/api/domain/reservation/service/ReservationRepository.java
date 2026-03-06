package com.zqksk.api.domain.reservation.service;

import com.zqksk.api.domain.reservation.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
    void save(Reservation reservation);
    List<Reservation> findByReservationDateBetween(LocalDateTime from, LocalDateTime to);
}
