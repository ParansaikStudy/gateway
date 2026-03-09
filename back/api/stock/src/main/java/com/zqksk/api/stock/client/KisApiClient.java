package com.zqksk.api.stock.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqksk.api.stock.config.KisProperties;
import com.zqksk.api.stock.dto.kis.KisDailyItem;
import com.zqksk.api.stock.dto.kis.KisPriceOutput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 한국투자증권 Open API 호출 (토큰, 현재가, 일봉).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KisApiClient {

    private static final String TOKEN_PATH = "/oauth2/tokenP";
    private static final String PRICE_PATH = "/uapi/domestic-stock/v1/quotations/inquire-price";
    private static final String DAILY_CHART_PATH = "/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice";
    private static final String TR_ID_PRICE = "FHKST01010100";
    private static final String TR_ID_DAILY = "FHKST03010100";

    private final KisProperties kisProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private String cachedToken;
    private Instant tokenExpiresAt;

    private String getBaseUrl() {
        return "practice".equalsIgnoreCase(kisProperties.getEnvironment())
            ? "https://openapivts.koreainvestment.com:29443"
            : "https://openapi.koreainvestment.com:9443";
    }

    public String getAccessToken() {
        if (cachedToken != null && tokenExpiresAt != null && Instant.now().isBefore(tokenExpiresAt)) {
            return cachedToken;
        }
        String url = getBaseUrl() + TOKEN_PATH;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = Map.of(
            "grant_type", "client_credentials",
            "appkey", kisProperties.getAppKey(),
            "appsecret", kisProperties.getAppSecret()
        );
        ResponseEntity<String> res = restTemplate.exchange(
            url,
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            String.class
        );
        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null || res.getBody().isBlank()) {
            throw new IllegalStateException("KIS 토큰 발급 실패");
        }
        JsonNode node = readTree(res.getBody());
        cachedToken = node.path("access_token").asText(null);
        int expiresIn = node.path("expires_in").asInt(86400);
        tokenExpiresAt = Instant.now().plusSeconds(expiresIn - 60);
        if (cachedToken == null) {
            throw new IllegalStateException("KIS access_token 없음: " + node.toString());
        }
        return cachedToken;
    }

    public KisPriceOutput inquirePrice(String stockCode) {
        String url = getBaseUrl() + PRICE_PATH + "?FID_COND_MRKT_DIV_CODE=J&FID_INPUT_ISCD=" + stockCode.trim();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + getAccessToken());
        headers.set("appkey", kisProperties.getAppKey());
        headers.set("appsecret", kisProperties.getAppSecret());
        headers.set("tr_id", TR_ID_PRICE);

        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null || res.getBody().isBlank()) {
            throw new IllegalStateException("KIS 현재가 조회 실패");
        }
        JsonNode body = readTree(res.getBody());
        if (!"0".equals(body.path("rt_cd").asText(""))) {
            throw new IllegalStateException("KIS API 오류: " + body.path("msg1").asText(""));
        }
        JsonNode output = body.path("output");
        return objectMapper.convertValue(output, KisPriceOutput.class);
    }

    /**
     * 일봉 조회 (기간: endDate 기준 과거 약 6개월).
     */
    public List<KisDailyItem> inquireDailyChart(String stockCode, String startDate, String endDate) {
        String url = getBaseUrl() + DAILY_CHART_PATH
            + "?FID_COND_MRKT_DIV_CODE=J"
            + "&FID_INPUT_ISCD=" + stockCode.trim()
            + "&FID_INPUT_DATE_1=" + startDate
            + "&FID_INPUT_DATE_2=" + endDate
            + "&FID_PERIOD_DIV_CODE=D"
            + "&FID_ORG_ADJ_PRC=0";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + getAccessToken());
        headers.set("appkey", kisProperties.getAppKey());
        headers.set("appsecret", kisProperties.getAppSecret());
        headers.set("tr_id", TR_ID_DAILY);

        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null || res.getBody().isBlank()) {
            throw new IllegalStateException("KIS 일봉 조회 실패");
        }
        JsonNode body = readTree(res.getBody());
        if (!"0".equals(body.path("rt_cd").asText(""))) {
            throw new IllegalStateException("KIS API 오류: " + body.path("msg1").asText(""));
        }
        JsonNode output2 = body.path("output2");
        List<KisDailyItem> list = new ArrayList<>();
        if (output2.isArray()) {
            for (JsonNode item : output2) {
                list.add(objectMapper.convertValue(item, KisDailyItem.class));
            }
        }
        return list;
    }

    private JsonNode readTree(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("KIS API 응답 JSON 파싱 실패: " + e.getMessage());
        }
    }
}
