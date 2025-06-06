package cn.tablego.project.springboot.common.model;

import java.util.Date;
import org.hibernate.validator.constraints.Range;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.date.DatePattern;
import io.swagger.annotations.ApiModelProperty;

/**
 * 实体公共字段基础类，用于实体JavaBean继承
 * 
 * @author bianj
 * @version 1.0.0 2025-04-23
 */
public abstract class BaseBean extends OverrideBeanMethods {
    /** 版本号 */
    private static final long serialVersionUID = 6101499067277733665L;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    protected String createdBy;

    /** 创建时间 */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(fill = FieldFill.INSERT)
    protected Date createdTime;

    /** 最后修改人 */
    @ApiModelProperty(value = "最后修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String lastUpdatedBy;

    /** 最后修改时间 */
    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date lastUpdatedTime;

    /** 删除标记，0：已删除、1：未删除 */
    @ApiModelProperty(value = "删除标记，0：已删除、1：未删除", example = "1")
    @Range(min = 0, max = 1, message = "删除标记必需是 {min} 或 {max} 的一位正整数")
    @TableLogic
    protected Integer deleteFlag;

    /**
     * 获取记录创建人
     *
     * @return 记录创建人
     */
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
     * 设置记录创建人
     *
     * @param createdBy 记录创建人
     */
    public BaseBean setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    /**
     * 获取记录创建时间
     *
     * @return 记录创建时间
     */
    public Date getCreatedTime() {
        return this.createdTime;
    }

    /**
     * 设置记录创建时间
     *
     * @param createdTime 记录创建时间
     */
    public BaseBean setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    /**
     * 获取记录最后修改人
     *
     * @return 记录最后修改人
     */
    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }

    /**
     * 设置记录最后修改人
     *
     * @param lastUpdatedBy 记录最后修改人
     */
    public BaseBean setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    /**
     * 获取记录最后修改时间
     *
     * @return 记录最后修改时间
     */
    public Date getLastUpdatedTime() {
        return this.lastUpdatedTime;
    }

    /**
     * 设置记录最后修改时间
     *
     * @param lastUpdatedTime 记录最后修改时间
     */
    public BaseBean setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
        return this;
    }

    /**
     * 获取删除标记，0：已删除、1：未删除
     *
     * @return 删除标记
     */
    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }

    /**
     * 设置删除标记，0：已删除、1：未删除
     *
     * @param deleteFlag 删除标记
     */
    public BaseBean setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
        return this;
    }
}