package com.zqksk.api.datasource.cpu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CpuJpaRepository extends JpaRepository<CpuEntity, Long> {
    List<CpuEntity> findAllByBrand(String brand);
}
