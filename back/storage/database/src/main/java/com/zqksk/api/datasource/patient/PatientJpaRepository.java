package com.zqksk.api.datasource.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {
    List<PatientEntity> findAllByUidOrNameOrBirthDate(String uid, String name, LocalDate birthDate);
    List<PatientEntity> findAllByUidOrNameLike(String uid, String name);
    List<PatientEntity> findAllByUidOrNameLikeOrBirthDate(String uid, String name, LocalDate birthDate);
}
