package com.zqksk.api.controller.v1;

import com.zqksk.api.service.SmartAnalysisService;
import com.zqksk.api.support.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/stock/v1")
public class StockAnalysisController {

    private final SmartAnalysisService smartAnalysisService;

    @PostMapping("/analysis")
    public ResponseEntity<ApiResponse> requestAnalysis(@RequestBody AnalysisRequest request) {
        if (request.stockCode() == null || request.stockCode().isBlank()) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.of("stockCode는 필수입니다.", null));
        }
        var result = smartAnalysisService.analyze(request.stockCode(), request.stockName());
        return ResponseEntity.ok(ApiResponse.of(AnalysisResponse.from(result)));
    }
}
