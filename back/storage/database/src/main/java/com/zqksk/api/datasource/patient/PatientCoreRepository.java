package com.zqksk.api.datasource.patient;

import com.zqksk.api.domain.patient.model.Patient;
import com.zqksk.api.domain.patient.service.PatientRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PatientCoreRepository implements PatientRepository {
    private final PatientJpaRepository patientJpaRepository;

    public PatientCoreRepository(PatientJpaRepository patientJpaRepository) {
        this.patientJpaRepository = patientJpaRepository;
    }

    @Override
    public void save(Patient patient) {
        PatientEntity entity = new PatientEntity(
                patient.uid(),
                patient.patientId(),
                patient.name(),
                patient.residentRegistrationNumber(),
                patient.birthDateDivision() == null || patient.birthDateDivision().isEmpty() ? null : BirthDataDivision.from(patient.birthDateDivision().toUpperCase()),
                patient.birthDate(),
                patient.cellphoneNumber(),
                patient.telephoneNumber(),
                patient.email(),
                patient.zipCode(),
                patient.address(),
                patient.detailAddress(),
                patient.firstVisitDate(),
                patient.lastVisitDate(),
                patient.doctorId(),
                patient.hygienistId(),
                patient.protectorCellphoneNumber(),
                patient.personalInformationAgreeYn(),
                patient.smsReceiveYn(),
                patient.familyId(),
                patient.patientRelationId(),
                patient.rrn(),
                patient.visitPathId(),
                patient.imageId(),
                patient.consentSignatureId(),
                patient.chartSerialNumber(),
                patient.deletedYn()
        );
        patientJpaRepository.save(entity);
    }

    @Override
    public List<Patient> findAllByUidOrNameOrBirthDate(String id, String name, LocalDate birthDate) {
        List<PatientEntity> patients = patientJpaRepository.findAllByUidOrNameOrBirthDate(id, name, birthDate);

        return patients.stream()
                .map(PatientEntity::toPatient)
                .toList();
    }

    @Override
    public List<Patient> findAllByUidOrNameLike(String id, String name) {
        List<PatientEntity> patientEntities = patientJpaRepository.findAllByUidOrNameLike(id, name);
        return patientEntities.stream()
                .map(PatientEntity::toPatient)
                .toList();
    }

    @Override
    public List<Patient> findAllByUidOrNameLikeOrBirthDate(String id, String name, LocalDate birthDate) {
        List<PatientEntity> patientEntities = patientJpaRepository.findAllByUidOrNameLikeOrBirthDate(id, name, birthDate);
        return patientEntities.stream()
                .map(PatientEntity::toPatient)
                .toList();
    }

}
