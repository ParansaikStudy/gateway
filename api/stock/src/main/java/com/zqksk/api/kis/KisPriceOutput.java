package com.zqksk.api.kis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KisPriceOutput(
    @JsonProperty("stck_prpr") String stckPrpr,
    @JsonProperty("prdy_vrss") String prdyVrss,
    @JsonProperty("prdy_ctrt") String prdyCtrt
) {}
