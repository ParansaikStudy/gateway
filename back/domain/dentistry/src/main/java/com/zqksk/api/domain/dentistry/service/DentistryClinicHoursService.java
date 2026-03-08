package com.zqksk.api.domain.dentistry.service;

import com.zqksk.api.domain.dentistry.DentistryAndClinicHours;
import com.zqksk.api.domain.dentistry.DentistryClinicHours;
import com.zqksk.api.domain.dentistry.DentistryClinicHoursRequest;

import java.util.List;

public interface DentistryClinicHoursService {
    default DentistryClinicHours insertDentistryClinicHours(DentistryClinicHoursRequest dentistryClinicHoursRequest) {
        throw new UnsupportedOperationException("Not implemented");
    };

    default DentistryAndClinicHours getDentistryClinicHours(String hospitalId) {
        throw new UnsupportedOperationException("Not implemented");
    };

    default Long deleteDentistryClinicHoursByHospitalIdList(List<String> hoursHospitalIds){
        throw new UnsupportedOperationException("Not implemented");
    }
}
