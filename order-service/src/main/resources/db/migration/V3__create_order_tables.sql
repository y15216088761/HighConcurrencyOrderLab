CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(64) NOT NULL COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10, 2) NOT NULL COMMENT '订单总金额',
    status VARCHAR(32) NOT NULL COMMENT '订单状态',
    request_token VARCHAR(64) NOT NULL COMMENT '客户端请求幂等令牌',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_orders_order_no (order_no),
    UNIQUE KEY uk_orders_user_token (user_id, request_token),
    KEY idx_orders_user_id (user_id),
    KEY idx_orders_status (status)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='订单主表';

CREATE TABLE order_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(128) NOT NULL COMMENT '商品名称快照',
    price DECIMAL(10, 2) NOT NULL COMMENT '商品价格快照',
    quantity INT NOT NULL COMMENT '购买数量',
    amount DECIMAL(10, 2) NOT NULL COMMENT '明细金额',
    PRIMARY KEY (id),
    KEY idx_order_item_order_id (order_id),
    KEY idx_order_item_product_id (product_id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='订单明细表';
