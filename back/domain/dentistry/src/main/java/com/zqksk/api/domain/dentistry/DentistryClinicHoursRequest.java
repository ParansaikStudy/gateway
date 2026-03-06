package com.zqksk.api.domain.dentistry;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DentistryClinicHoursRequest(
    @NotBlank
    @Schema(description = "요양기관번호", example = "00000000", maxLength = 15, type = "string")
    String hospitalId,

    @NotBlank
    @Schema(description = "진료시간(월|화|수|목|금|토|일)", example = "10001900|10001900|09301900|10002000|10001900|09301300|09301300", maxLength = 62, type = "string")
    String  treatTimeTerms,

    @NotBlank
    @Schema(description = "점심시간(월|화|수|목|금|토|일)", example = "12001300|12001300|12001300|12001300|12001300|00000000|00000000", maxLength = 62, type = "string")
    String lunchTimeTerms,

    @NotBlank
    @Schema(description = "저녁시간(월|화|수|목|금|토|일)", example = "00000000|00000000|00000000|18001900|00000000|00000000|00000000", maxLength = 62, type = "string")
    String dinnerTimeTerms
) {
}
