package cn.tablego.project.springboot.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 
 * @since 2025-04-23
 */
@TableName("a_menu_seq")
public class MenuSeq implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long nextVal;

    public Long getNextVal() {
        return nextVal;
    }

    public void setNextVal(Long nextVal) {
        this.nextVal = nextVal;
    }

    @Override
    public String toString() {
        return "MenuSeq{" +
        ", nextVal = " + nextVal +
        "}";
    }
}
