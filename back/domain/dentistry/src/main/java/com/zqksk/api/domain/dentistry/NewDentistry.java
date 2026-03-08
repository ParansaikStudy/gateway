package com.zqksk.api.domain.dentistry;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NewDentistry(
        String hospitalId,
        String hospitalName,
        Integer pmsContractState,
        String programMonthlyFee,
        String pmsRequestAmount,
        String pmsDepositAmount,
        Integer pmsUnpaidCount,
        LocalDateTime pmsJoinDate,
        LocalDateTime pmsWithdrawalDate,
        String zipCode,
        String address,
        String telephone,
        Integer doctorCount,
        Integer employeeCount,
        String electronicSignatureYn,
        String dbUsableCapacity,
        String businessRegistrationNumber,
        Integer pgType
) {
}
