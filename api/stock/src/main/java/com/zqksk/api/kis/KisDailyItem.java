package com.zqksk.api.kis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KisDailyItem(
    @JsonProperty("stck_bsop_date") String stckBsopDate,
    @JsonProperty("stck_clpr") String stckClpr,
    @JsonProperty("stck_oprc") String stckOprc,
    @JsonProperty("stck_hgpr") String stckHgpr,
    @JsonProperty("stck_lwpr") String stckLwpr
) {}
