package com.zqksk.api.domain;

public record Customer(
    Long id,
    Long corporateRegistrationNumber,
    Long dentalId,
    String name,
    String sidoCode,
    String statusCode,
    String logoFileId,
    Double latitude,
    Double longitude
) {
}
