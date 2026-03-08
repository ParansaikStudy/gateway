package com.zqksk.api.domain.dentistry;

public record DentistryAndClinicHoursRequest(
      DentistryRequest dentistryRequest,
      DentistryClinicHoursRequest dentistryClinicHoursRequest
) {
}
