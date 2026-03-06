package com.zqksk.api.domain.dentistry.component;

import com.zqksk.api.domain.dentistry.DentistryEmployee;
import com.zqksk.api.domain.dentistry.NewDentistryEmployee;
import com.zqksk.api.domain.dentistry.repository.DentistryEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DentistryEmployeeAppender {

    private final DentistryEmployeeRepository dentistryEmployeeRepository;

    public DentistryEmployee appendDentistryEmployee(NewDentistryEmployee newDentistry) {
        return dentistryEmployeeRepository.save(newDentistry);
    }
}
