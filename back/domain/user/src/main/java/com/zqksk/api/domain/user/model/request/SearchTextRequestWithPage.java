package com.zqksk.api.domain.user.model.request;

import jakarta.validation.constraints.NotBlank;

public record SearchTextRequestWithPage(

        @NotBlank
        int page,
        @NotBlank
        int size,
        String searchText
) {
}
