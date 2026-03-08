package com.zqksk.api.domain.pc;

import lombok.Builder;

@Builder
public record NewPcScore(
    Long id,
    int totalScore
) {
}
