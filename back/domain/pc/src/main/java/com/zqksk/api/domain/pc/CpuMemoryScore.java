package com.zqksk.api.domain.pc;

import lombok.Builder;

@Builder
public record CpuMemoryScore(
        Long id,
        String cpuName,
        String cpuModel,
        String memory,
        double cpuScore,
        double memoryScore,
        int totalScore,
        int unitCount
) {
}
