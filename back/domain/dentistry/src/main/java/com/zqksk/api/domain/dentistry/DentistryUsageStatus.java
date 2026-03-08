package com.zqksk.api.domain.dentistry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DentistryUsageStatus {
    private Integer programType;
    private Long usageCount;
    private Long contractCount;
}
