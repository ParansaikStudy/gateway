package com.zqksk.api.domain.dentistry.repository;

import com.zqksk.api.domain.dentistry.DentistryEmployee;
import com.zqksk.api.domain.dentistry.NewDentistryEmployee;

import java.util.List;

public interface DentistryEmployeeRepository {
    DentistryEmployee save(NewDentistryEmployee newDentistryEmployee);

    List<DentistryEmployee> findAllByHospitalId(String hospitalId);
}
