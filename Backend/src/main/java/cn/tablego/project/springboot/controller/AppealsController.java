package cn.tablego.project.springboot.controller;

import cn.tablego.project.springboot.common.model.AjaxResult;
import cn.tablego.project.springboot.model.Messages;
import cn.tablego.project.springboot.model.Users;
import cn.tablego.project.springboot.service.MessagesService;
import cn.tablego.project.springboot.service.UsersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import cn.tablego.project.springboot.service.AppealsService;
import cn.tablego.project.springboot.model.Appeals;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 用户申诉表 前端控制器
 * </p>
 *
 * @author
 * @since 2025-04-23
 */
@RestController
@RequestMapping("/appeals")
public class AppealsController {


    @Autowired
    private AppealsService appealsService;

    @Autowired
    private UsersService userService;

    @Autowired
    private MessagesService messagesService;

    @GetMapping(value = "/list")
    public AjaxResult list(@RequestParam(required = false) Integer pageNum,
                           @RequestParam(required = false) Integer pageSize,
                           String status, String startDate, String endDate) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        QueryWrapper<Appeals> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(status)) {
            if (!status.equals("all")) {
                queryWrapper.eq("status", status);
            }
        }

        if (StringUtils.isNotBlank(startDate) && !startDate.equals("null")) {
            queryWrapper.ge("submitted_at", startDate + " 00:00:00");
        }
        if (StringUtils.isNotBlank(endDate) && !startDate.equals("null")) {
            queryWrapper.le("submitted_at", endDate + " 23:59:59");
        }
        queryWrapper.orderByDesc("submitted_at");
        Page<Appeals> aPage = appealsService.page(new Page<>(pageNum, pageSize), queryWrapper);
        List<Appeals> records = aPage.getRecords();
        for (Appeals record : records) {
            Integer userId = record.getUserId();
            Users users = userService.getById(userId);
            record.setUsername(users.getNickname());
            record.setAvator(users.getAvatarPath());
            record.setBanDate(users.getBanDate());
            record.setBanReason(users.getBanReason());
        }
        return AjaxResult.success(aPage);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getById(@PathVariable("id") Long id) {
        return AjaxResult.success(appealsService.getById(id));
    }

    @PostMapping(value = "/create")
    public AjaxResult create(@RequestBody Appeals params) {
        params.setStatus("pending");
        params.setSubmittedAt(new java.util.Date());
        String email = params.getEmail();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        Users user = userService.getOne(queryWrapper);
        if (user == null) {
            return AjaxResult.error("email is not register");
        }
        params.setUserId(user.getUserId());


        appealsService.save(params);
        return AjaxResult.success("created successfully");
    }

    @GetMapping(value = "/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        appealsService.removeById(id);
        return AjaxResult.success("deleted successfully");
    }

    @PostMapping(value = "/update")
    public AjaxResult update(@RequestBody Appeals params) {
        params.setResolvedAt(new java.util.Date());
        appealsService.updateById(params);
        Appeals appeal = appealsService.getById(params.getAppealId());

        Messages messages = new Messages();
        messages.setUserId(appeal.getUserId());
        if (appeal.getStatus().equals("approved")) {
            messages.setMessageContent("您的申诉已通过审核");
            // 解封账号
            Users users = userService.getById(appeal.getUserId());
            users.setBanDate(null);
            users.setBanReason(null);
            users.setIsActivated(true);
            userService.updateById(users);
        } else {
            messages.setMessageContent("您的申诉被拒绝，原因：" + appeal.getAdminResponse());
        }
        messages.setMessageType("appeals_notification");
        messages.setCreatedAt(new java.util.Date());
        messages.setRelatedId(appeal.getAppealId());
        messages.setIsRead(false);
        messagesService.save(messages);

        return AjaxResult.success("updated successfully");
    }
}
