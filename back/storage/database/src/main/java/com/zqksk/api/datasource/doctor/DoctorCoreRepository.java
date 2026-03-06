package com.zqksk.api.datasource.doctor;

import com.zqksk.api.domain.doctor.model.Doctor;
import com.zqksk.api.domain.doctor.repository.DoctorRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorCoreRepository implements DoctorRepository {

    private final DoctorJpaRepository doctorJpaRepository;

    public DoctorCoreRepository(DoctorJpaRepository doctorJpaRepository) {
        this.doctorJpaRepository = doctorJpaRepository;
    }

    @Override
    public void save(Doctor doctor) {
        doctorJpaRepository.save(new DoctorEntity(
                doctor.doctorId(),
                doctor.name(),
                doctor.licenseNumber(),
                doctor.hospitalId(),
                doctor.hospitalName())
        );
    }
}
