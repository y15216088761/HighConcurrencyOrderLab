package com.example.order.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.order.order.entity.Order;
import com.example.order.order.mapper.OrderMapper;
import com.example.order.payment.entity.Payment;
import com.example.order.payment.mapper.PaymentMapper;
import com.example.order.payment.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(OrderMapper orderMapper, PaymentMapper paymentMapper) {
        this.orderMapper = orderMapper;
        this.paymentMapper = paymentMapper;
    }

    @Override
    @Transactional
    public Payment pay(String orderNo) {
        Order order = orderMapper.selectOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getOrderNo, orderNo)
        );
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }

        int affectedRows = orderMapper.markPaid(order.getId());
        if (affectedRows != 1) {
            throw new IllegalArgumentException("订单已支付或当前状态不可支付");
        }

        Payment payment = new Payment();
        payment.setOrderId(order.getId());
        payment.setPaymentNo(UUID.randomUUID().toString().replace("-", ""));
        payment.setAmount(order.getTotalAmount());
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());
        paymentMapper.insert(payment);
        return payment;
    }
}
