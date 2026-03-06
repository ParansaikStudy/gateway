package com.zqksk.api.stock.controller;

import com.zqksk.api.stock.model.PaymentConfirmRequest;
import com.zqksk.api.stock.model.PaymentCreateResponse;
import com.zqksk.api.stock.service.TossPaymentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 분석 유료 결제: 주문 생성(create), 토스페이먼츠 결제 승인(confirm).
 * 결제 금액 5,000원 고정.
 */
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class PaymentController {

    /** 개발용 1원, 운영 시 5000 등으로 변경 */
    private static final int ANALYSIS_PAYMENT_AMOUNT = 1;
    private static final String ORDER_NAME = "종목 분석 1회 이용권";

    private final TossPaymentsService tossPaymentsService;

    @PostMapping("/payment/create")
    public ResponseEntity<PaymentCreateResponse> createPayment() {
        String orderId = "analysis-" + UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        return ResponseEntity.ok(new PaymentCreateResponse(orderId, ANALYSIS_PAYMENT_AMOUNT, ORDER_NAME));
    }

    @PostMapping("/payment/confirm")
    public ResponseEntity<Void> confirmPayment(@Valid @RequestBody PaymentConfirmRequest request) {
        if (request.getAmount() != ANALYSIS_PAYMENT_AMOUNT) {
            return ResponseEntity.badRequest().build();
        }
        tossPaymentsService.confirm(request.getPaymentKey(), request.getOrderId(), request.getAmount());
        return ResponseEntity.ok().build();
    }
}
