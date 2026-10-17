package com.example.order.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderCreateRequest(
        @NotNull(message = "用户ID不能为空")
        Long userId,

        @NotNull(message = "商品ID不能为空")
        Long productId,

        @NotNull(message = "购买数量不能为空")
        @Min(value = 1, message = "购买数量必须大于0")
        Integer quantity,

        @NotBlank(message = "请求幂等令牌不能为空")
        String requestToken
) {
}
