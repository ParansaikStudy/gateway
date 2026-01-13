package com.zqksk.api.domain.competitor;

import java.time.LocalDateTime;

public record NewCompetitorPc(
        String hospitalId,
        String hospitalName,
        String ip,
        String macAddress,
        String pcName,
        int pgType,
        int competitorId,
        LocalDateTime competitorInstallDatetime,
        LocalDateTime workDatetime

        ) {
}
