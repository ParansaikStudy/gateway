package com.zqksk.api.domain.log;

import java.time.LocalDateTime;

public record AccessLog(
    Long id,
    LocalDateTime workDatetime,
    String programType,
    String version,
    String rootMenu,
    String viewId,
    String viewName,
    String functionName,
    String elementName,
    String loadingSpeed,
    String description,
    String action,
    String hospitalName,
    String hospitalId,
    String pcName,
    String ip,
    String macAddress,
    String userId,
    String eventType,
    String remark
) {
}
