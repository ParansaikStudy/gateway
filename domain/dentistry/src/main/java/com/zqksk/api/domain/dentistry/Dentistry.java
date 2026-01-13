package com.zqksk.api.domain.dentistry;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Dentistry(
        Long id,
        String hospitalId,
        String hospitalName,
        Integer pmsContractState,
        String programMonthlyFee,
        String pmsRequestAmount,
        String pmsDepositAmount,
        Integer pmsUnpaidCount,
        LocalDateTime pmsJoinDate,
        LocalDateTime pmsWithdrawalDate,
        String sidoName,
        String sigunguName,
        String zipCode,
        String address,
        String telephone,
        Integer doctorCount,
        Integer employeeCount,
        String electronicSignatureYn,
        String dbUsableCapacity,
        String businessRegistrationNumber,
        String claimYn,
        Integer pgType
) {
}
