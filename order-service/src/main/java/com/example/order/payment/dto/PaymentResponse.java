package com.example.order.payment.dto;

import com.example.order.payment.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        String paymentNo,
        Long orderId,
        BigDecimal amount,
        String status,
        LocalDateTime paidAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentNo(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaidAt()
        );
    }
}
