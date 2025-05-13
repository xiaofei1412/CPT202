package cn.tablego.project.springboot.controller;

import cn.tablego.project.springboot.common.model.AjaxResult;
import cn.tablego.project.springboot.mapper.LogsMapper;
import cn.tablego.project.springboot.mapper.UsersMapper;
import cn.tablego.project.springboot.model.Messages;
import cn.tablego.project.springboot.model.PlatVo;
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
import cn.tablego.project.springboot.service.LogsService;
import cn.tablego.project.springboot.model.Logs;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author
 * @since 2025-04-23
 */
@RestController
@RequestMapping("/logs")
public class LogsController {


    @Autowired
    private LogsService logsService;

    @Autowired
    private LogsMapper logsMapper;

    @Autowired
    private MessagesService messagesService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersMapper usersMapper;

    @PostMapping(value = "/list")
    public AjaxResult list(@RequestBody Logs params) {

        Page<Object> objectPage = new Page<>(params.getPageNum(), params.getPageSize());
        Page<Logs> aPage = logsMapper.page(objectPage, params);
        List<Logs> records = aPage.getRecords();
        for (Logs record : records) {
            Integer readingLogId = record.getReadingLogId();
            Logs logs = logsService.getById(readingLogId);
            record.setTags(logs.getTags());
        }
//        Page<Logs> aPage = logsService.page(, queryWrapper);
        return AjaxResult.success(aPage);
    }

