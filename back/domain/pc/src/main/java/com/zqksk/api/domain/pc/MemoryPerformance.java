package com.zqksk.api.domain.pc;

public record MemoryPerformance(
        String model,
        int capacity,
        double memoryCoefficient
) {
}
