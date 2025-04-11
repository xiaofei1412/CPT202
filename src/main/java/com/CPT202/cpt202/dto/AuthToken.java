package com.CPT202.cpt202.dto;

import com.CPT202.cpt202.model.User;
import java.util.Date;
import java.util.List;

public class AuthToken {

    public static final String SESSION_NAME = "auth_token";  // session 名称

    private User user;  // 登录用户
    private String loginIp;  // 登录 IP 地址
    private Date loginTime;  // 登录时间
    private List<String> authList;  // 用户权限列表
    private List<String> authMenu;  // 用户权限菜单列表

    // Getter 和 Setter 方法
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public List<String> getAuthList() {
        return authList;
    }

    public void setAuthList(List<String> authList) {
        this.authList = authList;
    }

    public List<String> getAuthMenu() {
        return authMenu;
    }

    public void setAuthMenu(List<String> authMenu) {
        this.authMenu = authMenu;
    }
}
