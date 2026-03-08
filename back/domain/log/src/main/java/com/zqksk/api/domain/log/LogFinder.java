package com.zqksk.api.domain.log;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LogFinder {

    private final LogRepository logRepository;

    public List<Log> getLogs() {
        return logRepository.findAll();
    }

    public List<Log> getLogListWithCondition(boolean excludeTest, List<Integer> eventTypes, LocalDate startDate, LocalDate endDate, int pgType, String searchType, String searchText) {
        return logRepository.findAllWithCondition(excludeTest, eventTypes, startDate, endDate, pgType, searchType, searchText);
    }

    public Page<Log> getLogsWithConditionAndPaging(int page, int size, boolean excludeTest, List<Integer> eventTypes, LocalDate startDate, LocalDate endDate, int pgType, String searchType, String searchText) {
        return logRepository.findAllWithConditionAndPaging(page, size, excludeTest, eventTypes, startDate, endDate, pgType, searchType, searchText);
    }

    public Log getLog(Long id) {
        return logRepository.findById(id);
    }

    public List<DailyLogCount> getLogsWithConditionForChart(boolean excludeTest, List<Integer> eventTypes, LocalDate startDate, LocalDate endDate, int pgType, String searchType, String searchText) {
        return logRepository.findAllWithConditionForChart(excludeTest, eventTypes, startDate, endDate, pgType, searchType, searchText);
    }

    public Log getLogByCondition(LogSearchCriteria logSearchCriteria) {
        return logRepository.findByCondition(logSearchCriteria);
    }

    public Optional<Log> getDuplicateLog(LogSearchCriteria logSearchCriteria) {
        return logRepository.findDuplicateLog(logSearchCriteria);
    }
}
