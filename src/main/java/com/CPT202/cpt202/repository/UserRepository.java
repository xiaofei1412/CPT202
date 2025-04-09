package com.CPT202.cpt202.repository;

import com.CPT202.cpt202.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // 可以在这里定义自定义的查询方法，例如根据邮箱查找用户
    User findByEmail(String email);

    // 根据昵称查找用户
    User findByNickname(String nickname);
}
