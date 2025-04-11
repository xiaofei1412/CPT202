package com.CPT202.cpt202.service;

import com.CPT202.cpt202.model.User;

import java.security.NoSuchAlgorithmException;

public interface IUserService {
    User findByNickname(String nickname);  // 查找用户
    User registerUser(String email, String password, String nickname) throws NoSuchAlgorithmException;  // 注册用户
}
