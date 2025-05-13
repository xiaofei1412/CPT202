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
import cn.tablego.project.springboot.service.FeedbackService;
import cn.tablego.project.springboot.model.Feedback;
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
@RequestMapping("/feedback")
public class FeedbackController {


    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private MessagesService messagesService;

    @GetMapping(value = "/list")
    public AjaxResult list(@RequestParam(required = false) Integer pageNum,
                           @RequestParam(required = false) Integer pageSize,
                           String status, String type) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("created_at");
        if (status != null) {
            if (!status.equals("all")) {
                queryWrapper.eq("status", status);
            }
            if (type != null) {
                if (!type.equals("all")) {
                    queryWrapper.eq("feedback_type", type);
                }
            }
        }
        Page<Feedback> aPage = feedbackService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return AjaxResult.success(aPage);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getById(@PathVariable("id") Long id) {
        return AjaxResult.success(feedbackService.getById(id));
    }

    @PostMapping(value = "/create")
    public AjaxResult create(@RequestBody Feedback params) {
        params.setCreatedAt(new java.util.Date());
        params.setStatus("pending");
        Integer userId = params.getUserId();
        Users user = usersService.getById(userId);
        params.setUsername(user.getNickname());
        feedbackService.save(params);
        return AjaxResult.success("created successfully");
    }

    @GetMapping(value = "/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        feedbackService.removeById(id);
        return AjaxResult.success("deleted successfully");
    }

    @PostMapping(value = "/update")
    public AjaxResult update(@RequestBody Feedback params) {
        feedbackService.updateById(params);
        Feedback feedback = feedbackService.getById(params.getFeedbackId());
        String adminResponse = feedback.getAdminResponse();
        if (StringUtils.isNotBlank(adminResponse)) {
            Messages messages = new Messages();
            messages.setUserId(feedback.getUserId());
            messages.setMessageContent("回复信息:" + feedback.getAdminResponse());
            messages.setMessageType("feedback_notification");
            messages.setCreatedAt(new java.util.Date());
            messages.setRelatedId(feedback.getFeedbackId());
            messages.setIsRead(false);
            messagesService.save(messages);
        }
        return AjaxResult.success("updated successfully");
    }
}
