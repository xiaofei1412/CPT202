package cn.tablego.project.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("content_settings")
public class Settings implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Boolean enableLogApproval;
    private Boolean enableSensitiveWordDetection;
    private Date createdAt;
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnableLogApproval() {
        return enableLogApproval;
    }

    public void setEnableLogApproval(Boolean enableLogApproval) {
        this.enableLogApproval = enableLogApproval;
    }

    public Boolean getEnableSensitiveWordDetection() {
        return enableSensitiveWordDetection;
    }

    public void setEnableSensitiveWordDetection(Boolean enableSensitiveWordDetection) {
        this.enableSensitiveWordDetection = enableSensitiveWordDetection;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Settings{" +
        ", id = " + id +
        ", enableLogApproval = " + enableLogApproval +
        ", enableSensitiveWordDetection = " + enableSensitiveWordDetection +
        ", createdAt = " + createdAt +
        ", updatedAt = " + updatedAt +
        "}";
    }
}
