package com.ndc.deliverymanagement.model;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private Double amount;
}
