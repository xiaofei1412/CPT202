package com.CPT202.cpt202.dto;

public class UserRegistrationRequest {

    private String nickname;
    private String email;
    private String password;

    // 默认构造函数
    public UserRegistrationRequest() {}

    // 构造函数
    public UserRegistrationRequest(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    // Getter 和 Setter 方法

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
