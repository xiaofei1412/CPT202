package cn.tablego.project.springboot.controller;

import cn.tablego.project.springboot.common.model.AjaxResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import cn.tablego.project.springboot.service.MessagesService;
import cn.tablego.project.springboot.model.Messages;
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
@RequestMapping("/messages")
public class MessagesController {


    @Autowired
    private MessagesService messagesService;

    @GetMapping(value = "/list")
    public AjaxResult list(@RequestParam(required = false) Integer pageNum,
                           @RequestParam(required = false) Integer pageSize,
                            Integer isRead, Integer userId) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        QueryWrapper<Messages> queryWrapper = new QueryWrapper<>();
        if (isRead != null && isRead.equals(0)) {
            queryWrapper.eq("is_read", isRead);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        Page<Messages> aPage = messagesService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return AjaxResult.success(aPage);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getById(@PathVariable("id") Long id) {
        return AjaxResult.success(messagesService.getById(id));
    }

    @PostMapping(value = "/create")
    public AjaxResult create(@RequestBody Messages params) {
        messagesService.save(params);
        return AjaxResult.success("created successfully");
    }

    @GetMapping(value = "/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        messagesService.removeById(id);
        return AjaxResult.success("deleted successfully");
    }

    @PostMapping(value = "/update")
    public AjaxResult update(@RequestBody Messages params) {
        messagesService.updateById(params);
        return AjaxResult.success("updated successfully");
    }
}
