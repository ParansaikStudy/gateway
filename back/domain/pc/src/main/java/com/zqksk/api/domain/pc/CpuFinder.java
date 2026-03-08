package com.zqksk.api.domain.pc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CpuFinder {

    private final CpuRepository cpuRepository;

    public List<CpuPerformance> getCpus() {
        return cpuRepository.getCpus();
    }

    public List<CpuPerformance> getCpusByBrand(String brand) {
        return cpuRepository.getCpusByBrand(brand.toUpperCase());
    }
}
