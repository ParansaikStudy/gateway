package com.zqksk.api.datasource.hygienist;

import com.zqksk.api.domain.hygienist.Hygienist;
import com.zqksk.api.domain.hygienist.HygienistRepository;
import org.springframework.stereotype.Repository;

@Repository
public class HygienistCoreRepository implements HygienistRepository {
    private final HygienistJpaRepository hygienistJpaRepository;

    public HygienistCoreRepository(HygienistJpaRepository hygienistJpaRepository) {
        this.hygienistJpaRepository = hygienistJpaRepository;
    }

    @Override
    public void save(Hygienist hygienist) {
        hygienistJpaRepository.save(new HygienistEntity(
                hygienist.hygenistId(),
                hygienist.name(),
                hygienist.licenseNumber(),
                hygienist.hospitalId(),
                hygienist.hospitalName())
        );
    }
}
