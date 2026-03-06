package com.zqksk.api.domain.log;

import java.time.LocalDate;

public record LogResponseForChart(
    LocalDate date,
    int count
) {
}
