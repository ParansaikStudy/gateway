package com.zqksk.api.datasource.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {
    List<ReservationEntity> findByReservationDateBetween(LocalDateTime from, LocalDateTime to);
}
