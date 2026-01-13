package com.zqksk.api.domain.competitor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record CompetitorPcRequest(

        @NotBlank
        @Schema(description = "요양기관번호", example = "1234567890", maxLength = 15, type = "string")
        String hospitalId,

        @NotBlank
        @Schema(description = "병원명", example = "트윈치과", maxLength = 50, type = "string")
        String hospitalName,
        @Schema(description = "IP 주소", example = "192.168.0.111", maxLength = 20, type = "string")
        String ip,

        @NotBlank
        @Schema(description = "PC MAC 주소", example = "AA-BB-CC-DD-EE-FF", maxLength = 100, type = "string")
        String macAddress,

        @Schema(description = "PC명", example = "트윈치과PC-1", maxLength = 255, type = "string")
        String pcName,

        @Schema(description = "프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger)", example = "0", type = "number")
        int pgType,

        @Schema(description = "경쟁사 프로그램 ID(1:하나로 , 2:OneClick, 3:덴트웹, 4:앤드윈 , 5:제대로 , 6:아이프로 , 7:OK-Pen , 8:두번에)", example = "1", type = "number")
        int competitorId,

        @Schema(description = "경쟁사 프로그램 설치 날짜",  example = "2024-08-18 15:30:00", type = "string")
        String competitorInstallDatetime,
        @NotBlank
        @Schema(description = "작업일시", example = "2024-08-18 15:30:00", type = "string")
        String workDatetime

        ) {

    public NewCompetitorPc toNewCompetitorPc(){
        return new NewCompetitorPc(
                hospitalId,
                hospitalName,
                ip,
                macAddress,
                pcName,
                pgType,
                competitorId,
                LocalDateTime.parse(competitorInstallDatetime,  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(workDatetime,  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

    }
}
