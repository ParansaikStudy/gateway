package com.zqksk.api.domain.log;

import java.time.LocalDateTime;

public record Log(
    Long id,
    String hospitalId,
    String hospitalName,
    LocalDateTime workDatetime,
    String userId,
    String patientUid,
    String ip,
    String macAddress,
    String pcName,
    String eventType,
    String rootMenu,
    String viewId,
    String viewName,
    String errorCode,
    String log,
    String logDetail,
    String etc,
    String workType,
    String version,
    String programType
) {
    public LogResponse toResponse() {
        return new LogResponse(
            id,
            workDatetime,
            programType,
            workType,
            version,
            eventType,
            rootMenu,
            viewId,
            viewName,
            errorCode,
            log,
            logDetail,
            etc,
            hospitalId,
            hospitalName,
            pcName
        );
    }
}
