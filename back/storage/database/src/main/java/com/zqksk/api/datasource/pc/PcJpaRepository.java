package com.zqksk.api.datasource.pc;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PcJpaRepository extends JpaRepository<PcEntity, Long> {
    Optional<PcEntity> findByMacAddress(String macAddress);
    Optional<PcEntity> findByHospitalIdAndMacAddressAndPgType(String hospitalId, String macAddress, int pgType);
}
