package com.CPT202.cpt202.service;

import java.util.List;

public interface IMenuService {

    // 获取用户权限菜单列表
    List<String> listAuthByUser(Integer userId);

    // 根据 userId 查询菜单数据
    List<String> queryMenuDtoNew(Integer userId);
}
