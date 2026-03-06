package com.zqksk.api.domain.doctor.model;

public record CreateDoctor(
    Long doctorId,
    String name,
    Long licenseNumber,
    Long hospitalId,
    String hospitalName
) {
    public Doctor toDoctor() {
        return new Doctor(null, doctorId, name, licenseNumber, hospitalId, hospitalName);
    }
}
