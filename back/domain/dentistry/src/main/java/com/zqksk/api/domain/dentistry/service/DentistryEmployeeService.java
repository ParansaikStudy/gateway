package com.zqksk.api.domain.dentistry.service;

import com.zqksk.api.domain.dentistry.DentistryEmployee;
import com.zqksk.api.domain.dentistry.DentistryEmployeeRequest;

import java.util.List;

public interface DentistryEmployeeService {
    default void insertDentistryEmployee(List<DentistryEmployeeRequest> dentistryEmployeeRequests) {
        throw new UnsupportedOperationException("Not implemented");
    }

    default List<DentistryEmployee> getDentistryEmployees(String hospitalId) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
