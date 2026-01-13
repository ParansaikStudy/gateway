package com.zqksk.api.domain.chair;

public record Chair(
    Long id,
    Long chairId,
    String manufacturer,
    String modelName
) {
}
