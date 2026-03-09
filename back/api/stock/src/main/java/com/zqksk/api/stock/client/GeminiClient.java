package com.zqksk.api.stock.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zqksk.api.stock.config.GeminiProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Google Gemini API generateContent 호출.
 * REST: generativelanguage.googleapis.com/v1/models/{model}:generateContent
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    private static final String BASE = "https://generativelanguage.googleapis.com/v1/models";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final GeminiProperties geminiProperties;
    private final RestTemplate restTemplate;

    /**
     * 프롬프트 한 개로 텍스트 생성. 응답의 첫 번째 candidate text 반환.
     */
    public String generateContent(String prompt) {
        if (geminiProperties.isConfigured()) {
            throw new IllegalStateException("Gemini API 키가 설정되지 않았습니다. gemini.api-key 또는 GEMINI_API_KEY를 설정하세요.");
        }
        String url = BASE + "/" + geminiProperties.getModel() + ":generateContent?key=" + geminiProperties.getApiKey();
        ObjectNode root = MAPPER.createObjectNode();
        ArrayNode contents = root.putArray("contents");
        ObjectNode content = contents.addObject();
        ArrayNode parts = content.putArray("parts");
        parts.addObject().put("text", prompt);
        String jsonBody;
        try {
            jsonBody = MAPPER.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Gemini 요청 JSON 생성 실패: " + e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> res = restTemplate.exchange(
            url,
            org.springframework.http.HttpMethod.POST,
            new HttpEntity<>(jsonBody, headers),
            String.class
        );
        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null || res.getBody().isBlank()) {
            throw new IllegalStateException("Gemini API 호출 실패: " + res.getStatusCode());
        }
        JsonNode body;
        try {
            body = MAPPER.readTree(res.getBody());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Gemini 응답 JSON 파싱 실패: " + e.getMessage());
        }
        JsonNode candidates = body.path("candidates");
        if (candidates.isEmpty()) {
            String reason = body.path("promptFeedback").path("blockReason").asText("");
            throw new IllegalStateException("Gemini 응답 후보 없음. blockReason=" + reason);
        }
        JsonNode partsOut = candidates.get(0).path("content").path("parts");
        if (partsOut.isEmpty()) {
            return "";
        }
        String text = partsOut.get(0).path("text").asText("");
        return text != null ? text.trim() : "";
    }
}
