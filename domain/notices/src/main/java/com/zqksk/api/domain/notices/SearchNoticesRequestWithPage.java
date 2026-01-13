package com.zqksk.api.domain.notices;

import jakarta.validation.constraints.NotNull;

public record SearchNoticesRequestWithPage(
        @NotNull
        int page,
        @NotNull
        int size,
        String searchText,
        Integer pgType
) {

}
