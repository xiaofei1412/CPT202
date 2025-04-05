package com.CPT202.cpt202.utils;

import java.util.HashMap;
import java.util.Map;

public class R {
    private int code;  // 状态码
    private String msg;  // 消息
    private Map<String, Object> data = new HashMap<>();  // 返回的数据

    // 成功返回
    public static R ok() {
        R r = new R();
        r.setCode(200);  // 状态码200
        r.setMsg("成功");
        return r;
    }

    // 错误返回
    public static R error(String msg) {
        R r = new R();
        r.setCode(500);  // 状态码500
        r.setMsg(msg);
        return r;
    }

    // 设置返回的数据
    public R put(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    // Getter 和 Setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
