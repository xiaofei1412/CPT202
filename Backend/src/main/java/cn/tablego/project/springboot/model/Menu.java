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
@TableName("a_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer display;
    private String href;
    private String icon;
    private String name;
    private Integer orderNum;
    private Integer pid;
    private String pname;
    private String psn;
    private String sn;
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPsn() {
        return psn;
    }

    public void setPsn(String psn) {
        this.psn = psn;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Menu{" +
        ", id = " + id +
        ", display = " + display +
        ", href = " + href +
        ", icon = " + icon +
        ", name = " + name +
        ", orderNum = " + orderNum +
        ", pid = " + pid +
        ", pname = " + pname +
        ", psn = " + psn +
        ", sn = " + sn +
        ", type = " + type +
        "}";
    }
}
