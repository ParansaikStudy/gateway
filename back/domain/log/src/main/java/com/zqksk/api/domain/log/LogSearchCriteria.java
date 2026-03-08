package com.zqksk.api.domain.log;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 로그 검색 조건
 */
@Builder
public record LogSearchCriteria(
    Integer id,
    String hospitalId,
    String hospitalName,
    LocalDateTime workDatetime,
    String userId,
    String patientUid,
    String ip,
    String macAddress,
    String pcName,
    int eventType,
    String rootMenu,
    String viewId,
    String viewName,
    String errorCode,
    String log,
    String logDetail,
    String etc,
    int workType,
    String version,
    int programType
) {
}
