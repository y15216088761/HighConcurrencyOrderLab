package com.example.order.order.service;

import com.example.order.order.dto.OrderCreateRequest;
import com.example.order.order.dto.OrderDetailResponse;
import com.example.order.order.dto.OrderResponse;
import com.example.order.order.entity.Order;

import java.util.List;

public interface OrderService {

    Order create(OrderCreateRequest request);

    OrderDetailResponse getDetail(String orderNo);

    List<OrderResponse> listByUserId(Long userId);

    void cancel(String orderNo);
}
