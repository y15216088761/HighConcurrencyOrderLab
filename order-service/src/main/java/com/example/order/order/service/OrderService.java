package com.example.order.order.service;

import com.example.order.order.dto.OrderCreateRequest;
import com.example.order.order.entity.Order;

public interface OrderService {

    Order create(OrderCreateRequest request);
}
