package cn.tablego.project.springboot.common.model;

import java.util.List;
import java.io.Serializable;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页响应参数
 *
 * @author bianj
 * @version 1.0.0 2025-04-23
 */
@ApiModel(description = "分页响应参数")
public class Paging<T extends Serializable> extends OverrideBeanMethods {
    /** 版本号 */
    private static final long serialVersionUID = 6242056024283004874L;

    /** 每页显示多少条记录 */
    @ApiModelProperty(value = "每页显示多少条记录", example = "20")
    private Long pageSize = 20L;

    /** 当前页 */
    @ApiModelProperty(value = "当前页", example = "1")
    private Long page = 1L;

    /** 总记录数 */
    @ApiModelProperty(value = "总记录数")
    private Long total = 0L;

    /** 总页数 */
    @ApiModelProperty(value = "总页数")
    private Long totalPage = 0L;

    /** 是否存在上一页 */
    @ApiModelProperty(value = "是否存在上一页")
    private Boolean hasPrevious;

    /** 是否存在下一页 */
    @ApiModelProperty(value = "是否存在下一页")
    private Boolean hasNext;

    /** 当前页的记录集 */
    @ApiModelProperty(value = "当前页的记录集")
    private List<T> records;

    /** Paging空构造函数 */
    public Paging() {
    }

    /**
     * Paging对象造函数
     *
     * @param pageSize  每页显示多少条记录
     * @param page      当前页
     * @param total     总记录数
     * @param totalPage 总页数
     * @param records   当前页的记录集
     */
    public Paging(Long pageSize, Long page, Long total, Long totalPage, List<T> records) {
        this.setPageSize(pageSize).setPage(page).setTotal(total).setTotalPage(totalPage).setRecords(records);
        this.setHasPrevious(page > 1);
        this.setHasNext(page < totalPage);
    }

    /** 创建分页对象 */
    public static <T extends Serializable> Paging<T> buildPaging() {
        return new Paging<>();
    }

    /**
     * 创建分页对象并赋值
     *
     * @param page 分页模型
     * @return 分页数据
     */
    public static <T extends Serializable> Paging<T> buildPaging(IPage<T> page) {
        return new Paging<>(page.getSize(), page.getCurrent(), page.getTotal(), page.getPages(), page.getRecords());
    }

    /**
     * 创建分页对象并赋值
     *
     * @param pageSize  每页显示多少条记录
     * @param page      当前页
     * @param total     总记录数
     * @param totalPage 总页数
     * @param records   当前页的记录集
     * @return 分页数据
     */
    public static <T extends Serializable> Paging<T> buildPaging(Long pageSize, Long page, Long total, Long totalPage, List<T> records) {
        return new Paging<>(pageSize, page, total, totalPage, records);
    }

    /**
     * 计算总页数
     *
     * @param pageSize 每页显示多少条记录
     * @param total    总记录数
     * @return 总页数
     */
    public static Long computeTotalPage(Long pageSize, Long total) {
        if (pageSize != null && pageSize > 0 && total != null && total >= 0) {
            return (total + pageSize - 1) / pageSize;
        }
        return 0L;
    }

    /**
     * 获取每页显示多少条记录
     *
     * @return 每页显示多少条记录
     */
    public Long getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页显示多少条记录
     *
     * @param pageSize 每页显示多少条记录
     * @return 分页数据
     */
    public Paging<T> setPageSize(Long pageSize) {
        if (pageSize != null && pageSize > 0) {
            this.pageSize = pageSize;
        }
        return this;
    }

    /**
     * 获取当前页
     *
     * @return 当前页
     */
    public Long getPage() {
        return page;
    }

    /**
     * 设置当前页
     *
     * @param page 当前页
     * @return 分页数据
     */
    public Paging<T> setPage(Long page) {
        if (page != null && page > 0) {
            this.page = page;
        }
        return this;
    }

    /**
     * 获取总记录数
     *
     * @return 总记录数
     */
    public Long getTotal() {
        return total;
    }

    /**
     * 设置总记录数
     *
     * @param total 总记录数
     * @return 分页数据
     */
    public Paging<T> setTotal(Long total) {
        if (total != null && total >= 0) {
            this.total = total;
        }
        return this;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    public Long getTotalPage() {
        return totalPage;
    }

    /**
     * 设置总页数
     *
     * @param totalPage 总页数
     * @return 分页数据
     */
    public Paging<T> setTotalPage(Long totalPage) {
        if (totalPage != null && totalPage >= 0) {
            this.totalPage = totalPage;
        }
        return this;
    }

    /**
     * 获取当前页的记录集
     *
     * @return 当前页的记录集
     */
    public List<T> getRecords() {
        return records;
    }

    /**
     * 设置当前页的记录集
     *
     * @param records 当前页的记录集
     * @return 分页数据
     */
    public Paging<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    /**
     * 获取是否存在上一页
     *
     * @return 是否存在上一页
     */
    public Boolean getHasPrevious() {
        return hasPrevious;
    }

    /**
     * 设置是否存在上一页
     *
     * @param hasPrevious 是否存在上一页
     * @return 分页数据
     */
    public Paging<T> setHasPrevious(Boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
        return this;
    }

    /**
     * 获取是否存在下一页
     *
     * @return 是否存在下一页
     */
    public Boolean getHasNext() {
        return hasNext;
    }

    /**
     * 设置是否存在下一页
     *
     * @param hasNext 是否存在下一页
     * @return 分页数据
     */
    public Paging<T> setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
        return this;
    }
}