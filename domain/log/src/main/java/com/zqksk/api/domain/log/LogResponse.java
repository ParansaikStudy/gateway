package com.zqksk.api.domain.log;

import java.time.LocalDateTime;

public record LogResponse(
    Long id,
    LocalDateTime workDatetime,
    String programType,
    String workType,
    String version,
    String eventType,
    String rootMenu,
    String viewId,
    String viewName,
    String errorCode,
    String log,
    String logDetail,
    String etc,
    String hospitalId,
    String hospitalName,
    String pcName
) {
}
