package com.zqksk.api.domain.registration.service;

import com.zqksk.api.domain.registration.model.CreateRegistration;
import com.zqksk.api.domain.registration.model.Registration;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RegistrationService {
    private final RegistrationAppender registrationAppender;
    private final RegistrationFinder registrationFinder;

    public RegistrationService(RegistrationAppender registrationAppender, RegistrationFinder registrationFinder) {
        this.registrationAppender = registrationAppender;
        this.registrationFinder = registrationFinder;
    }

    public void createRegistration(CreateRegistration registration) {
        registrationAppender.append(registration);
    }

    public List<Registration> getRegistrations(LocalDate registrationDate) {
        return registrationFinder.getRegistrations(registrationDate);
    }
}
