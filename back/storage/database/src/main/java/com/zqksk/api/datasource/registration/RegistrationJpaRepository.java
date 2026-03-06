package com.zqksk.api.datasource.registration;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistrationJpaRepository extends JpaRepository<RegistrationEntity, Long> {
    List<RegistrationEntity> findByRegistrationDateBetween(LocalDateTime from, LocalDateTime to);
}
