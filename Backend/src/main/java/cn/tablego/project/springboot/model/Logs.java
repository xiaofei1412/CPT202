package cn.tablego.project.springboot.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName(value = "reading_logs", autoResultMap = true)
@Data
public class Logs implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "reading_log_id", type = IdType.AUTO)
    private Integer readingLogId;
    private String author;
    private Date createdAt;
    private String personalNotes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date readingDate;
    private Integer readingTime;
    @TableField(exist = false)
    private Integer readingTimeType;
    private String title;
    private String readStatus;
    private Date updatedAt;
    private Integer userId;
    @TableField(exist = false)
    private Integer aduit;
    private String mood;
    private String status;
    private String rejectReason;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags;

    @TableField(exist = false)
    private Integer pageNum;
    @TableField(exist = false)
    private Integer pageSize;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @TableField(exist = false)
    private Integer sortType;

    public Integer getReadingLogId() {
        return readingLogId;
    }

    public void setReadingLogId(Integer readingLogId) {
        this.readingLogId = readingLogId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getPersonalNotes() {
        return personalNotes;
    }

    public void setPersonalNotes(String personalNotes) {
        this.personalNotes = personalNotes;
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(Date readingDate) {
        this.readingDate = readingDate;
    }

    public Integer getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(Integer readingTime) {
        this.readingTime = readingTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Logs{" +
        ", readingLogId = " + readingLogId +
        ", author = " + author +
        ", createdAt = " + createdAt +
        ", personalNotes = " + personalNotes +
        ", readingDate = " + readingDate +
        ", readingTime = " + readingTime +
        ", title = " + title +
        ", updatedAt = " + updatedAt +
        ", userId = " + userId +
        ", status = " + status +
        "}";
    }
}
