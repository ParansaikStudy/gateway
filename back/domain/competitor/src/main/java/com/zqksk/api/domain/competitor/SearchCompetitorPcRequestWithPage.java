package com.zqksk.api.domain.competitor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SearchCompetitorPcRequestWithPage(
        @Schema(title = "페이지위치", example = "1")
        @NotNull
        int page,
        @Schema(title = "페이지사이즈", example = "15")
        @NotNull
        int size,
        @Schema(title = "기간검색조건",example = "workDateTime",  description = "조건 = workDateTime,competitorInstallDatetime")
        String dateTypeName,
        @NotBlank
        @Schema(title = "시작일자" , example = "2025-04-13")
        String startDate,
        @NotBlank
        @Schema(title = "종료일자", example = "2024-04-20")
        String endDate,
        @Schema(title = "프로그램 종류 리스트", example = "[1,2]")
        List<Integer> pgTypeList,
        @Schema(title = "검색조건",example = "competitorId", description = "workDateTime, hospitalId, hospitalName, macAddress,pcName, programType, competitorId")
        String searchType,
        @Schema(title = "검색어", example = "덴트웹")
        String searchText
){
}
