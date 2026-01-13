package com.zqksk.api.domain.log;

import org.springframework.data.domain.Page;

import java.util.List;

public interface LogService {
    default Log save(NewLog log) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<Log> getLogs() {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<LogResponse> getLogListWithCondition(SearchLogRequest searchLogRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default Page<Log> getLogListWithConditionAndPaging(SearchLogRequestWithPage searchLogRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default Log getLog(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
