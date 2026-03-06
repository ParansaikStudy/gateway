package com.zqksk.api.domain.pc;

public record PerformanceSpec(
    int softwareId,
    int workType,
    int minScore,
    int recScore,
    int maxScore
) {
}
