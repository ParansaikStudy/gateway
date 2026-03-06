package com.zqksk.api.domain.dentistry;

import lombok.Builder;

@Builder
public record DentistryClinicHours(
    Long id,
    String hospitalId,
    String  treatTimeTerms,
    String lunchTimeTerms,
    String dinnerTimeTerms
) {
}
