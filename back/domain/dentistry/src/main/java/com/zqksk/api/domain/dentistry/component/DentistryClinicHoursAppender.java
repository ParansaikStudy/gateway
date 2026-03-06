package com.zqksk.api.domain.dentistry.component;

import com.zqksk.api.domain.dentistry.DentistryClinicHours;
import com.zqksk.api.domain.dentistry.NewDentistryClinicHours;
import com.zqksk.api.domain.dentistry.component.DentistryClinicHoursRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DentistryClinicHoursAppender {

    private final DentistryClinicHoursRepository dentistryClinicHoursRepository;

    public DentistryClinicHours appendDentistryClinicHours(NewDentistryClinicHours newDentistryClinicHours) {
        return dentistryClinicHoursRepository.save(newDentistryClinicHours);
    }

    public Long deleteAllByHospitalIdList(List<String> hospotalIdList){
        return dentistryClinicHoursRepository.deleteAllByHospitalIdList(hospotalIdList);
    }
}
