package com.CPT202.cpt202.repository;

import com.CPT202.cpt202.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 通过用户名查询用户
    UserEntity findByUsername(String username);
}
