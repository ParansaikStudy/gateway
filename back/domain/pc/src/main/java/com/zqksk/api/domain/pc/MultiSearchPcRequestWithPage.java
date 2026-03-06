package com.zqksk.api.domain.pc;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record MultiSearchPcRequestWithPage(
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
    @Schema(title = "클라이언트/서버구분", example = "0")
    Integer workType,
    @Schema(title = "성능 사양", example = "고사양", description = "조건 : 고사양, 저사양")
    String perfSpecName,
    @Schema(title = "검색조건",description = "조건 : 치과명, 요영기관번호, CPU, RAM, OS, 설치날짜",example = "hospitalName")
    String searchType,
    @Schema(title = "검색어", example = "오스템치과")
    String searchText
) {
}
