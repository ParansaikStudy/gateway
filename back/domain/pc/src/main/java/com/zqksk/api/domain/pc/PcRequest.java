package com.zqksk.api.domain.pc;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record PcRequest(
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

    @Schema(description = "PC명", example = "트윈치과PC-1", maxLength = 20, type = "string")
    String pcName,

    @Schema(description = "CPU", example = "i7-1165G7", maxLength = 100, type = "string")
    String cpu,

    @Schema(description = "Memory", example = "32GB", maxLength = 100, type = "string")
    String memory,

    @Schema(description = "GPU", example = "NVIDIA GeForce GTX1650 Ti", maxLength = 100, type = "string")
    String gpu,

    @Schema(description = "OS", example = "Windows 11 Pro", maxLength = 100, type = "string")
    String os,

    @Schema(description = "클라이언트/서버 구분", example = "0", type = "number")
    int workType,

    @Schema(description = "프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger, 5: OneServer, 6: One2, " +
            "7: One3 ,8: V-ceph, 9: OneClickM, 10: OneDeskM, 11: OnePhoto, 12: OneChartScanM, 13: OneCodiM  , 14: DBMigration)", example = "0", type = "number")
    int pgType,

    @Schema(description = "버전", example = "15.0.0.1", maxLength = 20, type = "string")
    String version,

    @Schema(description = "모니터 해상도", example = "2560x1440,1920x1080", maxLength = 40, type = "string")
    String monResol,

    @NotBlank
    @Schema(description = "최초작업일시", example = "2024-08-18 15:30:00", type = "string")
    String firstDatetime,

    @NotBlank
    @Schema(description = "최종작업일시", example = "2024-08-18 15:30:00", type = "string")
    String lastDatetime
) {
    public PcRequest {
        if (firstDatetime == null || firstDatetime.isBlank()) {
            firstDatetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (lastDatetime == null || lastDatetime.isBlank()) {
            lastDatetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    public NewPc toNewPc() {
        return new NewPc(
                hospitalId,
                hospitalName,
                ip,
                macAddress,
                pcName,
                cpu,
                memory,
                gpu,
                os,
                workType,
                pgType,
                version,
                0,
                monResol,
                LocalDateTime.parse(firstDatetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(lastDatetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}
