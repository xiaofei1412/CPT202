package cn.tablego.project.springboot.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2025-04-23
 */
@TableName("user_entity")
public class Entity implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String password;
    private String username;
    private Date addtime;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Entity{" +
        ", id = " + id +
        ", password = " + password +
        ", username = " + username +
        ", addtime = " + addtime +
        ", role = " + role +
        "}";
    }
}
