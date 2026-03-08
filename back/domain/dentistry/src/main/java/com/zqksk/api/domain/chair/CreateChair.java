package com.zqksk.api.domain.chair;

public record CreateChair(
    Long chairId,
    String manufacturer,
    String modelName
) {
    public Chair toChair() {
        return new Chair(
                null,
                chairId,
                manufacturer,
                modelName
        );
    }
}
