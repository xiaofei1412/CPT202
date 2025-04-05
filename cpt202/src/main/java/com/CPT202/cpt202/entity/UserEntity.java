package com.CPT202.cpt202.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserEntity {

    @Id
    private Long id;          // 主键ID
    private String username;  // 用户名
    private String password;  // 密码

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
