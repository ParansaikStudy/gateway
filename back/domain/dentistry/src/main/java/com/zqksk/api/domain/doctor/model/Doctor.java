package com.zqksk.api.domain.doctor.model;

public record Doctor(
    Long id,
    Long doctorId,
    String name,
    Long licenseNumber,
    Long hospitalId,
    String hospitalName
) {
}
