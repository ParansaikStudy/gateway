package com.zqksk.api.domain.log;

public record AccessLogCustomCount(
        int pgType,
        String rootMenu,
        Long count

) {
}
