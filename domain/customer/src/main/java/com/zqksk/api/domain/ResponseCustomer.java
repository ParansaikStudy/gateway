package com.zqksk.api.domain;

public record ResponseCustomer(
        String name,
        String logoFileId,
        String cellphoneNumber,
        String telephoneNumber,
        Long zipNumber,
        String jibunAddress,
        String jibunAddressDetail,
        String streetAddress,
        String streetAddressDetail,
        String representativeName,
        Double latitude,
        Double longitude
) {
}
