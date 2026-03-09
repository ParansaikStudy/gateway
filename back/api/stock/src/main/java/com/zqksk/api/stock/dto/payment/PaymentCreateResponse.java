package com.zqksk.api.stock.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateResponse {

    private String orderId;
    private int amount;
    private String orderName;
}
