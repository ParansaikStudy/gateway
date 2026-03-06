package com.zqksk.api.domain.pc;

import lombok.Builder;

@Builder
public record PcRegistrationEvent(
    Long id,
    String cpu,
    String memory
) {
}
