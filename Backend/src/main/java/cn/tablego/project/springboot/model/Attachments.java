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
@TableName("feedback_attachments")
public class Attachments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 附件ID
     */
    @TableId(value = "attachment_id", type = IdType.AUTO)
    private Integer attachmentId;

    /**
     * 关联的反馈ID
     */
    private Integer feedbackId;

    /**
     * 原始文件名
     */
    private String fileName;

    /**
     * 文件存储路径
     */
    private String storagePath;

    /**
     * 文件MIME类型
     */
    private String mimeType;

    /**
     * 上传时间
     */
    private Date uploadedAt;

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String toString() {
        return "Attachments{" +
        ", attachmentId = " + attachmentId +
        ", feedbackId = " + feedbackId +
        ", fileName = " + fileName +
        ", storagePath = " + storagePath +
        ", mimeType = " + mimeType +
        ", uploadedAt = " + uploadedAt +
        "}";
    }
}
