package com.zqksk.api.domain;

public record CustomerDetail(
    Long customerId,
    String cellphoneNumber,
    String telephoneNumber,
    String email,
    Long zipNumber,
    String jibunAddress,
    String jibunAddressDetail,
    String streetAddress,
    String streetAddressDetail,
    String representativeLicenseNumber,
    String representativeName
) {
}
