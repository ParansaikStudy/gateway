package com.zqksk.api.domain.dentistry;

import lombok.Builder;

@Builder
public record DentistryAndClinicHours(
    String hospitalId,
    String hospitalName,
    String treatTimeTerms,
    String lunchTimeTerms,
    String dinnerTimeTerms
) {
}
