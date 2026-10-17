package com.example.order.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.order.order.dto.OrderCreateRequest;
import com.example.order.order.entity.Order;
import com.example.order.order.entity.OrderItem;
import com.example.order.order.mapper.OrderItemMapper;
import com.example.order.order.mapper.OrderMapper;
import com.example.order.order.service.OrderService;
import com.example.order.product.entity.Product;
import com.example.order.product.mapper.ProductMapper;
import com.example.order.user.entity.UserAccount;
import com.example.order.user.mapper.UserAccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final UserAccountMapper userAccountMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderServiceImpl(UserAccountMapper userAccountMapper,
                            ProductMapper productMapper,
                            OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper) {
        this.userAccountMapper = userAccountMapper;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    @Transactional
    public Order create(OrderCreateRequest request) {
        UserAccount user = userAccountMapper.selectById(request.userId());
        if (user == null || user.getStatus() != 1) {
            throw new IllegalArgumentException("用户不存在或已禁用");
        }

        Product product = productMapper.selectById(request.productId());
        if (product == null || product.getStatus() != 1) {
            throw new IllegalArgumentException("商品不存在或已下架");
        }

        int affectedRows = productMapper.decreaseStock(
                request.productId(), request.quantity());
        if (affectedRows != 1) {
            throw new IllegalArgumentException("库存不足");
        }

        BigDecimal amount = product.getPrice()
                .multiply(BigDecimal.valueOf(request.quantity()));

        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", ""));
        order.setUserId(request.userId());
        order.setTotalAmount(amount);
        order.setStatus("PENDING_PAYMENT");
        order.setRequestToken(request.requestToken());
        orderMapper.insert(order);

        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setPrice(product.getPrice());
        item.setQuantity(request.quantity());
        item.setAmount(amount);
        orderItemMapper.insert(item);

        return order;
    }
}
