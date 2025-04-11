package com.CPT202.cpt202.model;

import jakarta.persistence.*;

/**
 * 菜单类，代表一个系统中的菜单项。
 * 该类用于描述菜单的名称、链接、顺序、显示状态、父菜单等信息。
 */
@Entity
@Table(name = "a_menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;  // 菜单ID，数据库中的主键

    @Column(nullable = false)
    private String name;  // 菜单的名称（用户界面显示的名字）

    @Column(nullable = false, unique = true)
    private String sn;  // 菜单的唯一英文标识符，用于系统内部区分菜单（如 "user", "settings"）

    @Column(name = "order_num", nullable = false)
    private int orderNum;  // 菜单的顺序，用于菜单排序，越小的值排在前面

    @Column(nullable = false)
    private int display;  // 是否显示，0表示不显示，1表示显示

    private String href;  // 菜单的链接地址，点击该菜单时跳转的 URL

    private Integer pid;  // 父菜单ID，表示该菜单项的父级菜单，若是顶级菜单则为 null

    private String pname;  // 父菜单的名称（可选，供显示用）

    private String psn;  // 父菜单的英文标识符，用于内部标识

    private String icon;  // 菜单图标，用于显示在菜单旁边

    private String type;  // 菜单类型：1表示导航菜单，2表示权限菜单

    // Getter 和 Setter 方法

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
