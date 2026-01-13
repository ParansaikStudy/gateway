package com.zqksk.api.domain.pc;

import java.util.List;
import java.util.Optional;

public interface CpuRepository {
    List<CpuPerformance> getCpus();
    List<CpuPerformance> getCpusByBrand(String brand);
}
