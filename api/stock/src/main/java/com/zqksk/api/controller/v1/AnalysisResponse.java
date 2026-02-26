package com.zqksk.api.controller.v1;

import com.zqksk.api.service.SmartAnalysisService;

public record AnalysisResponse(
    String summary,
    String fullAnalysis
) {
    public static AnalysisResponse from(SmartAnalysisService.AnalysisResult result) {
        return new AnalysisResponse(result.summary(), result.fullAnalysis());
    }
}
