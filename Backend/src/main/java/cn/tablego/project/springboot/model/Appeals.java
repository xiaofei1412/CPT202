package cn.tablego.project.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户申诉表
 * </p>
 *
 * @author
 * @since 2025-04-23
 */
@Data
public class Appeals implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 申诉ID
     */
    @TableId(value = "appeal_id", type = IdType.AUTO)
    private Integer appealId;

    /**
     * 提交申诉的用户ID
     */
    private Integer userId;

    private String email;

    /**
     * 被限制的原因 (申诉时记录)
     */
    private String restrictionReason;

    @TableField(exist = false)
    private String username;

    /**
     * 申诉详细信息
     */
    private String appealMessage;

    /**
     * 申诉提交时间
     */
    private Date submittedAt;

    /**
     * 申诉处理状态 (如 Open, Under Review, Resolved)
     */
    private String status;

    /**
     * 管理员回复
     */
    private String adminResponse;

    /**
     * 申诉解决时间
     */
    private Date resolvedAt;

    @TableField(exist = false)
    private Date banDate;

    @TableField(exist = false)
    private String banReason;

    @TableField(exist = false)
    private String avator;


    public Integer getAppealId() {
        return appealId;
    }

    public void setAppealId(Integer appealId) {
        this.appealId = appealId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRestrictionReason() {
        return restrictionReason;
    }

    public void setRestrictionReason(String restrictionReason) {
        this.restrictionReason = restrictionReason;
    }

    public String getAppealMessage() {
        return appealMessage;
    }

    public void setAppealMessage(String appealMessage) {
        this.appealMessage = appealMessage;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    @Override
    public String toString() {
        return "Appeals{" +
        ", appealId = " + appealId +
        ", userId = " + userId +
        ", restrictionReason = " + restrictionReason +
        ", appealMessage = " + appealMessage +
        ", submittedAt = " + submittedAt +
        ", status = " + status +
        ", adminResponse = " + adminResponse +
        ", resolvedAt = " + resolvedAt +
        "}";
    }
}
