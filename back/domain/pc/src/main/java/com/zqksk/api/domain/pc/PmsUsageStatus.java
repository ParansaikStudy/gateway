package com.zqksk.api.domain.pc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PmsUsageStatus {
    private Integer programType;
    private Long usageCount;
}
