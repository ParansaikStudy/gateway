package com.zqksk.api.domain;

public record CustomerLocationJob(
    String cimsBusinessmanCustomerCode,
    double latitude,
    double longitude,
    String logoFileId,
    String isProgress
) {
}

