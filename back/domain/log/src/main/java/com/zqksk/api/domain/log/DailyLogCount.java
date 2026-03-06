package com.zqksk.api.domain.log;

import java.time.LocalDate;

public record DailyLogCount(
    LocalDate date,
    Long count
) {
}
