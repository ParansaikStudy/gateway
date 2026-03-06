package com.zqksk.api.datasource.dentistry;

import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.dentistry.*;
import com.zqksk.api.domain.dentistry.repository.DentistryRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.zqksk.api.datasource.dentistry.QDentistryEntity.dentistryEntity;
import static com.zqksk.api.datasource.pc.QPcEntity.pcEntity;

@Repository
@RequiredArgsConstructor
public class DentistryCoreRepository implements DentistryRepository {

    private final DentistryJpaRepository dentistryJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Dentistry save(NewDentistry newDentistry) {

        DentistryEntity dentistryEntity = dentistryJpaRepository.findByHospitalIdAndPgType(newDentistry.hospitalId(), newDentistry.pgType())
                .orElse(null);

        if(dentistryEntity != null) {
            return dentistryJpaRepository.save(
                    dentistryEntity.update(
                            dentistryEntity.getHospitalId(),
                            newDentistry.hospitalName(),
                            dentistryEntity.getPmsContractState(),
                            dentistryEntity.getProgramMonthlyFee(),
                            dentistryEntity.getPmsRequestAmount(),
                            dentistryEntity.getPmsDepositAmount(),
                            dentistryEntity.getPmsUnpaidCount(),
                            dentistryEntity.getPmsJoinDate(),
                            dentistryEntity.getPmsWithdrawalDate(),
                            dentistryEntity.getSidoName(),
                            dentistryEntity.getSigunguName(),
                            newDentistry.zipCode(),
                            newDentistry.address(),
                            newDentistry.telephone(),
                            newDentistry.doctorCount(),
                            newDentistry.employeeCount(),
                            newDentistry.electronicSignatureYn(),
                            newDentistry.dbUsableCapacity(),
                            newDentistry.businessRegistrationNumber(),
                            dentistryEntity.getClaimYn(),
                            dentistryEntity.getPgType()
                    )
                    ).toDentistry();
        }

        return dentistryJpaRepository.save(DentistryEntity.builder()
                .hospitalId(newDentistry.hospitalId())
                .hospitalName(newDentistry.hospitalName())
                .pmsContractState(newDentistry.pmsContractState())
                .zipCode(newDentistry.zipCode())
                .address(newDentistry.address())
                .telephone(newDentistry.telephone())
                .doctorCount(newDentistry.doctorCount())
                .employeeCount(newDentistry.employeeCount())
                .electronicSignatureYn(newDentistry.electronicSignatureYn())
                .dbUsableCapacity(newDentistry.dbUsableCapacity())
                .businessRegistrationNumber(newDentistry.businessRegistrationNumber())
                .pgType(newDentistry.pgType())
                .build()
        ).toDentistry();
    }

    @Override
    public int updateContractInfo(Dentistry dentistry) {
        return dentistryJpaRepository.updateContractInfoById(
                dentistry.id()
                , dentistry.pmsContractState()
                , dentistry.pmsJoinDate()
                , dentistry.pmsWithdrawalDate()
        );
    }

    @Override
    public int updateSidoAndSigungu(Dentistry dentistry) {
        return dentistryJpaRepository.updateSidoAndSigunguById(dentistry.id(), dentistry.sidoName(), dentistry.sigunguName());
    }

    @Override
    public Dentistry findByHospitalId(String hospitalId) {
        return dentistryJpaRepository.findByHospitalId(hospitalId)
                .map(DentistryEntity::toDentistry)
                .orElse(null);
    }

    @Override
    public List<Dentistry> findAllByCriteria(boolean excludeTest, String searchType, String searchText) {
        return jpaQueryFactory.selectFrom(dentistryEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        containsSearchText(searchType, searchText)
                )
                .fetch()
                .stream()
                .map(DentistryEntity::toDentistry)
                .toList();
    }

    @Override
    public List<Dentistry> findAllByhBusinessRegistrationNumber() {
        return jpaQueryFactory.selectFrom(dentistryEntity)
                .where(dentistryEntity.businessRegistrationNumber.isNotNull())
                .fetch()
                .stream()
                .map(DentistryEntity::toDentistry)
                .toList();
    }

    @Override
    public Long changeClaimYnById(Long id, String claimYn) {
        return jpaQueryFactory.update(dentistryEntity)
                .set(dentistryEntity.claimYn, claimYn)
                .where(dentistryEntity.id.eq(id))
                .execute();
    }

    @Override
    public List<DentistryUsageStatus> findPmsUsageStatus(boolean excludeTest, LocalDate startDate, LocalDate endDate, String searchType, String searchText) {
        return jpaQueryFactory
                .select(
                        Projections.fields(DentistryUsageStatus.class,
                                dentistryEntity.pgType.as("programType"),
                                dentistryEntity.hospitalId.countDistinct().as("usageCount"),
                                new CaseBuilder()
                                        .when(dentistryEntity.pmsContractState.ne(0))
                                        .then(dentistryEntity.hospitalId)
                                        .otherwise((String) null)
                                        .countDistinct()
                                        .as("contractCount")
                        )
                )
                .from(dentistryEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest)
                )
                .groupBy(dentistryEntity.pgType)
                .fetch();
    }

    @Override
    public List<String> findAllHospitalIdList() {
        return jpaQueryFactory.select(dentistryEntity.hospitalId)
                .from(dentistryEntity)
                .fetch();
    }

    @Override
    public Long deleteAllByHospitalIdList(List<String> hospitalIds) {
        return jpaQueryFactory.delete(dentistryEntity)
                .where(dentistryEntity.hospitalId.in(hospitalIds))
                .execute();
    }

    private BooleanExpression hospitalNameNotLike(final boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return dentistryEntity.hospitalName.notLike("오스템%");
    }

    private BooleanExpression hospitalIdNotIn(final boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return dentistryEntity.hospitalId.notIn("00000000","99999999");
    }

    private BooleanExpression pcNameNotLike(final boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return pcEntity.pcName.notLike("OST%");
    }

    private BooleanExpression containsSearchText(final String searchType, final String searchText) {
        if (searchType == null || searchType.isBlank()) {
            return null;
        }

        if (searchText == null || searchText.isBlank()) {
            return null;
        }

        return switch (SearchType.fromValue(searchType)) {
            case 치과명 -> dentistryEntity.hospitalName.contains(searchText);
            case 요양기관번호 -> dentistryEntity.hospitalId.contains(searchText);
            case 프로그램종류 -> dentistryEntity.pgType.eq(ProgramType.getCodeFromString(searchText));
            case 주소 -> dentistryEntity.address.contains(searchText);
            case 전화번호 -> dentistryEntity.telephone.contains(searchText);
            case 계약상태  -> dentistryEntity.pmsContractState.in(PmsContractState.getCodesFromString(searchText));
            case 사업자등록번호 -> dentistryEntity.businessRegistrationNumber.contains(searchText);
            default -> null;
        };
    }

}
