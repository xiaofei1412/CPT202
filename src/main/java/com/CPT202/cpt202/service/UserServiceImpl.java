package com.CPT202.cpt202.service;

import com.CPT202.cpt202.model.User;
import com.CPT202.cpt202.repository.UserRepository;
import com.CPT202.cpt202.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;  // 用于数据库操作



    // 用户注册功能
    @Override
    public User registerUser(String email, String password, String nickname) throws NoSuchAlgorithmException {
        // 检查邮箱是否已存在
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("邮箱已注册");
        }

        // 检查昵称是否已存在
        if (userRepository.findByNickname(nickname) != null) {
            throw new RuntimeException("昵称已存在");
        }

        // 加密密码
        String encryptedPassword = SecurityUtil.md5(nickname, password);

        // 创建新用户
        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPasswordHash(encryptedPassword);  // 保存加密后的密码
        user.setActivated(false);  // 初始状态为未激活

        // 保存用户到数据库
        return userRepository.save(user);  // 返回保存的用户对象
    }

    // 用户登录功能
    @Override
    public User loginUser(String nickname, String password) throws NoSuchAlgorithmException {
        // 根据昵称查找用户
        User user = userRepository.findByNickname(nickname);
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }

        // 验证密码
        String encryptedPassword = SecurityUtil.md5(nickname, password);
        if (!user.getPasswordHash().equals(encryptedPassword)) {
            throw new RuntimeException("密码不正确");
        }

        return user;  // 返回登录成功的用户
    }
}
