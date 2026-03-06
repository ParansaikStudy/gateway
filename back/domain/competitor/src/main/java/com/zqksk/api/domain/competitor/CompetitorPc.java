package com.zqksk.api.domain.competitor;

import java.time.LocalDateTime;

public record CompetitorPc(
        Long id,
        String hospitalId,
        String hospitalName,
        String ip,
        String macAddress,
        String pcName,
        String pgTypeName,
        String competitorPgName,
        LocalDateTime competitorInstallDatetime,
        LocalDateTime workDatetime

) {
}
