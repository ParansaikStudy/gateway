package com.zqksk.api.domain.dentistry.component;

import com.zqksk.api.domain.dentistry.DentistryEmployee;
import com.zqksk.api.domain.dentistry.repository.DentistryEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DentistryEmployeeFinder {

    private final DentistryEmployeeRepository dentistryEmployeeRepository;

    public List<DentistryEmployee> getDentistryEmployees(String hospitalId) {
        return dentistryEmployeeRepository.findAllByHospitalId(hospitalId);
    }


}