    @PostMapping(value = "/admin/list")
    public AjaxResult adminlist(@RequestBody Logs params) {
        if(params.getStatus()!= null &&params.getStatus().equals("all")) {
            params.setStatus("");
        }

        Page<Object> objectPage = new Page<>(params.getPageNum(), params.getPageSize());
        Page<Logs> aPage = logsMapper.page1(objectPage, params);
//        Page<Logs> aPage = logsService.page(, queryWrapper);
        return AjaxResult.success(aPage);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getById(@PathVariable("id") Long id) {
        return AjaxResult.success(logsService.getById(id));
    }

    @PostMapping(value = "/create")
    public AjaxResult create(@RequestBody Logs params) {
        params.setCreatedAt(new java.util.Date());
        params.setUpdatedAt(new java.util.Date());
        params.setStatus("pending");
        logsService.save(params);
        return AjaxResult.success("created successfully");
    }

    @GetMapping(value = "/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        logsService.removeById(id);
        return AjaxResult.success("deleted successfully");
    }

    @PostMapping(value = "/update")
    public AjaxResult update(@RequestBody Logs params) {
        params.setUpdatedAt(new java.util.Date());

        logsService.updateById(params);
        Logs logs = logsService.getById(params.getReadingLogId());
        if (params.getAduit()!= null && params.getAduit().equals(1)) {
            // 发送系统通知
            Integer userId = logs.getUserId();
            Messages messages = new Messages();
            messages.setUserId(userId);
            if (logs.getStatus().equals("approved")) {
                messages.setMessageContent("您的阅读日志已通过审核");
            } else if (logs.getStatus().equals("rejected")) {
                messages.setMessageContent("您的阅读日志被拒绝，原因：" + logs.getRejectReason());
            }
            messages.setMessageType("system_notification");
            messages.setCreatedAt(new java.util.Date());
            messages.setRelatedId(logs.getReadingLogId());
            messages.setIsRead(false);
            messagesService.save(messages);
        }


        return AjaxResult.success("updated successfully");
    }

    /**
     * 审核
     */
    @GetMapping(value = "/audit")
    public AjaxResult audit(@RequestParam(required = false) Integer id, @RequestParam(required = false) String status) {
        Logs logs = logsService.getById(id);
        logs.setStatus(status);
        logsService.updateById(logs);


        return AjaxResult.success("updated successfully");
    }

    /**
     * 平台数据
     */
    @GetMapping(value = "/platform/data")
    public AjaxResult platform() {
        QueryWrapper<Logs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "approved");
        List<Logs> list = logsService.list();
        Integer sumTime = 0;
        for (Logs logs : list) {
            sumTime += logs.getReadingTime();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("sumTime", sumTime);
        map.put("sumReadingLog", list.size());
        List<Users> list1 = usersService.list();
        map.put("sumUser", list1.size());
        List<Users> collect = list1.stream().filter(e -> !e.getIsActivated()).collect(Collectors.toList());
        map.put("sumBanUser", collect.size());
        List<Logs> collect1 = list.stream().filter(e -> e.getStatus().equals("pending")).collect(Collectors.toList());
        map.put("sumInProgressLog", collect1.size());

        // 最近t7天的新用户列表
        List<PlatVo> map1 = usersMapper.recent7days();
        map.put("recent7days", map1);

        // 最近7天，每天的阅读量
        List<PlatVo> map2 =logsMapper.recent7days();
        map.put("recent7daysLogs", map2);
        return AjaxResult.success(map);
    }

    /**
     * 我的仪表盘
     */
    @GetMapping(value = "/my/data")
    public AjaxResult my(Integer userId, Integer type) {
        Map<String, Object> map = new HashMap<>();
        // 我的阅读时间
        List<Logs> logs = logsMapper.logs(userId, type);
        Integer sumTime = 0;
        for (Logs log : logs) {
            sumTime += log.getReadingTime();
        }
        map.put("sumTime", sumTime);

        // 连续阅读天数
        Integer days = logsMapper.days(userId, type);
        map.put("days", days);

        // 阅读类别
        Map<String, Map<String, Integer>> typeMap = logsMapper.typeMap(userId, type);
        LinkedHashMap<String, Integer> newMap1 = new LinkedHashMap<>();
        for (String s : typeMap.keySet()) {
            Map<String, Integer> map1 = typeMap.get(s);
            newMap1.put(s, map1.get("value"));

        }
        map.put("typeMap", newMap1);

        // 最近阅读日志
        QueryWrapper<Logs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("reading_date");
        queryWrapper.last("limit 3");
        List<Logs> list = logsService.list(queryWrapper);
        map.put("list", list);

        if (type == 1) {
            // 本周阅读时间
            Map<String, Map<String, Integer>> weekMap = logsMapper.weekMap(userId);
            LinkedHashMap<String, Integer> newMap = new LinkedHashMap<>();
            newMap.put("Mon", weekMap.get("Monday").get("value"));
            newMap.put("Tue", weekMap.get("Tuesday").get("value"));
            newMap.put("Wed", weekMap.get("Wednesday").get("value"));
            newMap.put("Thu", weekMap.get("Thursday").get("value"));
            newMap.put("Fri", weekMap.get("Friday").get("value"));
            newMap.put("Sat", weekMap.get("Saturday").get("value"));
            newMap.put("Sun", weekMap.get("Sunday").get("value"));
            map.put("readDays", newMap);
        } else if (type == 2) {
            // 本月的阅读时间
            Map<String, Map<String, Integer>> monthMap = logsMapper.monthMap(userId);
            LinkedHashMap<String, Integer> newMap = new LinkedHashMap<>();
            newMap.put("Week1", monthMap.get("Week1").get("value"));
            newMap.put("Week2", monthMap.get("Week2").get("value"));
            newMap.put("Week3", monthMap.get("Week3").get("value"));
            newMap.put("Week4", monthMap.get("Week4").get("value"));

            map.put("readDays", newMap);
        } else if (type == 3) {
            // 本年的阅读时间
            Map<Long, Map<String, Integer>> yearMap = logsMapper.yearMap(userId);
            LinkedHashMap<String, Integer> newMap = new LinkedHashMap<>();
            // 遍历 1 到 12 月
            for (Long integer : yearMap.keySet()) {
                newMap.put(integer + "", yearMap.get(integer).get("value"));
            }

            map.put("readDays", newMap);
        }
        return AjaxResult.success(map);
    }
}
