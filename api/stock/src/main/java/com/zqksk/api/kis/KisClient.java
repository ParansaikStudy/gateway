package com.zqksk.api.kis;

import com.zqksk.api.config.KisProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KisClient {

    private static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final KisProperties kisProperties;

    public String getAccessToken() {
        String url = kisProperties.getBaseUrl() + "/oauth2/tokenP";
        var body = Map.of(
            "grant_type", "client_credentials",
            "appkey", kisProperties.appKey(),
            "appsecret", kisProperties.appSecret()
        );
        var response = RestClient.create().post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .retrieve()
            .body(KisTokenResponse.class);
        if (response == null || response.accessToken() == null) {
            throw new IllegalStateException("KIS token request failed");
        }
        return response.accessToken();
    }

    public KisPriceOutput getPrice(String accessToken, String stockCode) {
        String url = kisProperties.getBaseUrl()
            + "/uapi/domestic-stock/v1/quotations/inquire-price?"
            + "FID_COND_MRKT_DIV_CODE=J&FID_INPUT_ISCD=" + stockCode;
        @SuppressWarnings("unchecked")
        var raw = RestClient.create().get()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/json; charset=utf-8")
            .header("appkey", kisProperties.appKey())
            .header("appsecret", kisProperties.appSecret())
            .header("tr_id", "FHKST01010100")
            .retrieve()
            .body(Map.class);
        if (raw == null) throw new IllegalStateException("KIS price response empty");
        String rtCd = (String) raw.get("rt_cd");
        if (!"0".equals(rtCd)) {
            throw new IllegalStateException("KIS price error: " + raw.get("msg1"));
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> output = (Map<String, Object>) raw.get("output");
        if (output == null) throw new IllegalStateException("KIS price output missing");
        return new KisPriceOutput(
            (String) output.get("stck_prpr"),
            (String) output.get("prdy_vrss"),
            (String) output.get("prdy_ctrt")
        );
    }

    public List<KisDailyItem> getDailyChart(String accessToken, String stockCode, String startDate, String endDate) {
        String url = kisProperties.getBaseUrl()
            + "/uapi/domestic-stock/v1/quotations/inquire-daily-itemchartprice?"
            + "FID_COND_MRKT_DIV_CODE=J"
            + "&FID_INPUT_ISCD=" + stockCode
            + "&FID_INPUT_DATE_1=" + startDate
            + "&FID_INPUT_DATE_2=" + endDate
            + "&FID_PERIOD_DIV_CODE=D"
            + "&FID_ORG_ADJ_PRC=0";
        @SuppressWarnings("unchecked")
        var raw = RestClient.create().get()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .header("Content-Type", "application/json; charset=utf-8")
            .header("appkey", kisProperties.appKey())
            .header("appsecret", kisProperties.appSecret())
            .header("tr_id", "FHKST03010100")
            .retrieve()
            .body(Map.class);
        if (raw == null) throw new IllegalStateException("KIS chart response empty");
        String rtCd = (String) raw.get("rt_cd");
        if (!"0".equals(rtCd)) {
            throw new IllegalStateException("KIS chart error: " + raw.get("msg1"));
        }
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> output2 = (List<Map<String, Object>>) raw.get("output2");
        if (output2 == null || output2.isEmpty()) {
            return List.of();
        }
        return output2.stream()
            .map(m -> new KisDailyItem(
                (String) m.get("stck_bsop_date"),
                (String) m.get("stck_clpr"),
                (String) m.get("stck_oprc"),
                (String) m.get("stck_hgpr"),
                (String) m.get("stck_lwpr")
            ))
            .toList();
    }

    public static String toStartDate(int daysAgo) {
        return LocalDate.now().minusDays(daysAgo).format(YYYYMMDD);
    }

    public static String toEndDate() {
        return LocalDate.now().format(YYYYMMDD);
    }
}
