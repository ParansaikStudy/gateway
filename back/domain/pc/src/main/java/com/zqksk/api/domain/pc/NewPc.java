package com.zqksk.api.domain.pc;

import java.time.LocalDateTime;

public record NewPc(
    String hospitalId,
    String hospitalName,
    String ip,
    String macAddress,
    String pcName,
    String cpu,
    String memory,
    String gpu,
    String os,
    int workType,
    int pgType,
    String version,
    int perfSpecScore,
    String monResol,
    LocalDateTime firstDatetime,
    LocalDateTime lastDatetime
) {
}
