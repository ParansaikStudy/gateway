package com.zqksk.api.domain.dentistry;

import lombok.Builder;

@Builder
public record NewDentistryClinicHours(
    String hospitalId,
    String  treatTimeTerms,
    String lunchTimeTerms,
    String dinnerTimeTerms
) {
}
