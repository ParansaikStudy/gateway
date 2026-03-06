package com.zqksk.api.domain.log;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record LogRequest(
    @NotBlank
    @Schema(description = "요양기관번호", maxLength = 15, type = "string")
    String hospitalId,

    @NotBlank
    @Schema(description = "병원명",  maxLength = 50, type = "string")
    String hospitalName,

    @NotBlank
    @Schema(description = "작업일시", example = "2024-08-18 15:30:00", type = "string")
    String workDatetime,

    @Schema(description = "사용자ID", maxLength = 255, type = "string")
    String userId,

    @Schema(description = "환자 UID", maxLength = 20, type = "string")
    String patientUid,

    @Schema(description = "IP 주소", example = "192.168.0.111", maxLength = 20, type = "string")
    String ip,

    @Schema(description = "PC MAC 주소", example = "AA-BB-CC-DD-EE-FF", maxLength = 100, type = "string")
    String macAddress,

    @Schema(description = "PC명", example = "1", maxLength = 20, type = "string")
    String pcName,

    @NotNull
    @Schema(description = "이벤트 타입(0: Info, 1: Debug, 2: Warning, 3: Error, 4: Fatal)", example = "0", allowableValues = {"0", "1", "2", "3", "4"}, type = "number")
    int eventType,

    @Schema(description = "대메뉴", maxLength = 36, type = "string")
    String rootMenu,

    @Schema(description = "화면ID", maxLength = 36, type = "string")
    String viewId,

    @Schema(description = "화면명", maxLength = 255, type = "string")
    String viewName,

    @Schema(description = "오류 코드", maxLength = 100, type = "string")
    String errorCode,

    @NotBlank
    @Schema(description = "로그", type = "string")
    String log,

    @Schema(description = "로그 상세", type = "string")
    String logDetail,

    @Schema(description = "로그 기타", type = "string")
    String etc,

    @Schema(description = "클라이언트/서버 구분(0: Server, 1: Client)", example = "0", allowableValues = {"0", "1"}, type = "number")
    int workType,

    @Schema(description = "버전", example = "15.0.0.1", maxLength = 20, type = "string")
    String version,

    @Schema(description = "프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger)", example = "0", allowableValues = {"0", "1", "2", "3", "4"}, type = "number")
    int programType
) {
    public NewLog toNewLog() {
        return new NewLog(
            hospitalId,
            hospitalName,
            LocalDateTime.parse(workDatetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            userId,
            patientUid,
            ip,
            macAddress,
            pcName,
            eventType,
            rootMenu,
            viewId,
            viewName,
            errorCode,
            log,
            logDetail,
            etc,
            workType,
            version,
            programType
        );
    }
}
