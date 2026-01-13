package com.zqksk.api.domain.pc;

import java.time.LocalDateTime;

public record Pc(
    Long id,
    String hospitalId,
    String hospitalName,
    String ip,
    String macAddress,
    String pcName,
    String cpu,
    String memory,
    String gpu,
    String os,
    String workTypeName,
    String pgTypeName,
    String version,
    int perfSpecScore,
    String monResol,
    LocalDateTime firstDatetime,
    LocalDateTime lastDatetime
) {
}
