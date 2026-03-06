package com.zqksk.api.domain.patient.service;

import com.zqksk.api.domain.patient.model.CreatePatient;
import org.springframework.stereotype.Component;

@Component
public class PatientAppender {
    private final PatientRepository patientRepository;

    public PatientAppender(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public void append(CreatePatient patient) {
        patientRepository.save(patient.toPatient());
    }
}
