package com.zqksk.api.domain.doctor.component;

import com.zqksk.api.domain.doctor.repository.DoctorRepository;
import com.zqksk.api.domain.doctor.model.CreateDoctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorAppender {
    private final DoctorRepository doctorRepository;

    public DoctorAppender(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public void save(CreateDoctor doctor) {
        doctorRepository.save(doctor.toDoctor());
    }
}
