package com.example.order.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductUpdateRequest(
        @NotBlank(message = "商品名称不能为空")
        String name,

        @NotNull(message = "商品价格不能为空")
        @DecimalMin(value = "0.00", message = "商品价格不能小于0")
        BigDecimal price,

        @NotNull(message = "库存不能为空")
        @Min(value = 0, message = "库存不能小于0")
        Integer stock
) {
}
