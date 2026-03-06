package com.zqksk.api.domain.pc;

import lombok.Builder;

@Builder
public record InstallInfoData(
        Long id,
        String cpu,
        String memory
) {
}
