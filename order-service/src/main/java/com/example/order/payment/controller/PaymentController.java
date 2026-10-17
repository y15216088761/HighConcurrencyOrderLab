package com.example.order.payment.controller;

import com.example.order.payment.dto.PaymentResponse;
import com.example.order.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{orderNo}")
    public PaymentResponse getByOrderNo(@PathVariable String orderNo) {
        return PaymentResponse.from(paymentService.getByOrderNo(orderNo));
    }

    @PostMapping("/{orderNo}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse pay(@PathVariable String orderNo) {
        return PaymentResponse.from(paymentService.pay(orderNo));
    }
}
