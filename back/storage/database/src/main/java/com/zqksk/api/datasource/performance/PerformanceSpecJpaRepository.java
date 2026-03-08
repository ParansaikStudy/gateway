package com.zqksk.api.datasource.performance;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceSpecJpaRepository extends JpaRepository<PerformanceSpecEntity, Long> {
    List<PerformanceSpecEntity> findAllBySoftwareId(@NotNull int softwareId);

    PerformanceSpecEntity findBySoftwareIdAndWorkType(@NotNull int softwareId, int workType);
}
