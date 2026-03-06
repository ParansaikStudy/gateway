package com.zqksk.api.domain.dentistry.component;

import com.zqksk.api.domain.dentistry.DentistryClinicHours;
import com.zqksk.api.domain.dentistry.NewDentistryClinicHours;

import java.util.List;

public interface DentistryClinicHoursRepository {
    DentistryClinicHours save(NewDentistryClinicHours newDentistryClinicHours);
    DentistryClinicHours findByHospitalId(String hospitalId);

    List<String> findAllHospitalIdList();

    Long deleteAllByHospitalIdList(List<String> hospitalIds);
}
