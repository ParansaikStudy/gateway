package com.zqksk.api.domain.log;

import org.springframework.data.domain.Page;

import java.util.List;

public interface AccessLogService {

    default AccessLog save(NewAccessLog accessLog) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default Page<AccessLog> getAccessLogListWithConditionAndPaging(SearchAccessLogRequestWithPage searchLogRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<AccessLog> getAccessLogListWithCondition(SearchAccessLogRequest searchLogRequest) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<AccessLogCustomCount> getTop3RootMenuCounts(SearchCountRequest searchCountRequest){
        throw new UnsupportedOperationException("Not implemented");
    }

}
