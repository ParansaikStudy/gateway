package com.zqksk.api.datasource.dentistry;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DentistryJpaRepository extends JpaRepository<DentistryEntity, Long> {
    Optional<DentistryEntity> findByHospitalId(@Size(max = 15) @NotNull String hospitalId);

    Optional<DentistryEntity> findByHospitalIdAndPgType(@Size(max = 15) @NotNull String hospitalId, @NotNull Integer pgType);

    @Modifying
    @Query("UPDATE DentistryEntity d SET d.sidoName = :sidoName, d.sigunguName = :sigunguName WHERE d.id = :id")
    int updateSidoAndSigunguById(@Param("id") Long id, @Param("sidoName") String sidoName, @Param("sigunguName") String sigunguName);

    @Modifying
    @Query("UPDATE DentistryEntity d SET d.pmsContractState = :pmsContractState, d.pmsJoinDate = :joinDate, d.pmsWithdrawalDate = :pmsWithdrawalDate WHERE d.id = :id")
    int updateContractInfoById(@Param("id") Long id, @Param("pmsContractState") int pmsContractState,
                               @Param("joinDate") LocalDateTime joinDate, @Param("pmsWithdrawalDate") LocalDateTime pmsWithdrawalDate);


}
