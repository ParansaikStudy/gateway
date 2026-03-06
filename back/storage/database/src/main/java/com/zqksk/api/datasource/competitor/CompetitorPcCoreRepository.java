package com.zqksk.api.datasource.competitor;

import com.zqksk.api.domain.common.CompetitorProgramType;
import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.competitor.*;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.zqksk.api.datasource.competitor.QCompetitorPcEntity.competitorPcEntity;
@Repository
@RequiredArgsConstructor
public class CompetitorPcCoreRepository implements CompetitorPcRepository {
    private final CompetitorPcJpaRepository competitorPcJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public CompetitorPc save(final NewCompetitorPc newCompetitorPc) {

        CompetitorPcEntity competitorPc = jpaQueryFactory.selectFrom(competitorPcEntity)
                .where(hospitalIdEq(newCompetitorPc.hospitalId()),
                        pgTypeEq(newCompetitorPc.pgType()),
                        competitorIdEq(newCompetitorPc.competitorId()),
                        macAddressEq(newCompetitorPc.macAddress()))
                .fetchFirst();

        if(competitorPc != null){

            return competitorPcJpaRepository.save(
                    competitorPc.update(
                            competitorPc.getHospitalId(),
                            newCompetitorPc.hospitalName(),
                            newCompetitorPc.ip(),
                            competitorPc.getMacAddress(),
                            newCompetitorPc.pcName(),
                            competitorPc.getPgType(),
                            competitorPc.getCompetitorId(),
                            newCompetitorPc.competitorInstallDatetime(),
                            newCompetitorPc.workDatetime()
                    )

            ).toCompetitorPc();
        }

        return competitorPcJpaRepository.save(
                CompetitorPcEntity.builder()
                        .hospitalId(newCompetitorPc.hospitalId())
                        .hospitalName(newCompetitorPc.hospitalName())
                        .ip(newCompetitorPc.ip())
                        .macAddress(newCompetitorPc.macAddress())
                        .pcName(newCompetitorPc.pcName())
                        .pgType(newCompetitorPc.pgType())
                        .competitorId(newCompetitorPc.competitorId())
                        .competitorInstallDate(newCompetitorPc.competitorInstallDatetime())
                        .workDatetime(newCompetitorPc.workDatetime())
                        .build()
        ).toCompetitorPc();
    }
    @Override
    public List<CompetitorPc> findAll() {
        List<CompetitorPcEntity> competitorPcList = competitorPcJpaRepository.findAll();
        return competitorPcList.stream()
                .map(CompetitorPcEntity::toCompetitorPc)
                .toList();
    }
    @Override
    public List<CompetitorPc> findAllWithCondition(String dateTypeName, LocalDate startDate, LocalDate endDate, List<Integer> pgTypeList, String searchType, String searchText) {
        return jpaQueryFactory.selectFrom(competitorPcEntity)
                .where(
                        dateTimeBetween(dateTypeName, startDate, endDate),
                        pgTypeIn(pgTypeList),
                        containsSearchText(searchType, searchText)
                )
                .fetch()
                .stream()
                .map(CompetitorPcEntity::toCompetitorPc)
                .toList();
    }
    @Override
    public Page<CompetitorPc> findAllWithConditionAndPaging(int page, int size, String dateTypeName, LocalDate startDate, LocalDate endDate, List<Integer> pgTypeList, String searchType, String searchText) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<CompetitorPc> competitorPcList = jpaQueryFactory.selectFrom(competitorPcEntity)
                .where(
                        dateTimeBetween(dateTypeName, startDate, endDate),
                        pgTypeIn(pgTypeList),
                        containsSearchText(searchType, searchText)
                )
                .orderBy(orderByDateTime(dateTypeName))
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch()
                .stream()
                .map(CompetitorPcEntity::toCompetitorPc)
                .toList();

