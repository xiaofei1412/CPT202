package cn.tablego.project.springboot.model;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Messages implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    @TableId(value = "message_id", type = IdType.AUTO)
    private Integer messageId;

    /**
     * 接收消息的用户ID
     */
    private Integer userId;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 消息类型 (如 log_review, system_notification)
     */
    private String messageType;

    /**
     * 关联的实体ID (如 reading_log_id)
     */
    private Integer relatedId;

    /**
     * 是否已读 (0未读, 1已读)
     */
    private Boolean isRead;

    /**
     * 消息创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = DatePattern.NORM_DATE_PATTERN)
    private Date createdAt;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Messages{" +
        ", messageId = " + messageId +
        ", userId = " + userId +
        ", messageContent = " + messageContent +
        ", messageType = " + messageType +
        ", relatedId = " + relatedId +
        ", isRead = " + isRead +
        ", createdAt = " + createdAt +
        "}";
    }
}
