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
@TableName("sensitive_words")
public class Words implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "word_id", type = IdType.AUTO)
    private Long wordId;
    private String word;
    private Date createdAt;

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Words{" +
        ", wordId = " + wordId +
        ", word = " + word +
        ", createdAt = " + createdAt +
        "}";
    }
}
