package com.zqksk.api.domain.notices;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record NoticesUpdateRequest(
        @NotNull
        @Schema(description = "ID", example = "1", type = "number")
        Long id,

        @NotNull
        @Schema(description = "프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick)", example = "2", type = "number")
        int pgType,

        @NotBlank
        @Schema(description = "공지사항 제목", example = "공지사항 제목입니다.", maxLength = 200, type = "string")
        String title,

        @NotBlank
        @Schema(description = "공지사항 내용", example = "여기에 공지사항 내용을 작성하세요.", maxLength = 10000, type = "string")
        String content,

        @Schema(description = "공지 시작 일자", example = "2024-08-18 00:00:00", type = "string")
        String noticeStartDate,

        @Schema(description = "공지 종료 일자", example = "2024-08-18 00:00:00", type = "string")
        String noticeEndDate,

        @NotNull
        @Schema(description = "유저ID", example = "1", type = "number")
        Long userId

) {

        public LocalDate startDateAsLocalDate() {
                return parseDate(noticeStartDate);
        }

        public LocalDate endDateAsLocalDate() {
                return parseDate(noticeEndDate);
        }



        private LocalDate parseDate(String dateStr){
                if(dateStr == null || dateStr.isBlank()){
                        return  null;
                }else{
                        return LocalDate.parse(dateStr,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
        }
}
