package com.example.order.order.dto;

import com.example.order.order.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetailResponse(
        Long id,
        String orderNo,
        Long userId,
        BigDecimal totalAmount,
        String status,
        List<OrderItemResponse> items
) {
    public static OrderDetailResponse from(Order order, List<OrderItemResponse> items) {
        return new OrderDetailResponse(
                order.getId(),
                order.getOrderNo(),
                order.getUserId(),
                order.getTotalAmount(),
                order.getStatus(),
                items
        );
    }
}
