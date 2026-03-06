package com.zqksk.api.datasource.log;

import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.common.SearchType;
import com.zqksk.api.domain.common.WorkType;
import com.zqksk.api.domain.log.*;
import com.zqksk.api.exception.JpaErrorType;
import com.zqksk.api.support.exception.CoreException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.zqksk.api.datasource.log.QAccessLogEntity.accessLogEntity;
import static com.zqksk.api.datasource.log.QLogEntity.logEntity;

@Repository
@RequiredArgsConstructor
public class LogCoreRepository implements LogRepository {
    private final LogJpaRepository logJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Log save(NewLog newLog) {
        return logJpaRepository.save(LogEntity.builder()
                .hospitalId(newLog.hospitalId())
                .hospitalName(newLog.hospitalName())
                .workDatetime(newLog.workDatetime())
                .userId(newLog.userId())
                .patientUid(newLog.patientUid())
                .ip(newLog.ip())
                .macAddress(newLog.macAddress())
                .pcName(newLog.pcName())
                .eventType(newLog.eventType())
                .rootMenu(newLog.rootMenu())
                .viewId(newLog.viewId())
                .viewName(newLog.viewName())
                .errorCode(safeString(newLog.errorCode()))
                .log(newLog.log())
                .logDetail(newLog.logDetail())
                .etc(newLog.etc())
                .workType(newLog.workType())
                .version(newLog.version())
                .programType(newLog.programType())
                .build()
        ).toLog();
    }

    @Override
    public List<Log> findAll() {
        List<LogEntity> logs = logJpaRepository.findAll();

        return logs.stream()
                .map(LogEntity::toLog)
                .toList();
    }

    @Override
    public List<Log> findAllWithCondition(boolean excludeTest, List<Integer> eventTypes, LocalDate startDate, LocalDate endDate, int pgType, String searchType, String searchText) {
        return jpaQueryFactory.selectFrom(logEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        eventTypeEq(eventTypes),
                        pgTypeEq(pgType),
                        lastDatetimeBetween(startDate, endDate),
                        containsSearchText(searchType, searchText)
                )
                .orderBy(logEntity.workDatetime.asc())
                .fetch()
                .stream()
                .map(LogEntity::toLog)
                .toList();
    }

    @Override
    public Page<Log> findAllWithConditionAndPaging(int page, int size, boolean excludeTest, List<Integer> eventTypes, LocalDate startDate, LocalDate endDate, int pgType, String searchType, String searchText) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Log> logs = jpaQueryFactory.selectFrom(logEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        eventTypeEq(eventTypes),
                        pgTypeEq(pgType),
                        lastDatetimeBetween(startDate, endDate),
                        containsSearchText(searchType, searchText)
                )
                .orderBy(logEntity.workDatetime.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch()
                .stream()
                .map(LogEntity::toLog)
                .toList();

        Long totalCount = jpaQueryFactory.select(logEntity.count())
                .from(logEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        eventTypeEq(eventTypes),
                        pgTypeEq(pgType),
                        lastDatetimeBetween(startDate, endDate),
                        containsSearchText(searchType, searchText)
                )
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(logs, pageRequest, total);
    }

