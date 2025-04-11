package com.CPT202.cpt202.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;

@Service
public class MenuServiceImpl implements IMenuService {

    @Override
    public List<String> listAuthByUser(Integer userId) {
        // 假设这是用来返回用户权限菜单的实现
        return Arrays.asList("Dashboard", "Settings", "Profile");  // 使用 Arrays.asList()
    }

    @Override
    public List<String> queryMenuDtoNew(Integer userId) {
        if (userId != null) {
            // 假设需要根据 userId 查询菜单数据
            return Arrays.asList("Dashboard", "Reports", "Settings");  // 示例返回的菜单列表
        } else {
            return Arrays.asList("Dashboard", "Settings");  // 返回默认菜单
        }
    }
}
