package com.example.order.order.controller;

import com.example.order.order.dto.OrderCreateRequest;
import com.example.order.order.dto.OrderDetailResponse;
import com.example.order.order.dto.OrderResponse;
import com.example.order.order.dto.OrderResponse;
import com.example.order.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderNo}")
    public OrderDetailResponse getDetail(@PathVariable String orderNo) {
        return orderService.getDetail(orderNo);
    }

    @GetMapping
    public List<OrderResponse> listByUserId(@RequestParam Long userId) {
        return orderService.listByUserId(userId);
    }

    @DeleteMapping("/{orderNo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable String orderNo) {
        orderService.cancel(orderNo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderCreateRequest request) {
        return OrderResponse.from(orderService.create(request));
    }
}
