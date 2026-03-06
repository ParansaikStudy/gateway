package com.zqksk.api.datasource.dentistry;

import com.zqksk.api.datasource.BaseEntity;
import com.zqksk.api.domain.dentistry.Dentistry;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "dentistry")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DentistryEntity extends BaseEntity {
    @Size(max = 15)
    @NotNull
    @Comment("요양기관번호")
    @Column(name = "hospital_id", nullable = false, length = 15)
    private String hospitalId;

    @Size(max = 40)
    @NotNull
    @Comment("병원명")
    @Column(name = "hospital_name", nullable = false, length = 40)
    private String hospitalName;


    @NotNull
    @Comment("PMS 계약 상태")
    @Column(name = "pms_contract_state", nullable = false)
    private Integer pmsContractState;

    @Size(max = 20)
    @Comment("프로그램 월회비")
    @Column(name = "program_monthly_fee", length = 20)
    private String programMonthlyFee;

    @Size(max = 20)
    @Comment("PMS 청구 금액")
    @Column(name = "pms_request_amount", length = 20)
    private String pmsRequestAmount;

    @Size(max = 20)
    @Comment("PMS 입금 금액")
    @Column(name = "pms_deposit_amount", length = 20)
    private String pmsDepositAmount;

    @Comment("PMS 미납 횟수")
    @Column(name = "pms_unpaid_count")
    private Integer pmsUnpaidCount;

    @Comment("PMS 가입일")
    @Column(name = "pms_join_date")
    private LocalDateTime pmsJoinDate;

    @Comment("PMS 탈퇴일")
    @Column(name = "pms_withdrawal_date")
    private LocalDateTime pmsWithdrawalDate;

    @Size(max = 10)
    @Comment("시도명")
    @Column(name = "sido_name", length = 10)
    private String sidoName;

    @Size(max = 10)
    @Comment("시군구명")
    @Column(name = "sigungu_name", length = 10)
    private String sigunguName;

    @Size(max = 6)
    @Comment("우편번호")
    @Column(name = "zip_code", length = 6)
    private String zipCode;

    @Size(max = 100)
    @Comment("주소")
    @Column(name = "address", length = 100)
    private String address;

    @Size(max = 13)
    @Comment("전화번호")
    @Column(name = "telephone", length = 13)
    private String telephone;

    @Comment("의사 수")
    @Column(name = "doctor_count")
    private Integer doctorCount;

    @Comment("직원 수")
    @Column(name = "employee_count")
    private Integer employeeCount;

    @Comment("전자서명여부")
    @Column(name = "electronic_Signature_yn")
    private String electronicSignatureYn;

    @Comment("db가용량")
    @Column(name = "db_usable_capacity")
    private String dbUsableCapacity;

    @Comment("사업자등록번호")
    @Column(name = "business_registration_number")
    private String businessRegistrationNumber;

    @Comment("청구여부")
    @Column(name = "claim_yn")
    private String claimYn;

    @NotNull
    @Column(name = "pg_type", nullable = false)
    @Comment("프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger, 5: OneServer, 6: One2  7" +
            ": One3 ,8: V-ceph, 9: OneClickM, 10: OneDeskM, 11: OnePhoto, 12: OneChartScanM, 13: OneCodiM , 14: DBMigration)")
    private Integer pgType;


    @Builder
    public DentistryEntity(String hospitalId, String hospitalName, Integer pmsContractState, String programMonthlyFee, String pmsRequestAmount,
                           String pmsDepositAmount, Integer pmsUnpaidCount, LocalDateTime pmsJoinDate, LocalDateTime pmsWithdrawalDate,
                           String sidoName, String sigunguName, String zipCode, String address, String telephone, Integer doctorCount, Integer employeeCount,
                           String electronicSignatureYn, String dbUsableCapacity, String businessRegistrationNumber, String claimYn, Integer pgType) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.pmsContractState = pmsContractState;
        this.programMonthlyFee = programMonthlyFee;
        this.pmsRequestAmount = pmsRequestAmount;
        this.pmsDepositAmount = pmsDepositAmount;
        this.pmsUnpaidCount = pmsUnpaidCount;
        this.pmsJoinDate = pmsJoinDate;
        this.pmsWithdrawalDate = pmsWithdrawalDate;
        this.sidoName = sidoName;
        this.sigunguName = sigunguName;
        this.zipCode = zipCode;
        this.address = address;
        this.telephone = telephone;
        this.doctorCount = doctorCount;
        this.employeeCount = employeeCount;
        this.electronicSignatureYn = electronicSignatureYn;
        this.dbUsableCapacity = dbUsableCapacity;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.claimYn = claimYn;
        this.pgType = pgType;
    }

    public DentistryEntity update(String hospitalId, String hospitalName, Integer pmsContractState,
                                  String programMonthlyFee, String pmsRequestAmount, String pmsDepositAmount,
                                  Integer pmsUnpaidCount, LocalDateTime pmsJoinDate, LocalDateTime pmsWithdrawalDate,
                                  String sidoName, String sigunguName, String zipCode, String address, String telephone,
                                  Integer doctorCount, Integer employeeCount, String electronicSignatureYn, String dbUsableCapacity,
                                  String businessRegistrationNumber, String claimYn, Integer pgType) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.pmsContractState = pmsContractState;
        this.programMonthlyFee = programMonthlyFee;
        this.pmsRequestAmount = pmsRequestAmount;
        this.pmsDepositAmount = pmsDepositAmount;
        this.pmsUnpaidCount = pmsUnpaidCount;
        this.pmsJoinDate = pmsJoinDate;
        this.pmsWithdrawalDate = pmsWithdrawalDate;
        this.sidoName = sidoName;
        this.sigunguName = sigunguName;
        this.zipCode = zipCode;
        this.address = address;
        this.telephone = telephone;
        this.doctorCount = doctorCount;
        this.employeeCount = employeeCount;
        this.electronicSignatureYn = electronicSignatureYn;
        this.dbUsableCapacity = dbUsableCapacity;
        this.businessRegistrationNumber = businessRegistrationNumber;
        this.claimYn = claimYn;
        this.pgType = pgType;
        return this;
    }

    public Dentistry toDentistry() {
        return Dentistry.builder()
                .id(super.getId())
                .hospitalId(hospitalId)
                .hospitalName(hospitalName)
                .pmsContractState(pmsContractState)
                .programMonthlyFee(programMonthlyFee)
                .pmsRequestAmount(pmsRequestAmount)
                .pmsDepositAmount(pmsDepositAmount)
                .pmsUnpaidCount(pmsUnpaidCount)
                .pmsJoinDate(pmsJoinDate)
                .pmsWithdrawalDate(pmsWithdrawalDate)
                .sidoName(sidoName)
                .sigunguName(sigunguName)
                .zipCode(zipCode)
                .address(address)
                .telephone(telephone)
                .doctorCount(doctorCount)
                .employeeCount(employeeCount)
                .electronicSignatureYn(electronicSignatureYn)
                .dbUsableCapacity(dbUsableCapacity)
                .businessRegistrationNumber(businessRegistrationNumber)
                .claimYn(claimYn)
                .pgType(pgType)
                .build();
    }
}