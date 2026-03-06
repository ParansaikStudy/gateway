package com.zqksk.api.domain.log;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SearchLogRequest(
    boolean excludeTest,
    @NotNull
    @Size(min = 1)
    List<Integer> eventTypes,
    @NotBlank
    String startDate,
    @NotBlank
    String endDate,
    @NotNull
    int pgType,
    String searchType,
    String searchText
) {
}
