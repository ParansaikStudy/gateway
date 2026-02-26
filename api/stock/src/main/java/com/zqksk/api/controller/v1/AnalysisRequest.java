package com.zqksk.api.controller.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AnalysisRequest(
    @JsonProperty("stockCode") String stockCode,
    @JsonProperty("stockName") String stockName
) {
    public AnalysisRequest {
        if (stockCode == null) stockCode = "";
        if (stockName == null) stockName = "";
    }
}
