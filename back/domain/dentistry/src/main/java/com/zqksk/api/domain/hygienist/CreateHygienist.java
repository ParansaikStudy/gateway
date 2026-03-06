package com.zqksk.api.domain.hygienist;

public record CreateHygienist(
    Long hygenistId,
    String name,
    Long licenseNumber,
    Long hospitalId,
    String hospitalName
) {
    public Hygienist toHygienist() {
        return new Hygienist(null, hygenistId, name, licenseNumber, hospitalId, hospitalName);
    }
}
