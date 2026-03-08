package com.zqksk.api.domain.dentistry;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SearchDentistryRequestWithPage(
        @NotNull
        @Schema(title = "페이지위치", example = "1")
        int page,
        @NotNull
        @Schema(title = "페이지사이즈", example = "15")
        int size,
        @Schema(title = "테스트 데이터 제외 유무" ,example ="true")
        boolean excludeTest,
        @NotBlank
        @Schema(title = "시작일자" , example = "2025-04-13")
        String startDate,
        @NotBlank
        @Schema(title = "종료일자", example = "2024-04-20")
        String endDate,
        @Schema(title = "검색조건",example = "competitorId", description = "hospitalId, hospitalName, programType, address, telephone, pmsContractState ")
        String searchType,
        @Schema(title = "검색어", example = "오스템치과")
        String searchText
) {
}
