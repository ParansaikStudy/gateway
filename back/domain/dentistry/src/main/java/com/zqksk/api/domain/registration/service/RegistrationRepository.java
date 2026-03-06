package com.zqksk.api.domain.registration.service;

import com.zqksk.api.domain.registration.model.Registration;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistrationRepository {
    void save(Registration registration);

    List<Registration> findByRegistrationDateBetween(LocalDateTime from, LocalDateTime to);
}
