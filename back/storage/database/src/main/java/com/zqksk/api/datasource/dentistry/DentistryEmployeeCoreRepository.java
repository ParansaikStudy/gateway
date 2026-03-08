package com.zqksk.api.datasource.dentistry;

import com.zqksk.api.domain.dentistry.DentistryEmployee;
import com.zqksk.api.domain.dentistry.repository.DentistryEmployeeRepository;
import com.zqksk.api.domain.dentistry.NewDentistryEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DentistryEmployeeCoreRepository implements DentistryEmployeeRepository {

    private final DentistryEmployeeJpaRepository dentistryEmployeeJpaRepository;

    @Override
    public DentistryEmployee save(NewDentistryEmployee newDentistryEmployee) {
        DentistryEmployeeEntity dentistryEmployeeEntity = dentistryEmployeeJpaRepository.findByHospitalIdAndPgTypeAndEmployeeNo(newDentistryEmployee.hospitalId(), newDentistryEmployee.pgType(), newDentistryEmployee.employeeNo())
                .orElse(null);

        if(dentistryEmployeeEntity != null) {
            return dentistryEmployeeJpaRepository.save(
                    dentistryEmployeeEntity.update(
                            dentistryEmployeeEntity.getHospitalId(),
                            dentistryEmployeeEntity.getPgType(),
                            dentistryEmployeeEntity.getEmployeeNo(),
                            newDentistryEmployee.employeeName(),
                            newDentistryEmployee.medicalSpecialty(),
                            newDentistryEmployee.profile()
                    )
            ).toDentistryEmployee();
        }

        return dentistryEmployeeJpaRepository.save(DentistryEmployeeEntity.builder()
                .hospitalId(newDentistryEmployee.hospitalId())
                .pgType(newDentistryEmployee.pgType())
                .employeeNo(newDentistryEmployee.employeeNo())
                .employeeName(newDentistryEmployee.employeeName())
                .medicalSpecialty(newDentistryEmployee.medicalSpecialty())
                .profile(newDentistryEmployee.profile())
                .build()
        ).toDentistryEmployee();
    }

    @Override
    public List<DentistryEmployee> findAllByHospitalId(String hospitalId) {
        return dentistryEmployeeJpaRepository.findAllByHospitalId(hospitalId)
                .stream()
                .map(DentistryEmployeeEntity::toDentistryEmployee)
                .toList();
    }
}
