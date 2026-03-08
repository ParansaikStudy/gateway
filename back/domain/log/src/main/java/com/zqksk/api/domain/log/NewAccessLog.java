package com.zqksk.api.domain.log;

import java.time.LocalDateTime;

public record NewAccessLog(
    String hospitalId,
    String hospitalName,
    LocalDateTime workDatetime,
    String userId,
    String ip,
    String macAddress,
    String pcName,
    int eventType,
    String rootMenu,
    String viewId,
    String viewName,
    String functionName,
    String elementName,
    int action,
    String description,
    String loadingSpeed,
    String remark,
    String version,
    int programType
) {
}
