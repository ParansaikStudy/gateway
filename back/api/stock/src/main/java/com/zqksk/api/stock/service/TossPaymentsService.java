package com.zqksk.api.stock.service;

import com.zqksk.api.stock.config.TossPaymentsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * 토스페이먼츠 결제 승인 API 호출.
 * POST https://api.tosspayments.com/v1/payments/confirm
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TossPaymentsService {

    private static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final TossPaymentsProperties tossProperties;
    private final RestTemplate restTemplate;

    public void confirm(String paymentKey, String orderId, int amount) {
        if (!tossProperties.isConfigured()) {
            throw new IllegalStateException("토스페이먼츠 시크릿 키가 설정되지 않았습니다.");
        }
        String secretKey = tossProperties.getSecretKey();
        String auth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + auth);

        Map<String, Object> body = Map.of(
            "paymentKey", paymentKey,
            "orderId", orderId,
            "amount", amount
        );

        ResponseEntity<String> res = restTemplate.exchange(
            CONFIRM_URL,
            HttpMethod.POST,
            new HttpEntity<>(body, headers),
            String.class
        );
        if (!res.getStatusCode().is2xxSuccessful()) {
            log.warn("Toss 결제 승인 실패: orderId={}, status={}, body={}", orderId, res.getStatusCode(), res.getBody());
            throw new IllegalStateException("결제 승인에 실패했습니다: " + (res.getBody() != null ? res.getBody() : res.getStatusCode()));
        }
    }
}
