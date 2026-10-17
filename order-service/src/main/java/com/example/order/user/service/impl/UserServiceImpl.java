package com.example.order.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.order.user.dto.UserRegisterRequest;
import com.example.order.user.dto.UserUpdateRequest;
import com.example.order.user.entity.UserAccount;
import com.example.order.user.mapper.UserAccountMapper;
import com.example.order.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserAccountMapper userAccountMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserAccountMapper userAccountMapper,
                           PasswordEncoder passwordEncoder) {
        this.userAccountMapper = userAccountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserAccount register(UserRegisterRequest request) {
        Long count = userAccountMapper.selectCount(
                new LambdaQueryWrapper<UserAccount>()
                        .eq(UserAccount::getUsername, request.username())
        );
        if (count > 0) {
            throw new IllegalArgumentException("用户名已存在");
        }

        UserAccount user = new UserAccount();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setStatus(1);
        userAccountMapper.insert(user);
        return user;
    }

    @Override
    public UserAccount getById(Long id) {
        return userAccountMapper.selectById(id);
    }

    @Override
    public List<UserAccount> listAll() {
        return userAccountMapper.selectList(null);
    }

    @Override
    public UserAccount update(Long id, UserUpdateRequest request) {
        UserAccount user = userAccountMapper.selectById(id);
        if (user == null) {
            return null;
        }
        user.setUsername(request.username());
        if (request.password() != null && !request.password().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }
        userAccountMapper.updateById(user);
        return userAccountMapper.selectById(id);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        UserAccount user = userAccountMapper.selectById(id);
        if (user == null) {
            return;
        }
        user.setStatus(status);
        userAccountMapper.updateById(user);
    }

    @Override
    public void delete(Long id) {
        userAccountMapper.deleteById(id);
    }
}
