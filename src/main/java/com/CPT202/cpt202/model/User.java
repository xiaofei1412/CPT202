package com.CPT202.cpt202.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash; // 存储哈希值而不是明文密码是为了提高安全性

    private String avatarPath; // 用户头像图片的存储路径或 URL

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isActivated; // 标记用户的账户是否已激活（通常通过邮箱验证）

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer verificationAttempts; // 记录用户尝试验证账户的次数（例如，发送验证邮件的次数）

    private LocalDateTime lastVerificationSent; // 记录上次发送验证邮件的时间

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role; // 关联到 Role 表

    public User() {
        this.createdAt = LocalDateTime.now();
        this.isActivated = false;
        this.verificationAttempts = 0;
    }

    // Getter 和 Setter 方法

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getVerificationAttempts() {
        return verificationAttempts;
    }

    public void setVerificationAttempts(Integer verificationAttempts) {
        this.verificationAttempts = verificationAttempts;
    }

    public LocalDateTime getLastVerificationSent() {
        return lastVerificationSent;
    }

    public void setLastVerificationSent(LocalDateTime lastVerificationSent) {
        this.lastVerificationSent = lastVerificationSent;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
