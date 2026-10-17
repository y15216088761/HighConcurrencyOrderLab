package com.example.order.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotBlank(message = "用户名不能为空")
        @Size(min = 3, max = 64, message = "用户名长度必须在3到64个字符之间")
        String username,

        @Size(min = 8, max = 128, message = "密码长度必须在8到128个字符之间")
        String password
) {
}
