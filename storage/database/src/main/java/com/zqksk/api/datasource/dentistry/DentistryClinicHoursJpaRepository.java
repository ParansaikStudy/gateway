package com.zqksk.api.datasource.dentistry;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DentistryClinicHoursJpaRepository extends JpaRepository<DentistryClinicHoursEntity, Long> {
    Optional<DentistryClinicHoursEntity> findByHospitalId(@Size(max = 15) @NotNull String hospitalId);
}
