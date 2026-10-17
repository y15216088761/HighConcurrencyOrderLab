package com.example.order.user.service;

import com.example.order.user.dto.UserRegisterRequest;
import com.example.order.user.dto.UserUpdateRequest;
import com.example.order.user.entity.UserAccount;

import java.util.List;

public interface UserService {

    UserAccount register(UserRegisterRequest request);

    UserAccount getById(Long id);

    List<UserAccount> listAll();

    UserAccount update(Long id, UserUpdateRequest request);

    void updateStatus(Long id, Integer status);

    void delete(Long id);
}
