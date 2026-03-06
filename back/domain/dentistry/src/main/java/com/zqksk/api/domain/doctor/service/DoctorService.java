package com.zqksk.api.domain.doctor.service;

import com.zqksk.api.domain.doctor.component.DoctorAppender;
import com.zqksk.api.domain.doctor.model.CreateDoctor;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    private final DoctorAppender doctorAppender;

    public DoctorService(DoctorAppender doctorAppender) {
        this.doctorAppender = doctorAppender;
    }

    public void save(CreateDoctor doctor) {
        doctorAppender.save(doctor);
    }
}