    @Override
    public Log findById(Long id) {
        return logJpaRepository.findById(id)
                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA))
                .toLog();
    }

    @Override
    public Log findByCondition(@Nonnull LogSearchCriteria logSearchCriteria) {
        return jpaQueryFactory.selectFrom(QLogEntity.logEntity)
                .where(
                        logSearchCriteria.hospitalId() != null ? QLogEntity.logEntity.hospitalId.eq(logSearchCriteria.hospitalId()) : null,
                        logSearchCriteria.hospitalName() != null ? QLogEntity.logEntity.hospitalName.eq(logSearchCriteria.hospitalName()) : null,
                        logSearchCriteria.workDatetime() != null ? QLogEntity.logEntity.workDatetime.eq(logSearchCriteria.workDatetime()) : null,
                        logSearchCriteria.userId() != null ? QLogEntity.logEntity.userId.eq(logSearchCriteria.userId()) : null,
                        logSearchCriteria.patientUid() != null ? QLogEntity.logEntity.patientUid.eq(logSearchCriteria.patientUid()) : null,
                        logSearchCriteria.ip() != null ? QLogEntity.logEntity.ip.eq(logSearchCriteria.ip()) : null,
                        logSearchCriteria.macAddress() != null ? QLogEntity.logEntity.macAddress.eq(logSearchCriteria.macAddress()) : null,
                        logSearchCriteria.pcName() != null ? QLogEntity.logEntity.pcName.eq(logSearchCriteria.pcName()) : null,
                        QLogEntity.logEntity.eventType.eq(logSearchCriteria.eventType()),
                        logSearchCriteria.rootMenu() != null ? QLogEntity.logEntity.rootMenu.eq(logSearchCriteria.rootMenu()) : null,
                        logSearchCriteria.viewId() != null ? QLogEntity.logEntity.viewId.eq(logSearchCriteria.viewId()) : null,
                        logSearchCriteria.log() != null ? QLogEntity.logEntity.log.eq(logSearchCriteria.log()) : null,
                        logSearchCriteria.logDetail() != null ? QLogEntity.logEntity.logDetail.eq(logSearchCriteria.logDetail()) : null,
                        logSearchCriteria.etc() != null ? QLogEntity.logEntity.etc.eq(logSearchCriteria.etc()) : null,
                        QLogEntity.logEntity.workType.eq(logSearchCriteria.workType()),
                        logSearchCriteria.version() != null ? QLogEntity.logEntity.version.eq(logSearchCriteria.version()) : null,
                        QLogEntity.logEntity.programType.eq(logSearchCriteria.programType())
                )
                .fetchFirst()
                .toLog();
    }

    @Override
    public Optional<Log> findDuplicateLog(LogSearchCriteria logSearchCriteria) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(QLogEntity.logEntity)
                .where(
                        logEntity.hospitalId.eq(logSearchCriteria.hospitalId()),
                        logEntity.macAddress.eq(logSearchCriteria.macAddress()),
                        logEntity.log.eq(logSearchCriteria.log()),
                        logEntity.programType.eq(logSearchCriteria.programType()),
                        logEntity.createdAt.between(LocalDateTime.now().minusMinutes(5), LocalDateTime.now())
                )
                .fetchFirst())
                .map(LogEntity::toLog);
    }

    @Override
    public List<DailyLogCount> findAllWithConditionForChart(boolean excludeTest, List<Integer> eventTypes, LocalDate startDate, LocalDate endDate, int pgType, String searchType, String searchText) {
        return jpaQueryFactory
                .select(
                        Expressions.dateTemplate(
                                Date.class,
                                "DATE({0})",
                                logEntity.workDatetime
                        ).as("date"),
                        logEntity.count()
                )
                .from(logEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        eventTypeEq(eventTypes),
                        pgTypeEq(pgType),
                        lastDatetimeBetween(startDate, endDate),
                        containsSearchText(searchType, searchText)
                )
                .groupBy(Expressions.dateTemplate(
                        LocalDate.class,
                        "DATE({0})",
                        logEntity.workDatetime
                ))
                .orderBy(Expressions.dateTemplate(
                        LocalDate.class,
                        "DATE({0})",
                        logEntity.workDatetime
                ).asc())
                .fetch()
                .stream()
                .map(tuple -> new DailyLogCount(
                        tuple.get(0, Date.class).toLocalDate(),
                        tuple.get(1, Long.class)
                ))
                .toList();
    }

    @Override
    public Long deleteByWorkDatetimeAtBefore(LocalDateTime localDateTime) {
        return jpaQueryFactory.delete(logEntity)
                .where(logEntity.workDatetime.lt(localDateTime))
                .execute();
    }

    private BooleanExpression pgTypeEq(int pgType) {
        return logEntity.programType.eq(pgType);
    }

    private BooleanExpression eventTypeEq(List<Integer> eventTypes) {
        return logEntity.eventType.in(eventTypes);
    }

    private BooleanExpression lastDatetimeBetween(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null
                ? logEntity.workDatetime.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59))
                : null;
    }

    private BooleanExpression containsSearchText(String searchType, String searchText) {
        if (searchType == null || searchType.isBlank()) {
            return null;
        }

        if (searchText == null || searchText.isBlank()) {
            return null;
        }

        return switch (SearchType.fromValue(searchType)) {
            case ID -> logEntity.id.eq(Long.valueOf(searchText));
            case 시간 -> logEntity.workDatetime.goe(LocalDate.parse(searchText, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
            case 프로그램 -> logEntity.programType.eq(ProgramType.getCodeFromString(searchText));
            case 프로그램_종류 -> logEntity.workType.eq(WorkType.getCodeFromString(searchText));
            case 버전 -> logEntity.version.eq(searchText);
            case 메뉴 -> logEntity.rootMenu.eq(searchText);
            case 화면ID -> logEntity.viewId.eq(searchText);
            case 화면명 -> logEntity.viewName.eq(searchText);
            case 로그 -> logEntity.log.containsIgnoreCase(searchText);
            case 로그_상세 -> logEntity.logDetail.containsIgnoreCase(searchText);
            case 로그_기타 -> logEntity.etc.containsIgnoreCase(searchText);
            case 요양기관번호 -> logEntity.hospitalId.eq(searchText);
            case 병원명 -> logEntity.hospitalName.containsIgnoreCase(searchText);
            case PC명 -> logEntity.pcName.containsIgnoreCase(searchText);
            default -> null;
        };
    }

    private BooleanExpression hospitalNameNotLike(boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return logEntity.hospitalName.notLike("오스템%");
    }

    private BooleanExpression pcNameNotLike(boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return logEntity.pcName.notLike("OST%");
    }

    private BooleanExpression hospitalIdNotIn(boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return logEntity.hospitalId.notIn("00000000","9999999");
    }

    private String safeString(String value){
        return value == null || value.isEmpty() ? value = "" : value;
    }
}
