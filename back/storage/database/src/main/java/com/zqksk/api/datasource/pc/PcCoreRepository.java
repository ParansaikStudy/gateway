package com.zqksk.api.datasource.pc;

import com.zqksk.api.datasource.performance.PerformanceSpecEntity;
import com.zqksk.api.datasource.performance.PerformanceSpecJpaRepository;
import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.common.WorkType;
import com.zqksk.api.domain.pc.*;
import com.zqksk.api.exception.JpaErrorType;
import com.zqksk.api.support.exception.CoreException;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.zqksk.api.datasource.log.QLogEntity.logEntity;
import static com.zqksk.api.datasource.pc.QPcEntity.pcEntity;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PcCoreRepository implements PcRepository {

    private final PcJpaRepository pcJpaRepository;
    private final PerformanceSpecJpaRepository performanceSpecJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Pc save(final NewPc newPc) {
        Optional<PcEntity> existingPc = pcJpaRepository.findByHospitalIdAndMacAddressAndPgType(newPc.hospitalId(), newPc.macAddress(), newPc.pgType());

        PcEntity pcEntity;

        if (existingPc.isPresent()) {
            pcEntity = existingPc.get();
            pcEntity.update(
                    newPc.hospitalId(),
                    newPc.hospitalName(),
                    newPc.ip(),
                    newPc.pcName(),
                    newPc.cpu(),
                    newPc.memory(),
                    newPc.gpu(),
                    newPc.os(),
                    newPc.workType(),
                    newPc.pgType(),
                    newPc.version(),
                    newPc.monResol(),
                    LocalDateTime.now()
            );
        } else {
            pcEntity = PcEntity.builder()
                    .hospitalId(newPc.hospitalId())
                    .hospitalName(newPc.hospitalName())
                    .ip(newPc.ip())
                    .macAddress(newPc.macAddress())
                    .pcName(newPc.pcName())
                    .cpu(newPc.cpu())
                    .memory(newPc.memory())
                    .gpu(newPc.gpu())
                    .os(newPc.os())
                    .workType(newPc.workType())
                    .pgType(newPc.pgType())
                    .version(newPc.version())
                    .perfSpecScore(newPc.perfSpecScore())
                    .monResol(newPc.monResol())
                    .firstDatetime(newPc.firstDatetime())
                    .lastDatetime(newPc.lastDatetime())
                    .build();
        }

        return pcJpaRepository.save(pcEntity).toPc();
    }

    @Override
    public Pc updateScore(final Long id, final int score) {
        PcEntity pcEntity = pcJpaRepository.findById(id)
                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA));

        pcEntity.updateScore(score);

        return pcJpaRepository.save(pcEntity).toPc();
    }

    @Override
    public List<Pc> findAll() {
        List<PcEntity> pcList = pcJpaRepository.findAll();

        return pcList.stream()
                .map(PcEntity::toPc)
                .toList();
    }

    @Override
    public List<PcResponse> findAllWithCondition(final boolean excludeTest, final int pgType,final String searchType, final String searchText) {
        PerformanceSpecEntity serverSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 0);
        PerformanceSpecEntity clientSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 1);



        return jpaQueryFactory.select(
                pcEntity.id,
                        pcEntity.id,
                        pcEntity.hospitalId,
                        pcEntity.hospitalName,
                        pcEntity.ip,
                        pcEntity.macAddress,
                        pcEntity.pcName,
                        pcEntity.cpu,
                        pcEntity.memory,
                        pcEntity.gpu,
                        pcEntity.os,
                        pcEntity.workType,
                        pcEntity.pgType,
                        pcEntity.version,
                        pcEntity.monResol,
                        pcEntity.perfSpecScore,
                        pcEntity.firstDatetime,
                        pcEntity.lastDatetime
                )
                .from(pcEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        pgTypeEq(pgType),
                        buildSearchCondition(searchType, searchText, serverSpec, clientSpec)
                )
                .fetch()
                .stream()
                .map(tuple -> getPcResponse(tuple, serverSpec, clientSpec))
                .toList();
    }

    @Override
    public Page<PcResponse> findAllWithConditionAndPaging(final int page, final int size, final boolean excludeTest, final int pgType,  final String searchType, final String searchText) {
        PageRequest pageRequest = PageRequest.of(page, size);

        PerformanceSpecEntity serverSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 0);
        PerformanceSpecEntity clientSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 1);



        List<PcResponse> pcs = jpaQueryFactory.select(
                        pcEntity.id,
                        pcEntity.hospitalId,
                        pcEntity.hospitalName,
                        pcEntity.ip,
                        pcEntity.macAddress,
                        pcEntity.pcName,
                        pcEntity.cpu,
                        pcEntity.memory,
                        pcEntity.gpu,
                        pcEntity.os,
                        pcEntity.workType,
                        pcEntity.pgType,
                        pcEntity.version,
                        pcEntity.monResol,
                        pcEntity.perfSpecScore,
                        pcEntity.firstDatetime,
                        pcEntity.lastDatetime
                )
                .from(pcEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        pgTypeEq(pgType),
                        buildSearchCondition(searchType, searchText, serverSpec, clientSpec)
                )
                .orderBy(pcEntity.lastDatetime.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> getPcResponse(tuple, serverSpec, clientSpec))
                .toList();

        Long totalCount = jpaQueryFactory.select(pcEntity.count())
                .from(pcEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        pgTypeEq(pgType),
                        buildSearchCondition(searchType, searchText, serverSpec, clientSpec)
                )
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(pcs, pageRequest, total);
    }

    @Override
    public List<PcResponse> findAllWithMultipleCondition(boolean excludeTest, int pgType, Integer workType, String perfSpecName, String searchType, String searchText) {
        PerformanceSpecEntity serverSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 0);
        PerformanceSpecEntity clientSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 1);



        return jpaQueryFactory.select(
                        pcEntity.id,
                        pcEntity.hospitalId,
                        pcEntity.hospitalName,
                        pcEntity.ip,
                        pcEntity.macAddress,
                        pcEntity.pcName,
                        pcEntity.cpu,
                        pcEntity.memory,
                        pcEntity.gpu,
                        pcEntity.os,
                        pcEntity.workType,
                        pcEntity.pgType,
                        pcEntity.version,
                        pcEntity.monResol,
                        pcEntity.perfSpecScore,
                        pcEntity.firstDatetime,
                        pcEntity.lastDatetime
                )
                .from(pcEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        pgTypeEq(pgType),
                        workTypeEq(workType),
                        workTypeAndPerfSpecScoreCompare(perfSpecName, serverSpec, clientSpec),
                        containsSearchText(searchType,searchText)
                )
                .fetch()
                .stream()
                .map(tuple -> getPcResponse(tuple, serverSpec, clientSpec))
                .toList();
    }

    @Override
    public Page<PcResponse> findAllWithMultipleConditionAndPaging(int page, int size, boolean excludeTest, int pgType, Integer workType, String perfSpecName,String searchType, String searchText) {
        PageRequest pageRequest = PageRequest.of(page, size);

        PerformanceSpecEntity serverSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 0);
        PerformanceSpecEntity clientSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 1);



        List<PcResponse> pcs = jpaQueryFactory.select(
                        pcEntity.id,
                        pcEntity.hospitalId,
                        pcEntity.hospitalName,
                        pcEntity.ip,
                        pcEntity.macAddress,
                        pcEntity.pcName,
                        pcEntity.cpu,
                        pcEntity.memory,
                        pcEntity.gpu,
                        pcEntity.os,
                        pcEntity.workType,
                        pcEntity.pgType,
                        pcEntity.version,
                        pcEntity.monResol,
                        pcEntity.perfSpecScore,
                        pcEntity.firstDatetime,
                        pcEntity.lastDatetime
                )
                .from(pcEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        pgTypeEq(pgType),
                        workTypeEq(workType),
                        workTypeAndPerfSpecScoreCompare(perfSpecName, serverSpec, clientSpec),
                        containsSearchText(searchType,searchText)
                )
                .orderBy(pcEntity.lastDatetime.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch()
                .stream()
                .map(tuple -> getPcResponse(tuple, serverSpec, clientSpec))
                .toList();

        Long totalCount = jpaQueryFactory.select(pcEntity.count())
                .from(pcEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        pgTypeEq(pgType),
                        workTypeEq(workType),
                        workTypeAndPerfSpecScoreCompare(perfSpecName, serverSpec, clientSpec),
                        containsSearchText(searchType,searchText)
                )
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(pcs, pageRequest, total);
    }


    @Override
    public Pc findById(final Long id) {
        return pcJpaRepository.findById(id)
                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA))
                .toPc();
    }

    @Override
    public Pc findByMacAddress(final String macAddress) {
        return pcJpaRepository.findByMacAddress(macAddress)
                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA))
                .toPc();
    }

    @Override
    public PcResponse findByHospitalIdAndMacAddressAndPgType(final String hospitalId, final String macAddress, final int pgType) {
        PerformanceSpecEntity serverSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 0);
        PerformanceSpecEntity clientSpec = performanceSpecJpaRepository.findBySoftwareIdAndWorkType(pgType, 1);

        PcEntity result  =   pcJpaRepository.findByHospitalIdAndMacAddressAndPgType(hospitalId, macAddress, pgType)
                .orElse(null);

        if(result == null) return  null;

        PcResponse pc = result.toPcResponse();


        String performanceSpecName = "미지정";
        int specScore = pc.perfSpecScore();
        boolean isServer = WorkType.getCodeFromString(pc.workTypeName()) == 0;

        if (isServer) {
            performanceSpecName = getPerformanceSpecName(serverSpec, specScore);
        } else {
            performanceSpecName = getPerformanceSpecName(clientSpec, specScore);
        }

        return new PcResponse(
                pc.id(),
                pc.hospitalId(),
                pc.hospitalName(),
                pc.ip(),
                pc.macAddress(),
                pc.pcName(),
                pc.cpu(),
                pc.memory(),
                pc.gpu(),
                pc.os(),
                pc.workTypeName(),
                pc.pgTypeName(),
                pc.version(),
                pc.perfSpecScore(),
                pc.monResol(),
                performanceSpecName,
                pc.firstDatetime(),
                pc.lastDatetime()
        );




    }

    @Override
    public Page<CurrentPcStatus> findCurrentPcStatusWithPage(int page, int size, boolean excludeTest, LocalDate startDate, LocalDate endDate, String searchType, String searchText) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<CurrentPcStatus> currentPcStatuses = jpaQueryFactory
                .select(
                        Projections.fields(CurrentPcStatus.class,
                        pcEntity.hospitalId,
                        pcEntity.hospitalName,
                        pcEntity.pgType.as("usedProgram"),
                        Expressions.numberTemplate(Long.class, "COUNT(DISTINCT CASE WHEN {0} = 0 THEN {1} ELSE NULL END)", pcEntity.workType, pcEntity.macAddress).as("serverCount"),
                        Expressions.numberTemplate(Long.class, "COUNT(DISTINCT CASE WHEN {0} = 1 THEN {1} ELSE NULL END)", pcEntity.workType, pcEntity.macAddress).as("clientCount")
                        )
                )
                .from(pcEntity)
                .where(
                        lastDatetimeBetween(startDate, endDate),
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        containsSearchText(searchType, searchText)
                )
                .groupBy(pcEntity.hospitalId, pcEntity.hospitalName, pcEntity.pgType)
                .orderBy(pcEntity.hospitalId.asc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        int totalCount = jpaQueryFactory
                .select(pcEntity.hospitalId,
                        pcEntity.hospitalName,
                        pcEntity.pgType)
                .from(pcEntity)
                .where(
                        lastDatetimeBetween(startDate, endDate),
                        pcEntity.hospitalName.notLike("오스템%"),
                        pcEntity.pcName.notLike("OST%"),
                        pcEntity.hospitalId.notIn("00000000", "99999999"),
                        containsSearchText(searchType, searchText)
                )
                .groupBy(pcEntity.hospitalId, pcEntity.hospitalName, pcEntity.pgType)
                .fetch()
                .size();

        long total = totalCount > 0 ? totalCount : 0L;

        return new PageImpl<>(currentPcStatuses, pageRequest, total);
    }

    @Override
    public List<CurrentPcStatus> findCurrentPcStatus(boolean excludeTest, String searchType, String searchText) {
        return jpaQueryFactory
                .select(
                        Projections.fields(CurrentPcStatus.class,
                                pcEntity.hospitalId,
                                pcEntity.hospitalName,
                                pcEntity.pgType.as("usedProgram"),
                                Expressions.numberTemplate(Long.class, "COUNT(DISTINCT CASE WHEN {0} = 0 THEN {1} ELSE NULL END)", pcEntity.workType, pcEntity.macAddress).as("serverCount"),
                                Expressions.numberTemplate(Long.class, "COUNT(DISTINCT CASE WHEN {0} = 1 THEN {1} ELSE NULL END)", pcEntity.workType, pcEntity.macAddress).as("clientCount")
                        )
                )
                .from(pcEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        containsSearchText(searchType, searchText)
                )
                .groupBy(pcEntity.hospitalId, pcEntity.hospitalName, pcEntity.pgType)
                .orderBy(pcEntity.hospitalId.asc())
                .fetch();
    }

    @Override
    public List<PmsUsageStatus> findPmsUsageStatus(boolean excludeTest, LocalDate startDate, LocalDate endDate, String searchType, String searchText) {
//        PageRequest pageRequest = PageRequest.of(page, size);

        return jpaQueryFactory
                .select(
                        Projections.fields(PmsUsageStatus.class,
                                pcEntity.pgType.as("programType"),
                                pcEntity.hospitalId.countDistinct().as("usageCount")
                        )
                )
                .from(pcEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest)
                )
                .groupBy(pcEntity.pgType)
                .fetch();
    }

    @Override
    public List<Pc> findAllByLastDateBetweenStartAndEnd(LocalDate startDate, LocalDate endDate) {
        return jpaQueryFactory.selectFrom(pcEntity)
                .where(
                        lastDatetimeBetween(startDate,endDate)
                ).fetch()
                .stream()
                .map(PcEntity::toPc)
                .toList();
    }

    @Override
    public List<PerformanceSpec> findAllPerformanceSpecBySoftwareId(int softwareId) {
        return performanceSpecJpaRepository.findAllBySoftwareId(softwareId).stream()
                .map(PerformanceSpecEntity::toPerformanceSpec)
                .toList();
    }

    @Override
    public long deleteByWorkDatetimeAtBefore(LocalDateTime localDateTime) {
        return jpaQueryFactory.delete(pcEntity)
                .where(pcEntity.updatedAt.lt(localDateTime))
                .execute();
    }

    @Override
    public List<String> findAllHospitalIdList() {
        return jpaQueryFactory.select(pcEntity.hospitalId)
                .from(pcEntity)
                .fetch();
    }

    @Override
    public List<CurrentPcStatus> findCurrentPcStatusByHospitalIds(boolean excludeTest, List<String> hospitalIds) {
        if (hospitalIds == null || hospitalIds.isEmpty()) {
            return Collections.emptyList();
        }

        return jpaQueryFactory
                .select(
                        Projections.fields(CurrentPcStatus.class,
                                pcEntity.hospitalId,
                                pcEntity.hospitalName,
                                Expressions.numberTemplate(Long.class, "COUNT(DISTINCT CASE WHEN {0} = 0 THEN {1} ELSE NULL END)", pcEntity.workType, pcEntity.macAddress).as("serverCount"),
                                Expressions.numberTemplate(Long.class, "COUNT(DISTINCT CASE WHEN {0} = 1 THEN {1} ELSE NULL END)", pcEntity.workType, pcEntity.macAddress).as("clientCount")
                        )
                )
                .from(pcEntity)
                .where(
                        pcEntity.hospitalId.in(hospitalIds),
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest)
                )
                .groupBy(pcEntity.hospitalId, pcEntity.hospitalName)
                .fetch();
    }

    private PcResponse getPcResponse(Tuple tuple, PerformanceSpecEntity serverSpec, PerformanceSpecEntity clientSpec) {
        String performanceSpecName = "미지정";
        int specScore = tuple.get(pcEntity.perfSpecScore) != null ? Objects.requireNonNull(tuple.get(pcEntity.perfSpecScore)) : 0;
        boolean isServer = tuple.get(pcEntity.workType) != null && Objects.requireNonNull(tuple.get(pcEntity.workType)) == 0;

        if (isServer) {
            performanceSpecName = getPerformanceSpecName(serverSpec, specScore);
        } else {
            performanceSpecName = getPerformanceSpecName(clientSpec, specScore);
        }
        return new PcResponse(
                tuple.get(pcEntity.id),
                tuple.get(pcEntity.hospitalId),
                tuple.get(pcEntity.hospitalName),
                tuple.get(pcEntity.ip),
                tuple.get(pcEntity.macAddress),
                tuple.get(pcEntity.pcName),
                tuple.get(pcEntity.cpu),
                tuple.get(pcEntity.memory),
                tuple.get(pcEntity.gpu),
                tuple.get(pcEntity.os),
                WorkType.fromCode(tuple.get(pcEntity.workType)),
                ProgramType.fromCode(tuple.get(pcEntity.pgType)),
                tuple.get(pcEntity.version),
                tuple.get(pcEntity.perfSpecScore) != null ? tuple.get(pcEntity.perfSpecScore) : 0,
                tuple.get(pcEntity.monResol),
                performanceSpecName,
                tuple.get(pcEntity.firstDatetime),
                tuple.get(pcEntity.lastDatetime)
        );
    }

    private String getPerformanceSpecName(PerformanceSpecEntity spec, int specScore) {

        if(spec == null) return "미지정";

        if(0 < specScore && specScore < spec.getMinScore()) {
            return "저사양";
        } else if ( spec.getMinScore() <= specScore && specScore < spec.getRecScore()) {
            return "최소사양";
        } else if ( spec.getRecScore() <= specScore && specScore < spec.getMaxScore()) {
            return "권장사양";
        } else if ( spec.getMaxScore() <= specScore) {
            return "고사양";
        } else {
            return "미지정";
        }
    }

    private BooleanExpression pgTypeEq(final int pgType) {
        return pcEntity.pgType.eq(pgType);
    }

    private BooleanExpression lastDatetimeBetween(final LocalDate startDate, final LocalDate endDate) {
        return startDate != null && endDate != null
                ? pcEntity.lastDatetime.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59))
                : null;
    }

    private BooleanExpression containsSearchText(final String searchType, final String searchText) {
        if (searchType == null || searchType.isBlank()) {
            return null;
        }

        if (searchText == null || searchText.isBlank()) {
            return null;
        }

        return switch (SearchType.fromValue(searchType)) {
            case 치과명 -> pcEntity.hospitalName.contains(searchText);
            case 요양기관번호 -> pcEntity.hospitalId.contains(searchText);
            case CPU -> pcEntity.cpu.like(searchText + "%");
            case MEMORY -> pcEntity.memory.like(searchText + "%");
            case OS -> pcEntity.os.like(searchText + "%");
            case 설치날짜 -> pcEntity.lastDatetime.goe(LocalDate.parse(searchText, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
            default -> null;
        };
    }

    private BooleanExpression hospitalNameNotLike(final boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return pcEntity.hospitalName.notLike("오스템%");
    }

    private BooleanExpression pcNameNotLike(final boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return pcEntity.pcName.notLike("OST%");
    }

    private BooleanExpression hospitalIdNotIn(final boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return pcEntity.hospitalId.notIn("00000000","99999999");
    }

    private BooleanExpression perfSpecScoreCompare(String searchText, PerformanceSpecEntity entity){

        if(entity == null){
            return null;
        }
        return switch (PerfSpecType.fromValue(searchText)){
            case 고사양 -> pcEntity.workType.eq(entity.getWorkType()).and(pcEntity.perfSpecScore.gt(entity.getMaxScore()));
            case 권장사양 -> pcEntity.workType.eq(entity.getWorkType()).and(pcEntity.perfSpecScore.between(entity.getRecScore(), entity.getMaxScore()-1));
            case 최소사양 -> pcEntity.workType.eq(entity.getWorkType()).and(pcEntity.perfSpecScore.between(entity.getMinScore(), entity.getRecScore()-1));
            case 저사양 -> pcEntity.workType.eq(entity.getWorkType()).and(pcEntity.perfSpecScore.between(1, entity.getMinScore()-1));
            case 미지정 -> pcEntity.workType.eq(entity.getWorkType()).and(pcEntity.perfSpecScore.isNull().or(pcEntity.perfSpecScore.eq(0)));
        };
    }

    private BooleanExpression workTypeAndPerfSpecScoreCompare(final String perfSpecName, PerformanceSpecEntity server, PerformanceSpecEntity client){
        if (perfSpecName == null || perfSpecName.isBlank()) {
            return null;
        }

        BooleanExpression serverSpec = perfSpecScoreCompare(perfSpecName, server);
        BooleanExpression clientSpec = perfSpecScoreCompare(perfSpecName, client);
        return serverSpec.or(clientSpec);
    }

    private BooleanExpression buildSearchCondition(final String searchType, final String searchText, PerformanceSpecEntity server, PerformanceSpecEntity client){
        if("perfSpecScore".equals(searchType)){

            return workTypeAndPerfSpecScoreCompare(searchText, server, client);
        }else{
            return containsSearchText(searchType, searchText);
        }
    }

    private BooleanExpression workTypeEq(Integer workType){
        if (workType==null) return null;

        return pcEntity.workType.eq(workType);


    }





}
