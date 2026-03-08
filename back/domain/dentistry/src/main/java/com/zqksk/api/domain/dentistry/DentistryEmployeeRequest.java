package com.zqksk.api.domain.dentistry;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record DentistryEmployeeRequest(
        @NotBlank
        @Schema(description = "요양기관번호", example = "00000000", maxLength = 15, type = "string")
        String hospitalId,

        @NotNull
        @Schema(description = "프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger, 5: OneServer, 6: One2 \" +\n" +
                "                \"7: One3 ,8: V-ceph, 9: OneClickM, 10: OneDeskM, 11: OnePhoto, 12: OneChartScanM, 13: OneCodiM , 14: DBMigration)", example = "0", type = "number")
        Integer pgType,

        @Schema(description = "직원번호", example = "D0001", type = "string")
        String employeeNo,

        @NotBlank
        @Schema(description = "직원이름", example = "홍길동", type = "string")
        String employeeName,

        @Schema(description = "진료과", example = "보철과", type = "string")
        String medicalSpecialty,

        @Schema(description = "프로필 사진", example = "file", type = "object")
        byte[] profile
) {
}
