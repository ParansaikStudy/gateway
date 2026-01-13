package com.zqksk.api.domain.log;

import io.swagger.v3.oas.annotations.media.Schema;

public record SearchCountRequest(
        @Schema(description = "프로그램타입" , example = "1")
        int pgType,
        @Schema(description = "시작일자" , example = "2025-04-13")
        String startDate,

        @Schema(description = "종료일자", example = "2024-04-20")
        String endDate
) {
}
