package com.zqksk.api.datasource.log;

import com.zqksk.api.domain.common.ProgramType;
import com.zqksk.api.domain.common.SearchType;
import com.zqksk.api.domain.log.*;
import com.zqksk.api.exception.JpaErrorType;
import com.zqksk.api.support.exception.CoreException;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.zqksk.api.datasource.log.QAccessLogEntity.accessLogEntity;

@Repository
@RequiredArgsConstructor
public class AccessLogCoreRepository implements AccessLogRepository {
    private final AccessLogJpaRepository accessLogJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public AccessLog save(NewAccessLog newAccessLog) {
        return accessLogJpaRepository.save(AccessLogEntity.builder()
                .hospitalId(newAccessLog.hospitalId())
                .hospitalName(newAccessLog.hospitalName())
                .workDatetime(newAccessLog.workDatetime())
                .userId(newAccessLog.userId())
                .ip(newAccessLog.ip())
                .macAddress(newAccessLog.macAddress())
                .pcName(newAccessLog.pcName())
                .eventType(newAccessLog.eventType())
                .rootMenu(newAccessLog.rootMenu())
                .viewId(newAccessLog.viewId())
                .viewName(newAccessLog.viewName())
                .functionName(newAccessLog.functionName())
                .elementName(newAccessLog.elementName())
                .action(newAccessLog.action())
                .description(newAccessLog.description())
                .loadingSpeed(newAccessLog.loadingSpeed())
                .remark(newAccessLog.remark())
                .version(newAccessLog.version())
                .programType(newAccessLog.programType())
                .build()
        ).toAccessLog();
    }

    @Override
    public Page<AccessLog> findAllWithConditionAndPaging(int page, int size, boolean excludeTest, LocalDate startDate, LocalDate endDate, int eventType, int pgType, String searchType, String searchText) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<AccessLog> accessLogs = jpaQueryFactory.selectFrom(accessLogEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        pgTypeEq(pgType),
                        eventTypeEq(eventType),
                        lastDatetimeBetween(startDate, endDate),
                        containsSearchText(searchType, searchText)
                )
                .orderBy(accessLogEntity.workDatetime.desc())
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch()
                .stream()
                .map(AccessLogEntity::toAccessLog)
                .toList();

        Long totalCount = jpaQueryFactory.select(accessLogEntity.count())
                .from(accessLogEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        pgTypeEq(pgType),
                        eventTypeEq(eventType),
                        lastDatetimeBetween(startDate, endDate),
                        containsSearchText(searchType, searchText)
                )
                .fetchOne();

        long total = totalCount != null ? totalCount : 0L;

        return new PageImpl<>(accessLogs, pageRequest, total);
    }

    @Override
    public List<AccessLog> findAllWithCondition(boolean excludeTest, LocalDate startDate, LocalDate endDate,int eventType, int pgType, String searchType, String searchText) {
        return jpaQueryFactory.selectFrom(accessLogEntity)
                .where(
                        hospitalNameNotLike(excludeTest),
                        pcNameNotLike(excludeTest),
                        hospitalIdNotIn(excludeTest),
                        pgTypeEq(pgType),
                        eventTypeEq(eventType),
                        lastDatetimeBetween(startDate, endDate),
                        containsSearchText(searchType, searchText)
                )
                .orderBy(accessLogEntity.workDatetime.desc())
                .fetch()
                .stream()
                .map(AccessLogEntity::toAccessLog)
                .toList();
    }

    @Override
    public List<AccessLogCustomCount> findTop3RootMenuCounts(int pgType, LocalDate startDate, LocalDate endDate) {
        return jpaQueryFactory.select(Projections.constructor(AccessLogCustomCount.class,accessLogEntity.programType, accessLogEntity.rootMenu, accessLogEntity.count()))
                .from(accessLogEntity)
                .where(accessLogEntity.rootMenu.isNotNull(),
                        pgTypeEq(pgType),
                        lastDatetimeBetween(startDate, endDate)
                        )
                .groupBy(accessLogEntity.rootMenu)
                .orderBy(accessLogEntity.count().desc())
                .limit(3)
                .fetch();
    }

    @Override
    public AccessLog findById(Long id) {
        return accessLogJpaRepository.findById(id)
                .orElseThrow(() -> new CoreException(JpaErrorType.NOT_FOUND_DATA))
                .toAccessLog();
    }

    @Override
    public Long deleteByWorkDatetimeAtBefore(LocalDateTime localDateTime) {
        return jpaQueryFactory.delete(accessLogEntity)
                .where(accessLogEntity.workDatetime.lt(localDateTime))
                .execute();
    }

    private BooleanExpression hospitalNameNotLike(boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return accessLogEntity.hospitalName.notLike("오스템%");
    }

    private BooleanExpression pcNameNotLike(boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return accessLogEntity.pcName.notLike("OST%");
    }

    private BooleanExpression hospitalIdNotIn(boolean excludeTest) {
        if(!excludeTest) {
            return null;
        }

        return accessLogEntity.hospitalId.notIn("00000000","9999999");
    }

    private BooleanExpression pgTypeEq(int pgType) {
        return accessLogEntity.programType.eq(pgType);
    }

    private BooleanExpression eventTypeEq(int eventType){
        if(eventType == 12) {
            return accessLogEntity.eventType.eq(eventType);
        }
        return accessLogEntity.eventType.in(0,11);
    }

    private BooleanExpression lastDatetimeBetween(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null
                ? accessLogEntity.workDatetime.between(startDate.atStartOfDay(), endDate.atTime(23, 59, 59))
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
            case ID -> accessLogEntity.id.eq(Long.valueOf(searchText));
            case 시간 -> accessLogEntity.workDatetime.goe(LocalDate.parse(searchText, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
            case 프로그램 -> accessLogEntity.programType.eq(ProgramType.getCodeFromString(searchText));
            case 버전 -> accessLogEntity.version.eq(searchText);
            case 메뉴 -> accessLogEntity.rootMenu.eq(searchText);
            case 화면ID -> accessLogEntity.viewId.eq(searchText);
            case 화면명 -> accessLogEntity.viewName.eq(searchText);
            case 요양기관번호 -> accessLogEntity.hospitalId.eq(searchText);
            case 병원명 -> accessLogEntity.hospitalName.containsIgnoreCase(searchText);
            case PC명 -> accessLogEntity.pcName.containsIgnoreCase(searchText);
            default -> null;
        };
    }
}
