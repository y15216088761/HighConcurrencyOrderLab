package com.example.order.user.service;

import com.example.order.user.dto.UserRegisterRequest;
import com.example.order.user.entity.UserAccount;

public interface UserService {

    UserAccount register(UserRegisterRequest request);
}
