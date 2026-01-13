package com.zqksk.api.domain.patient.service;

import com.zqksk.api.domain.patient.model.CreatePatient;
import com.zqksk.api.domain.patient.model.Patient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PatientService {
    private final PatientAppender patientAppender;
    private final PatientFinder patientFinder;

    public PatientService(PatientAppender patientAppender, PatientFinder patientFinder) {
        this.patientAppender = patientAppender;
        this.patientFinder = patientFinder;
    }

    public void createPatient(CreatePatient patient) {
        patientAppender.append(patient);
    }

    public List<Patient> getPatients(String uid, String name, String birthDate) {
        return patientFinder.getPatients(uid, name, birthDate);
    }

    public List<Patient> getPatientsByKeyword(String keyword) {
        return patientFinder.getPatientsByKeyword(keyword);
    }
}
