package cn.tablego.project.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2025-04-23
 */
@Data
@TableName(autoResultMap = true)
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;
    private String avatarPath;
    private Date createdAt;
    private String email;
    private Boolean isActivated;
    private Date lastVerificationSent;
    private String nickname;
    private String password;
    private Integer verificationAttempts;
    private Integer roleId;
    private String phone;
    private String githubId;
    private Date updatedAt;
    private Date banDate;
    private String banReason;

    /**
     * 验证码
     * @return
     */
    @TableField(exist = false)
    private String verificationCode;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }

    public Date getLastVerificationSent() {
        return lastVerificationSent;
    }

    public void setLastVerificationSent(Date lastVerificationSent) {
        this.lastVerificationSent = lastVerificationSent;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getVerificationAttempts() {
        return verificationAttempts;
    }

    public void setVerificationAttempts(Integer verificationAttempts) {
        this.verificationAttempts = verificationAttempts;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String githubId) {
        this.githubId = githubId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Users{" +
        ", userId = " + userId +
        ", avatarPath = " + avatarPath +
        ", createdAt = " + createdAt +
        ", email = " + email +
        ", isActivated = " + isActivated +
        ", lastVerificationSent = " + lastVerificationSent +
        ", nickname = " + nickname +
        ", password = " + password +
        ", verificationAttempts = " + verificationAttempts +
        ", roleId = " + roleId +
        ", phone = " + phone +
        ", githubId = " + githubId +
        ", updatedAt = " + updatedAt +
        "}";
    }
}
