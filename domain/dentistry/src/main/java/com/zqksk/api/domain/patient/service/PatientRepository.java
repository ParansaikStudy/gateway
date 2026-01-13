package com.zqksk.api.domain.patient.service;

import com.zqksk.api.domain.patient.model.Patient;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepository {
    void save(Patient patient);
    List<Patient> findAllByUidOrNameOrBirthDate(String id, String name, LocalDate birthDate);
    List<Patient> findAllByUidOrNameLike(String id, String name);
    List<Patient> findAllByUidOrNameLikeOrBirthDate(String id, String name, LocalDate birthDate);
}
