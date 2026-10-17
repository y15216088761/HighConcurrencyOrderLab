CREATE TABLE product (
                         id BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
                         name VARCHAR(128) NOT NULL COMMENT '商品名称',
                         price DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
                         stock INT NOT NULL DEFAULT 0 COMMENT '库存数量',
                         status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1上架，0下架',
                         version BIGINT NOT NULL DEFAULT 0 COMMENT '乐观锁版本号',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                             ON UPDATE CURRENT_TIMESTAMP,
                         PRIMARY KEY (id),
                         KEY idx_product_status (status)
) ENGINE=InnoDB
     DEFAULT CHARSET=utf8mb4
     COMMENT='商品表';