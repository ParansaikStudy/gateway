package com.zqksk.api.domain.notices;

import jakarta.validation.constraints.NotNull;

public record SearchNoticesRequest(
        @NotNull
        int page,
        @NotNull
        int size,
        String searchText,
        Integer pgType
) {

}
