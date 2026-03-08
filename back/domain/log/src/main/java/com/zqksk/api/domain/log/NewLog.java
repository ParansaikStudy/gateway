package com.zqksk.api.domain.log;

import java.time.LocalDateTime;

public record NewLog(
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
    private static final int MAX_LOG_DETAIL_LENGTH = 4000;

    public NewLog {
        if (logDetail != null && logDetail.length() > MAX_LOG_DETAIL_LENGTH) {
            logDetail = logDetail.substring(0, MAX_LOG_DETAIL_LENGTH);
        }
    }
}
