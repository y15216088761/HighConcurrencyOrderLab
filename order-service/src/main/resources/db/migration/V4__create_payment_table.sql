CREATE TABLE payment (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    payment_no VARCHAR(64) NOT NULL COMMENT '支付流水号',
    amount DECIMAL(10, 2) NOT NULL COMMENT '支付金额',
    status VARCHAR(32) NOT NULL COMMENT '支付状态',
    paid_at DATETIME NULL COMMENT '支付时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_payment_order_id (order_id),
    UNIQUE KEY uk_payment_payment_no (payment_no),
    KEY idx_payment_status (status)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='支付记录表';
