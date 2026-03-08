package com.zqksk.api.domain.patient.service;

import com.zqksk.api.domain.patient.model.Patient;
import com.zqksk.api.domain.patient.util.BirthDateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class PatientFinder {
    private final PatientRepository patientRepository;

    public PatientFinder(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<Patient> getPatients(String patientId, String patientName, String patientBirthDate) {
        return patientRepository.findAllByUidOrNameOrBirthDate(patientId, patientName, BirthDateUtil.convertDate(patientBirthDate));
    }

    public List<Patient> getPatientsByKeyword(String keyword) {
        if(keyword.matches("\\d{6}")) {
            return patientRepository.findAllByUidOrNameOrBirthDate(keyword, keyword, BirthDateUtil.convertDate(keyword));
        }
        return patientRepository.findAllByUidOrNameLike(keyword, keyword+"%");
    }

}
