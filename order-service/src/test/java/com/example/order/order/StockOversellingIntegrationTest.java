package com.example.order.order;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StockOversellingIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long userId;
    private Long productId;

    @BeforeEach
    void setUp() {
        UserAccount user = new UserAccount();
        user.setUsername("stock-test-" + UUID.randomUUID());
        user.setPasswordHash(passwordEncoder.encode("test123456"));
        user.setStatus(1);
        userAccountMapper.insert(user);
        userId = user.getId();

        Product product = new Product();
        product.setName("并发测试商品");
        product.setPrice(new BigDecimal("10.00"));
        product.setStock(10);
        product.setStatus(1);
        product.setVersion(0L);
        productMapper.insert(product);
        productId = product.getId();
    }

    @AfterEach
    void tearDown() {
        List<Order> orders = orderMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order>()
                        .eq(Order::getUserId, userId)
        );
        for (Order order : orders) {
            orderItemMapper.delete(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OrderItem>()
                            .eq(OrderItem::getOrderId, order.getId())
            );
            orderMapper.deleteById(order.getId());
        }
        productMapper.deleteById(productId);
        userAccountMapper.deleteById(userId);
    }

    @Test
    void shouldNotOversellWhenRequestsArriveConcurrently() throws Exception {
        int requestCount = 50;
        int initialStock = 10;
        ExecutorService pool = Executors.newFixedThreadPool(requestCount);
        CountDownLatch ready = new CountDownLatch(requestCount);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger();

        for (int i = 0; i < requestCount; i++) {
            pool.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    orderService.create(new OrderCreateRequest(
                            userId,
                            productId,
                            1,
                            UUID.randomUUID().toString()
                    ));
                    successCount.incrementAndGet();
                } catch (Exception ignored) {
                    // 库存不足的请求属于预期失败
                }
            });
        }

        assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
        start.countDown();
        pool.shutdown();
        assertThat(pool.awaitTermination(30, TimeUnit.SECONDS)).isTrue();

        Product product = productMapper.selectById(productId);
        assertThat(successCount.get()).isEqualTo(initialStock);
        assertThat(product.getStock()).isZero();
    }
}
