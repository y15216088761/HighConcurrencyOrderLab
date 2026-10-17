package com.example.order.user.dto;

import com.example.order.user.entity.UserAccount;

public record UserResponse(
        Long id,
        String username,
        Integer status
) {
    public static UserResponse from(UserAccount user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getStatus());
    }
}
