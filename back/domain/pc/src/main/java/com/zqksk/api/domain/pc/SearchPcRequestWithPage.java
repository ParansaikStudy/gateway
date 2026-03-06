package com.zqksk.api.domain.pc;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SearchPcRequestWithPage(
    @Schema(title = "페이지위치", example = "1")
    @NotNull
    int page,
    @Schema(title = "페이지사이즈", example = "15")
    @NotNull
    int size,
    @Schema(title = "테스트 데이터 제외 유무" ,example ="true")
    boolean excludeTest,
    @Schema(title = "프로그램 종류", example = "1")
    @NotNull
    int pgType,
    @Schema(title = "검색조건",description = "조건 : 치과명, 요영기관번호, CPU, RAM, OS, 설치날짜, 사양",example = "hospitalName")
    String searchType,
    @Schema(title = "검색어", example = "오스템치과")
    String searchText
) {
}
