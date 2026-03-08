package com.zqksk.api.domain.log;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccessLogFinder {

    private final AccessLogRepository accessLogRepository;

    public Page<AccessLog> getAccessLogListWithConditionAndPaging(int page, int size, boolean excludeTest, LocalDate startDate, LocalDate endDate,int eventType, int pgType, String searchType, String searchText) {
        return accessLogRepository.findAllWithConditionAndPaging(page, size, excludeTest, startDate, endDate, eventType, pgType, searchType, searchText);
    }

    public List<AccessLog> getAccessLogListWithCondition(boolean excludeTest, LocalDate startDate, LocalDate endDate,int eventType, int pgType, String searchType, String searchText) {
        return accessLogRepository.findAllWithCondition(excludeTest, startDate, endDate, eventType, pgType, searchType, searchText);
    }

    public List<AccessLogCustomCount> getTop3RootMenuCounts(int pgType, LocalDate startDate, LocalDate endDate){
        return accessLogRepository.findTop3RootMenuCounts(pgType, startDate, endDate);
    }

    public AccessLog getAccessLog(Long id){
        return accessLogRepository.findById(id);
    }
}
