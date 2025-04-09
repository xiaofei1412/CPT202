package com.CPT202.cpt202.repository;

import com.CPT202.cpt202.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // 可以在这里定义自定义的查询方法，例如根据角色名称查找角色
    Role findByRoleName(String roleName);
}
