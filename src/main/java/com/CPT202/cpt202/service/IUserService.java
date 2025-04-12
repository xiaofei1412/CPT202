package com.CPT202.cpt202.service;

import com.CPT202.cpt202.model.User;

import java.security.NoSuchAlgorithmException;

public interface IUserService {

    User registerUser(String email, String password, String nickname) throws NoSuchAlgorithmException;  // 注册用户
    User loginUser(String nickname, String password) throws NoSuchAlgorithmException;  // 用户登录

    void activateUser(String email);  // 激活用户接口
}
