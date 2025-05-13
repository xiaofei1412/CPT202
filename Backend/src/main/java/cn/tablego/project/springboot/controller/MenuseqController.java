package cn.tablego.project.springboot.controller;

import cn.tablego.project.springboot.common.model.AjaxResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import cn.tablego.project.springboot.service.MenuseqService;
import cn.tablego.project.springboot.model.MenuSeq;
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
@RequestMapping("/menu-seq")
public class MenuseqController {


    @Autowired
    private MenuseqService menuseqService;

    @GetMapping(value = "/list")
    public AjaxResult list(@RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize) {
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        Page<MenuSeq> aPage = menuseqService.page(new Page<>(pageNum, pageSize));
        return AjaxResult.success(aPage);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getById(@PathVariable("id") Long id) {
        return AjaxResult.success(menuseqService.getById(id));
    }

    @PostMapping(value = "/create")
    public AjaxResult create(@RequestBody MenuSeq params) {
        menuseqService.save(params);
        return AjaxResult.success("created successfully");
    }

    @GetMapping(value = "/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        menuseqService.removeById(id);
        return AjaxResult.success("deleted successfully");
    }

    @PostMapping(value = "/update")
    public AjaxResult update(@RequestBody MenuSeq params) {
        menuseqService.updateById(params);
        return AjaxResult.success("updated successfully");
    }
}
