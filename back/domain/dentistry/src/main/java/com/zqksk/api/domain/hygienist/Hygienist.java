package com.zqksk.api.domain.hygienist;

public record Hygienist(
    Long id,
    Long hygenistId,
    String name,
    Long licenseNumber,
    Long hospitalId,
    String hospitalName
) {
}