        Long totalCount = jpaQueryFactory.select(competitorPcEntity.count())
                .from(competitorPcEntity)
                .where(
                        dateTimeBetween(dateTypeName, startDate, endDate),
                        pgTypeIn(pgTypeList),
                        containsSearchText(searchType, searchText)
                )
                .fetchOne();
        long total = totalCount != null ? totalCount : 0L;
        return new PageImpl<>(competitorPcList, pageRequest, total);
    }

    @Override
    public List<CompetitorPc> findAllByCompetitorInstallDateBetweenStartAndEnd(LocalDate startDate, LocalDate endDate) {
        return jpaQueryFactory.selectFrom(competitorPcEntity)
                .where(
                        competitorInstallDateBetween(startDate, endDate)
                )
                .fetch()
                .stream()
                .map(CompetitorPcEntity::toCompetitorPc)
                .toList();
    }

    private BooleanExpression dateTimeBetween(String dateTypeName, LocalDate startDate, LocalDate endDate){
        if(dateTypeName ==null) return null;

        return switch (DateType.fromValue(dateTypeName)){
            case 경쟁사프로그램설치날짜 -> competitorInstallDateBetween(startDate, endDate);
            case 작업일시 -> workDatetimeBetween(startDate, endDate);
        };
    }

    private OrderSpecifier<?> orderByDateTime(String dateTypeName){
        if(dateTypeName ==null) return competitorPcEntity.workDatetime.desc();

        return switch (DateType.fromValue(dateTypeName)){
            case 경쟁사프로그램설치날짜 -> competitorPcEntity.competitorInstallDate.desc();
            case 작업일시 -> competitorPcEntity.workDatetime.desc();
        };
    }


    private BooleanExpression workDatetimeBetween(LocalDate startDate, LocalDate endDate){
        return startDate != null && endDate != null
                ? competitorPcEntity.workDatetime.between(startDate.atStartOfDay(), endDate.atTime(23,59,59))
                :null;
    }

    private BooleanExpression competitorInstallDateBetween(LocalDate startDate, LocalDate endDate){
        return startDate != null && endDate != null
                ? competitorPcEntity.competitorInstallDate.between(startDate.atStartOfDay(), endDate.atTime(23,59,59))
                :null;
    }
    private BooleanExpression containsSearchText(String searchType, String searchText){
        if(searchType == null || searchType.isBlank()){
            return null;
        }
        if(searchText == null || searchText.isBlank()){
            return null;
        }
        return switch (SearchType.fromValue(searchType)){
            case 시간 -> competitorPcEntity.workDatetime.goe(LocalDate.parse(searchText, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
            case 요양기관번호 -> competitorPcEntity.hospitalId.eq(searchText);
            case 치과명 -> competitorPcEntity.hospitalName.containsIgnoreCase(searchText);
            case MAC_주소 -> competitorPcEntity.macAddress.containsIgnoreCase(searchText);
            case PC_이름 -> competitorPcEntity.pcName.containsIgnoreCase(searchText);
            case 프로그램_종류 -> competitorPcEntity.pgType.eq(ProgramType.getCodeFromString(searchText));
            case 경쟁사_프로그램 -> competitorPcEntity.competitorId.eq(CompetitorProgramType.getCodeFromString(searchText));
            default -> null;
        };
    }

    private BooleanExpression pgTypeIn(List<Integer> pgTypeList){

        if(pgTypeList == null || pgTypeList.isEmpty()){
            return null;
        }
        return competitorPcEntity.pgType.in(pgTypeList);
    }

    private BooleanExpression hospitalIdEq(String hospitalId){
        return hospitalId != null ? competitorPcEntity.hospitalId.eq(hospitalId) : null;
    }

    private BooleanExpression competitorIdEq(int competitorId){
        return  competitorPcEntity.competitorId.eq(competitorId);
    }

    private BooleanExpression macAddressEq(String macAddress){
        return macAddress != null ? competitorPcEntity.macAddress.eq(macAddress) : null;
    }

    private BooleanExpression pgTypeEq(int pgType){
        return competitorPcEntity.pgType.eq(pgType);
    }


}