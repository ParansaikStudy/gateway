package com.zqksk.api.domain.dentistry;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DentistryResponse(
        String hospitalId,
        String hospitalName,
        Integer usedProgram,
        Integer pmsContractState,
        int serverCount,
        int clientCount,
        String region,
        String address,
        String telephone,
        String electronicSignatureYn,
        String dbUsableCapacity,
        int doctorCount,
        int employeeCount,
        LocalDateTime pmsJoinDate,
        LocalDateTime pmsWithdrawalDate,
        String businessRegistrationNumber,
        String claimYn,
        int pgType

) {
}
