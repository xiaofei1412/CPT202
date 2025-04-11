package com.CPT202.cpt202.controller;

import com.CPT202.cpt202.dto.AuthToken;
import com.CPT202.cpt202.dto.LoginRequest;
import com.CPT202.cpt202.model.User;
import com.CPT202.cpt202.service.IUserService;
import com.CPT202.cpt202.service.IMenuService;
import com.CPT202.cpt202.service.MenuServiceImpl;
import com.CPT202.cpt202.utils.SecurityUtil;
import com.CPT202.cpt202.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;


import jakarta.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private MenuServiceImpl menuServiceImpl;

    /** 登录 */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody  // 暂时返回 JSON 响应
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String nickname = request.getNickname();  // 获取请求体中的用户名
        String password = request.getPassword();  // 获取请求体中的密码
        String errMsg = null;

        try {
            // 查找用户
            User u = userService.findByNickname(nickname);
            if (u == null || u.getActivated() == null || !u.getActivated()) {
                errMsg = "用户名不存在或已停用";
                return ResponseEntity.status(401).body(errMsg);  // 返回 401 错误
            } else if (!u.getPasswordHash().equals(SecurityUtil.md5(nickname, password))) {
                errMsg = "密码输入不正确";
                return ResponseEntity.status(401).body(errMsg);  // 返回 401 错误
            } else {
                // 登录成功，创建 AuthToken 保存用户登录信息
                AuthToken at = new AuthToken();
                at.setLoginIp(httpRequest.getRemoteAddr());
                at.setLoginTime(new Date());
                at.setUser(u);
                at.setAuthMenu(menuServiceImpl.queryMenuDtoNew(u.getUserId()));  // 获取用户权限菜单
                List<String> authList = menuService.listAuthByUser(u.getUserId());
                authList.add("AdminController.index");
                authList.add("AdminController.updatePwd");
                at.setAuthList(authList);  // 设置用户的权限列表

                // 返回登录成功后的 AuthToken（JSON 格式）
                return ResponseEntity.ok(at);  // 返回成功的 JSON 响应
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SystemException("密码加密失败！", e);  // 异常处理
        } catch (Exception e) {
            throw new SystemException("登录异常！", e);  // 捕获其他所有异常，给出具体错误提示
        }
    }

    /** 设置当前的菜单 ID */
    @RequestMapping(value = "setCurrentMenuId", method = RequestMethod.POST)
    @ResponseBody
    public String setCurrentMenuId(Integer pmId, Integer mid, HttpServletRequest request) {
        request.getSession().setAttribute("pmId", pmId);
        request.getSession().setAttribute("mid", mid);
        return "1";  // 返回成功标识
    }

    /** 退出 */
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute(AuthToken.SESSION_NAME);  // 移除登录信息
        return "1";  // 返回成功标识
    }
}
