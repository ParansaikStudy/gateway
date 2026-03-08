package com.zqksk.api.domain.registration.service;

import com.zqksk.api.domain.registration.model.CreateRegistration;
import org.springframework.stereotype.Component;

@Component
public class RegistrationAppender {
    private final RegistrationRepository registrationRepository;

    public RegistrationAppender(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    public void append(CreateRegistration registration) {
        registrationRepository.save(registration.toRegistration());
    }
}
