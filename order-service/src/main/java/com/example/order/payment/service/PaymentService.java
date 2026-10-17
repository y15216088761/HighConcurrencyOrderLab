package com.example.order.payment.service;

import com.example.order.payment.entity.Payment;

public interface PaymentService {

    Payment pay(String orderNo);

    Payment getByOrderNo(String orderNo);
}
