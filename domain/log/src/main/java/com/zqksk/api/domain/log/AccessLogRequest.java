package com.zqksk.api.domain.log;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AccessLogRequest(
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

    @Schema(description = "IP 주소", example = "192.168.0.111", maxLength = 20, type = "string")
    String ip,

    @Schema(description = "PC MAC 주소", example = "AA-BB-CC-DD-EE-FF", maxLength = 100, type = "string")
    String macAddress,

    @Schema(description = "PC명", example = "1", maxLength = 20, type = "string")
    String pcName,

    @NotNull
    @Schema(description = "이벤트 타입(0: 기본, 11: 전환 속도, 12: 동기화)", example = "0", allowableValues = {"0", "11", "12"}, type = "number")
    int eventType,

    @Schema(description = "대메뉴", maxLength = 36, type = "string")
    String rootMenu,

    @Schema(description = "화면ID", maxLength = 36, type = "string")
    String viewId,

    @Schema(description = "화면명", maxLength = 255, type = "string")
    String viewName,

    @NotBlank
    @Schema(description = "기능", example = "접수", type = "string")
    String functionName,

    @Schema(description = "요소", example = "버튼", type = "string")
    String elementName,

    @Schema(description = "동작(0: Click, 1: Search, 2: Add, 3: Remove, 4: Modify)", example = "0", type = "string")
    int action,

    @Schema(description = "설명", example = "요소의 동작 설명 예) '데스크 접수 버튼 클릭' 등", allowableValues = {"0", "1"}, type = "string")
    String description,

    @Schema(description = "로딩 속도(s)", example = "예) 0.15, 20000 (eventType: 11 사용, 단위: 초)", type = "string")
    String loadingSpeed,

    @Schema(description = "비고", type = "string")
    String remark,

    @Schema(description = "버전", example = "15.0.0.1", maxLength = 20, type = "string")
    String version,

    @NotNull
    @Schema(description = "프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger)", example = "0", allowableValues = {"0", "1", "2", "3", "4"}, type = "number")
    int programType
) {
    public NewAccessLog toNewAccessLog() {
        return new NewAccessLog(
            hospitalId,
            hospitalName,
            LocalDateTime.parse(workDatetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            userId,
            ip,
            macAddress,
            pcName,
            eventType,
            rootMenu,
            viewId,
            viewName,
            functionName,
            elementName,
            action,
            eventType != 12 ? description : null,
                loadingSpeed,
            remark,
            version,
            programType
        );
    }
}
