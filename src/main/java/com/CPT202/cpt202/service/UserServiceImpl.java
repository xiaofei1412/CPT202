package com.CPT202.cpt202.service;

import com.CPT202.cpt202.model.User;
import com.CPT202.cpt202.repository.UserRepository;
import com.CPT202.cpt202.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;  // 处理加密异常

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;  // 使用构造函数注入

    private final SecurityUtil securityUtil;  // 使用构造函数注入

    // 构造函数注入
    @Autowired
    public UserServiceImpl(UserRepository userRepository, SecurityUtil securityUtil) {
        this.userRepository = userRepository;
        this.securityUtil = securityUtil;  // 构造函数注入 SecurityUtil
    }

    // 根据昵称查找用户
    @Override
    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname);  // 使用 findByNickname 查找用户
    }

    // 用户注册
    @Override
    public User registerUser(String email, String password, String nickname) throws NoSuchAlgorithmException {  // 处理异常
        // 检查邮箱是否已注册
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("该邮箱已注册！");
        }

        // 使用 SecurityUtil 工具类对密码进行 MD5 加密
        String encryptedPassword = SecurityUtil.md5(nickname, password);  // 使用 md5 方法加密密码

        // 创建新用户并设置加密后的密码
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(encryptedPassword);  // 设置加密后的密码
        user.setNickname(nickname);
        user.setActivated(false);  // 初始状态为未激活

        // 保存新用户到数据库
        return userRepository.save(user);  // 返回保存的用户
    }
}
