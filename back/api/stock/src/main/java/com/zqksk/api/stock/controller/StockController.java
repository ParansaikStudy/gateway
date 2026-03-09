package com.zqksk.api.stock.controller;

import com.zqksk.api.stock.dto.analysis.AnalysisRequest;
import com.zqksk.api.stock.dto.analysis.AnalysisResponse;
import com.zqksk.api.stock.dto.kakao.KakaoSkillRequest;
import com.zqksk.api.stock.dto.kakao.KakaoSkillResponse;
import com.zqksk.api.stock.service.KakaoSkillService;
import com.zqksk.api.stock.service.StockAnalysisService;
import com.zqksk.api.stock.service.StockGeminiAnalysisService;
import com.zqksk.api.support.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 프론트엔드 분석 요청을 받아 분석 결과(summary, fullAnalysis)를 반환합니다.
 * - POST /api/stock/analysis : 기존 규칙 기반 분석
 * - POST /api/stock/analysis/gemini : 한투 실시간 + 탑100 파일 → Gemini 결론 (3문장)
 * - POST /api/stock/kakao/skill : 카카오 스킬 전용 (utterance → 탑100에서 종목 검색 후 분석)
 */
@Slf4j
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockAnalysisService stockAnalysisService;
    private final StockGeminiAnalysisService stockGeminiAnalysisService;
    private final KakaoSkillService kakaoSkillService;

    @PostMapping("/analysis")
    public ResponseEntity<ApiResponse> analysis(@Valid @RequestBody AnalysisRequest request) {
        return ResponseEntity.ok(ApiResponse.of(stockAnalysisService.analyze(request)));
    }

    /** 한투 실시간 + 탑100 분석 파일 → Gemini 3문장 결론. body: {"stockCode":"005930"} */
    @PostMapping("/analysis/gemini")
    public ResponseEntity<AnalysisResponse> analysisWithGemini(@Valid @RequestBody AnalysisRequest request) {
        String stockCode = request != null ? request.getStockCode() : null;
        AnalysisResponse response = stockGeminiAnalysisService.analyzeWithGemini(stockCode);
        return ResponseEntity.ok(response);
    }

    /**
     * 카카오 스킬 URL에 대한 GET/HEAD (모니터링·브라우저 접근 시 405 방지). 실제 스킬은 POST로 호출.
     */
    @RequestMapping(value = "/kakao/skill", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<KakaoSkillResponse> kakaoSkillGetOrHead() {
        return ResponseEntity.ok(KakaoSkillResponse.ofText("카카오 스킬은 POST로 호출해 주세요."));
    }

    @PostMapping("/kakao/skill")
    public ResponseEntity<KakaoSkillResponse> kakaoSkill(@RequestBody KakaoSkillRequest request) {
        try {
            String utterance = request != null && request.getUserRequest() != null
                ? request.getUserRequest().getUtterance()
                : null;
            String text = kakaoSkillService.analyzeFromUtterance(utterance);
            return ResponseEntity.ok(KakaoSkillResponse.ofText(text));
        } catch (Exception e) {
            log.error("카카오 스킬 처리 중 오류: utterance={}, error={}", request != null && request.getUserRequest() != null ? request.getUserRequest().getUtterance() : null, e.getMessage(), e);
            return ResponseEntity.ok(KakaoSkillResponse.ofText(
                "일시적인 오류가 발생했습니다. (KIS/Gemini 설정 또는 네트워크를 확인해 주세요.) 잠시 후 다시 시도해 주세요."));
        }
    }
}
