package com.zqksk.api.stock.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zqksk.api.stock.config.KisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 한국투자증권 Open API 해외주식 시세 (현재가, 기간별시세).
 * KIS 해외 API 응답 필드명(last, clos, open 등)을 국내와 동일한 KisPriceOutput/KisDailyItem으로 매핑.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KisOverseasApiClient {

    private static final String PRICE_PATH = "/uapi/overseas-price/v1/quotations/price";
    private static final String DAILY_PRICE_PATH = "/uapi/overseas-price/v1/quotations/dailyprice";
    private static final String TR_ID_PRICE = "HHDFS00000300";
    private static final String TR_ID_DAILY = "HHDFS76240000";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final KisApiClient kisApiClient;
    private final KisProperties kisProperties;
    private final RestTemplate restTemplate;

    private String getBaseUrl() {
        return "practice".equalsIgnoreCase(kisProperties.getEnvironment())
            ? "https://openapivts.koreainvestment.com:29443"
            : "https://openapi.koreainvestment.com:9443";
    }

    /**
     * 해외주식 현재체결가 조회. 응답을 KisPriceOutput 형태로 매핑.
     * @param excd 거래소코드 (NAS, NYS, AMS 등)
     * @param symb 종목코드 (AAPL, TSLA 등)
     */
    public KisPriceOutput inquireOverseasPrice(String excd, String symb) {
        String url = getBaseUrl() + PRICE_PATH
            + "?AUTH=&EXCD=" + excd.trim() + "&SYMB=" + symb.trim();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + kisApiClient.getAccessToken());
        headers.set("appkey", kisProperties.getAppKey());
        headers.set("appsecret", kisProperties.getAppSecret());
        headers.set("tr_id", TR_ID_PRICE);

        ResponseEntity<String> res = restTemplate.exchange(
            url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null || res.getBody().isBlank()) {
            throw new IllegalStateException("KIS 해외 현재가 조회 실패");
        }
        JsonNode body = readTree(res.getBody());
        if (!"0".equals(body.path("rt_cd").asText(""))) {
            throw new IllegalStateException("KIS 해외 API 오류: " + body.path("msg1").asText(""));
        }
        JsonNode out = body.path("output");
        if (out.isArray() && out.size() > 0) {
            out = out.get(0);
        }
        return mapOverseasPriceToKis(out);
    }

    /**
     * 해외주식 기간별시세(일봉) 조회. output2를 KisDailyItem 리스트로 매핑.
     * @param excd 거래소코드
     * @param symb 종목코드
     * @param endDate 종료일 YYYYMMDD (조회기준일자)
     */
    public List<KisDailyItem> inquireOverseasDailyChart(String excd, String symb, String endDate) {
        String url = getBaseUrl() + DAILY_PRICE_PATH
            + "?AUTH=&EXCD=" + excd.trim()
            + "&SYMB=" + symb.trim()
            + "&GUBN=0&BYMD=" + endDate + "&MODP=0";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", "Bearer " + kisApiClient.getAccessToken());
        headers.set("appkey", kisProperties.getAppKey());
        headers.set("appsecret", kisProperties.getAppSecret());
        headers.set("tr_id", TR_ID_DAILY);

        ResponseEntity<String> res = restTemplate.exchange(
            url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        if (!res.getStatusCode().is2xxSuccessful() || res.getBody() == null || res.getBody().isBlank()) {
            throw new IllegalStateException("KIS 해외 기간별시세 조회 실패");
        }
        JsonNode body = readTree(res.getBody());
        if (!"0".equals(body.path("rt_cd").asText(""))) {
            throw new IllegalStateException("KIS 해외 API 오류: " + body.path("msg1").asText(""));
        }
        JsonNode output2 = body.path("output2");
        List<KisDailyItem> list = new ArrayList<>();
        if (output2.isArray()) {
            for (JsonNode item : output2) {
                list.add(mapOverseasDailyToKis(item));
            }
        }
        return list;
    }

    /** 해외 현재가 응답(last, sign, diff, rate, base) → KisPriceOutput */
    private static KisPriceOutput mapOverseasPriceToKis(JsonNode out) {
        KisPriceOutput p = new KisPriceOutput();
        String last = text(out, "last");
        String base = text(out, "base");
        String diff = text(out, "diff");
        String rate = text(out, "rate");
        String sign = text(out, "sign");
        // prdy_vrss_sign: 2=상승, 5=하락, 3=보합 (국내와 동일)
        if (sign == null || sign.isEmpty()) {
            double d = parseDoubleSafe(diff);
            sign = d > 0 ? "2" : (d < 0 ? "5" : "3");
        } else if ("+".equals(sign) || "2".equals(sign)) {
            sign = "2";
        } else if ("-".equals(sign) || "5".equals(sign)) {
            sign = "5";
        } else {
            sign = "3";
        }
        p.setStck_prpr(last);
        p.setPrdy_vrss(diff != null ? diff : "0");
        p.setPrdy_vrss_sign(sign);
        p.setPrdy_ctrt(rate != null ? rate : "0");
        p.setStck_oprc(last);
        p.setStck_hgpr(last);
        p.setStck_lwpr(last);
        return p;
    }

    /** 해외 기간별시세 output2 한 건(xymd, clos, open, high, low) → KisDailyItem */
    private static KisDailyItem mapOverseasDailyToKis(JsonNode item) {
        KisDailyItem d = new KisDailyItem();
        d.setStck_bsop_date(text(item, "xymd"));
        d.setStck_clpr(text(item, "clos"));
        d.setStck_oprc(text(item, "open"));
        d.setStck_hgpr(text(item, "high"));
        d.setStck_lwpr(text(item, "low"));
        d.setPrdy_vrss(text(item, "diff"));
        d.setPrdy_vrss_sign(text(item, "sign"));
        return d;
    }

    private static JsonNode readTree(String json) {
        try {
            return MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("KIS 해외 API 응답 JSON 파싱 실패: " + e.getMessage());
        }
    }

    private static String text(JsonNode n, String key) {
        if (n == null) return null;
        JsonNode v = n.path(key);
        return v.isMissingNode() ? null : v.asText(null);
    }

    private static double parseDoubleSafe(String s) {
        if (s == null || s.isBlank()) return 0;
        try {
            return Double.parseDouble(s.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
