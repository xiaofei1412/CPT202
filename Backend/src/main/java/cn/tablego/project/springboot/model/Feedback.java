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
@TableName(autoResultMap = true)
@Data
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 反馈ID
     */
    @TableId(value = "feedback_id", type = IdType.AUTO)
    private Integer feedbackId;

    /**
     * 提交反馈的用户ID (如果允许匿名则可空)
     */
    private Integer userId;

    /**
     * 反馈类型 (如 bug, feature_request, general)
     */
    private String feedbackType;

    /**
     * 反馈标题
     */
    private String title;

    /**
     * 反馈详细描述
     */
    private String description;

    /**
     * 用户联系方式 (可选)
     */
    private String contactInfo;

    /**
     * 反馈提交时间
     */
    private Date createdAt;
    private Date replyDate;

    /**
     * 反馈处理状态 (如 Open, In Progress, Closed)
     */
    private String status;

    /**
     * 管理员回复
     */
    private String adminResponse;

    private String username;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> attachments;

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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

    @Override
    public String toString() {
        return "Feedback{" +
        ", feedbackId = " + feedbackId +
        ", userId = " + userId +
        ", feedbackType = " + feedbackType +
        ", title = " + title +
        ", description = " + description +
        ", contactInfo = " + contactInfo +
        ", createdAt = " + createdAt +
        ", status = " + status +
        ", adminResponse = " + adminResponse +
        "}";
    }
}
