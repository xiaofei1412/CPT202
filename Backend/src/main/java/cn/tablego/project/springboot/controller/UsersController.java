package cn.tablego.project.springboot.controller;

import cn.tablego.project.springboot.common.model.AjaxResult;
import cn.tablego.project.springboot.service.impl.CaptchaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import cn.tablego.project.springboot.service.UsersService;
import cn.tablego.project.springboot.model.Users;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2025-04-23
 */
@RestController
@RequestMapping("/users")
public class UsersController {


    @Autowired
    private UsersService usersService;

    @Autowired
    private CaptchaService captchaService;

    @GetMapping(value = "/list")
    public AjaxResult list(@RequestParam(required = false) Integer pageNum,
                           @RequestParam(required = false) Integer pageSize,
                           String searchQuery, String statusFilter) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(searchQuery)) {
            queryWrapper.like("nickname", searchQuery)
                    .or()
                    .like("email", searchQuery);
        }
        if (StringUtils.isNotBlank(statusFilter)) {
            if (statusFilter.equals("banned")) {
                queryWrapper.eq("is_activated", false);
            }
            if (statusFilter.equals("active")) {
                queryWrapper.eq("is_activated", true);
            }
        }
        Page<Users> aPage = usersService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return AjaxResult.success(aPage);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getById(@PathVariable("id") Long id) {
        return AjaxResult.success(usersService.getById(id));
    }

    @PostMapping(value = "/create")
    public AjaxResult create(@RequestBody Users params) {
        usersService.save(params);
        return AjaxResult.success("created successfully");
    }

    @GetMapping(value = "/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        usersService.removeById(id);
        return AjaxResult.success("deleted successfully");
    }

    @PostMapping(value = "/update")
    public AjaxResult update(@RequestBody Users params) {
        String email = params.getEmail();
        // 修改个人信息时，邮箱不能重复
        if (StringUtils.isNotBlank(email)) {
            QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email);
            queryWrapper.ne("user_id", params.getUserId());
            if (usersService.count(queryWrapper) > 0) {
                return AjaxResult.error("邮箱已被使用");
            }
            String code = params.getVerificationCode();

            String key = email + ":" + "reg";
            boolean b = captchaService.validateCaptcha(key, code);
            if (!b) {
                return AjaxResult.error("验证码错误");
            }
        }

        if (StringUtils.isNotBlank(params.getNickname())) {
            QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
            // 修改个人信息，nickName 不能重复
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("nickname", params.getNickname());
            queryWrapper.ne("user_id", params.getUserId());
            if (usersService.count(queryWrapper) > 0) {
                return AjaxResult.error("昵称已被使用");
            }
        }

        usersService.updateById(params);
        return AjaxResult.success("updated successfully");
    }
    @GetMapping(value = "/updatepwd")
    public AjaxResult updatepwd(String email, String code, String password) {

        // 修改个人信息时，邮箱不能重复
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Users one = usersService.getOne(queryWrapper);

        String key = email + ":" + "reg";
        boolean b = captchaService.validateCaptcha(key, code);
        if (!b) {
            return AjaxResult.error("验证码错误");
        }


        if (one == null) {
            return AjaxResult.error("email is not register");
        }
        if (one.getPassword().equals(password)) {
            return AjaxResult.error("新密码不能与旧密码相同");
        }

        one.setPassword(password);



        usersService.updateById(one);
        return AjaxResult.success("updated successfully");
    }

    /**
     * 根据账号密码删除
     */
    @GetMapping(value = "/deleteByAccountAndPassword")
    public AjaxResult deleteByAccountAndPassword(String account, String password) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", account);
        queryWrapper.eq("password", password);
        Users one = usersService.getOne(queryWrapper);
        if (one == null) {
            return AjaxResult.error("password error");
        }
        usersService.removeById(one.getUserId());
        return AjaxResult.success("deleted successfully");
    }
}
