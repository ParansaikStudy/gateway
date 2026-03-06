package com.zqksk.api.domain.log;

import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LogRepository {
    Log save(NewLog log);
    List<Log> findAll();
    List<Log> findAllWithCondition(boolean excludeTest, List<Integer> eventTypes, LocalDate startDate, LocalDate endDate, int pgType, String searchType, String searchText);
    Page<Log> findAllWithConditionAndPaging(int page, int size, boolean excludeTest, List<Integer> eventTypes, LocalDate startDate, LocalDate endDate, int pgType, String searchType, String searchText);
    Log findById(Long id);
    Log findByCondition(LogSearchCriteria logSearchCriteria);
    Optional<Log> findDuplicateLog(LogSearchCriteria logSearchCriteria);
    List<DailyLogCount> findAllWithConditionForChart(boolean excludeTest, List<Integer> eventTypes, LocalDate startDate, LocalDate endDate, int pgType, String searchType, String searchText);

    Long deleteByWorkDatetimeAtBefore(LocalDateTime localDateTime);
}
