package com.example.order.order.dto;

import com.example.order.order.entity.Order;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        String orderNo,
        Long userId,
        BigDecimal totalAmount,
        String status
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNo(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }
}
