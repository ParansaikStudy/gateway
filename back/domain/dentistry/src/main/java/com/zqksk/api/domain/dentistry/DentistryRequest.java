package com.zqksk.api.domain.dentistry;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DentistryRequest(
        @NotBlank
        @Schema(description = "요양기관번호", example = "00000000", maxLength = 15, type = "string")
        String hospitalId,

        @NotBlank
        @Schema(description = "병원명", example = "오스템치과", maxLength = 40, type = "string")
        String hospitalName,

        @Schema(description = "우편번호", example = "07789", type = "string")
        String zipCode,

        @Schema(description = "주소", example = "서울시 강서구 마곡중앙 12로 3", type = "string")
        String address,

        @Schema(description = "전화번호", example = "070-0000-0000", type = "string")
        String telephone,

        @Schema(description = "의사 수", example = "2", type = "number")
        Integer doctorCount,

        @Schema(description = "직원 수", example = "3", type = "number")
        Integer employeeCount,

        @Schema(description = "전자서명 여부", example = "Y", type = "string")
        String electronicSignatureYn,

        @Schema(description = "db가용량", example = "8.6GB/10GB", type = "string")
        String dbUsableCapacity,

        @Schema(description = "사업자등록번호", example = "1234567890", type = "string")
        String businessRegistrationNumber,

        @Schema(description = "프로그램 구분(0: 두번에, 1: 하나로, 2: OneClick, 3: OneCodi, 4: OneMessenger, 5: OneServer, 6: One2  " +
                "7 : One3 ,8: V-ceph, 9: OneClickM, 10: OneDeskM, 11: OnePhoto, 12: OneChartScanM, 13: OneCodiM , 14: DBMigration)  기본값 : -1",
                example = "1",
                allowableValues = {"-1","0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" },
                type = "number")
        Integer pgType


) {
}
