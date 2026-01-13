package com.zqksk.api.domain.registration.service;

import com.zqksk.api.domain.registration.model.Registration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class RegistrationFinder {
    private final RegistrationRepository registrationRepository;

    public RegistrationFinder(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public List<Registration> getRegistrations(LocalDate registrationDate) {
        LocalDateTime from = registrationDate.atStartOfDay();
        LocalDateTime to = registrationDate.atTime(23, 59, 59);

        return registrationRepository.findByRegistrationDateBetween(from, to);
    }
}
