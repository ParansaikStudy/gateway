package com.zqksk.api.domain.dentistry.component;

import com.zqksk.api.domain.dentistry.DentistryClinicHours;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DentistryClinicHoursFinder {

    private final DentistryClinicHoursRepository dentistryClinicHoursRepository;

    public DentistryClinicHours getDentistryClinicHours(String hospitalId) {
        return dentistryClinicHoursRepository.findByHospitalId(hospitalId);
    }

    public List<String> getAllHospitalIds(){
        return dentistryClinicHoursRepository.findAllHospitalIdList();
    }
}
