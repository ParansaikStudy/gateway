package com.zqksk.api.domain.log;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SearchAccessLogRequestWithPage(
    @NotNull
    int page,
    @NotNull
    int size,
    boolean excludeTest,
    @NotBlank
    String startDate,
    @NotBlank
    String endDate,
    @NotNull
    int eventType,
    @NotNull
    int pgType,
    String searchType,
    String searchText
) {
}
