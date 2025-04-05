package com.CPT202.cpt202.service;

import com.CPT202.cpt202.entity.UserEntity;
import com.CPT202.cpt202.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;  // 自动注入仓库

    // 根据用户名查找用户
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
