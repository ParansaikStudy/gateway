package com.zqksk.api.domain.dentistry;

public record DentistryWithClinicHoursResponse(
        Dentistry dentistry,
        DentistryClinicHours clinicHours
) {
}
