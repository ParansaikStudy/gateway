package com.zqksk.api.domain;

public record CustomerLocation(
    String cimsBusinessmanCustomerCode,
    double latitude,
    double longitude,
    String logoFileId
) {
}
