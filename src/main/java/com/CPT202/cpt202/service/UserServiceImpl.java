package com.CPT202.cpt202.service;

import com.CPT202.cpt202.model.User;
import com.CPT202.cpt202.repository.UserRepository;
import com.CPT202.cpt202.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.CPT202.cpt202.repository.RoleRepository;
import com.CPT202.cpt202.model.Role;



import java.security.NoSuchAlgorithmException;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;  // 用于数据库操作
    @Autowired
    private RoleRepository roleRepository;



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

        // 查找角色
        Role defaultRole = roleRepository.findByRoleName("USER"); // 查找"USER"角色
        if (defaultRole == null) {
            defaultRole = new Role();
            defaultRole.setRoleName("USER");
            roleRepository.save(defaultRole);  // 如果没有找到，创建并保存"USER"角色
        }


        // 加密密码
        String encryptedPassword = SecurityUtil.md5(nickname, password);



        // 创建新用户
        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPasswordHash(encryptedPassword);  // 保存加密后的密码
        user.setActivated(false);  // 初始状态为未激活
        user.setRole(defaultRole);  // 设置角色

        return user;  // 返回未保存的用户对象
    }


    // 实现激活用户功能
    @Override
    public void activateUser(String email) {
        User user = userRepository.findByEmail(email);  // 查找用户
        if (user != null) {
            user.setActivated(true);  // 激活账户
            userRepository.save(user);  // 保存更新的用户信息
        } else {
            throw new RuntimeException("用户不存在");
        }
    }

    // 用户登录功能
    @Override
    public User loginUser(String usernameOrEmail, String password) throws NoSuchAlgorithmException {
        // 先检查是否是邮箱格式
        User user;
        if (usernameOrEmail.contains("@")) {  // 如果输入的是邮箱
            user = userRepository.findByEmail(usernameOrEmail);  // 根据邮箱查找
        } else {  // 如果输入的是昵称
            user = userRepository.findByNickname(usernameOrEmail);  // 根据昵称查找
        }

        if (user == null) {
            throw new RuntimeException("用户名或邮箱未注册");
        }

        // 检查用户是否已激活
        if (!user.getActivated()) {
            throw new RuntimeException("账户未激活，请检查您的邮箱激活账户");
        }

        // 验证密码
        String encryptedPassword = SecurityUtil.md5(usernameOrEmail, password);  // 使用邮箱或昵称加密密码
        if (!user.getPasswordHash().equals(encryptedPassword)) {
            throw new RuntimeException("密码不正确");
        }

        return user;  // 返回登录成功的用户
    }


}
