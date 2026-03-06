package com.zqksk.api.datasource.dentistry;

import com.zqksk.api.domain.dentistry.DentistryEmployee;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DentistryEmployeeJpaRepository extends JpaRepository<DentistryEmployeeEntity, Long> {
    Optional<DentistryEmployeeEntity> findByHospitalIdAndPgTypeAndEmployeeNo(@Size(max = 15) @NotNull String hospitalId, int pgType, String employeeNo);

    List<DentistryEmployeeEntity> findAllByHospitalId(String hospitalId);
}
