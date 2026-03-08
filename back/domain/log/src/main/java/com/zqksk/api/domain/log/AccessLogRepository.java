package com.zqksk.api.domain.log;

import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AccessLogRepository {
    AccessLog save(NewAccessLog log);
    Page<AccessLog> findAllWithConditionAndPaging(int page, int size, boolean excludeTest, LocalDate startDate, LocalDate endDate,int eventType, int pgType, String searchType, String searchText);
    List<AccessLog> findAllWithCondition(boolean excludeTest, LocalDate startDate, LocalDate endDate, int eventType,int pgType, String searchType, String searchText);

    List<AccessLogCustomCount> findTop3RootMenuCounts(int pgType, LocalDate startDate, LocalDate endDat);

    AccessLog findById(Long id);

    Long deleteByWorkDatetimeAtBefore(LocalDateTime localDateTime);
}
