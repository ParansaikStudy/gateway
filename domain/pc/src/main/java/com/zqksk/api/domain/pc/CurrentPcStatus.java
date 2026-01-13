package com.zqksk.api.domain.pc;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrentPcStatus {
    private String hospitalId;
    private String hospitalName;
    private Integer usedProgram;
    private Long serverCount;
    private Long clientCount;
}
