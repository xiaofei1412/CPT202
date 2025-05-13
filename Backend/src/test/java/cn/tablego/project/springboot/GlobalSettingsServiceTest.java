package cn.tablego.project.springboot;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.baomidou.mybatisplus.core.metadata.IPage;

import cn.hutool.core.lang.Console;

/**
 * Service接口测试
 *
 * @author bianj
 * @version 1.0.0 2025-04-23
 */
@SpringBootTest
public class GlobalSettingsServiceTest {
    @Autowired
    private GlobalSettingsService globalSettingsService;

    /** 测试分页查询列表 */
    @Test
    public void testFindGlobalSettingsPage() {
        Instant begin = Instant.now();

        GlobalSettingsCondition condition = GlobalSettingsCondition.builder().build();

        IPage<GlobalSettings> page = globalSettingsService.findGlobalSettingsPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindGlobalSettingsList() {
        Instant begin = Instant.now();

        GlobalSettingsCondition condition = GlobalSettingsCondition.builder().build();

        List<GlobalSettings> list = globalSettingsService.findGlobalSettingsList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetGlobalSettingsById() {
        Instant begin = Instant.now();

        GlobalSettings globalSettings = globalSettingsService.getGlobalSettingsById(935257);
        Console.log("GlobalSettings: {}", globalSettings);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddGlobalSettings() {
        Instant begin = Instant.now();

        GlobalSettings globalSettings = GlobalSettings.builder().build();

        boolean bool = globalSettingsService.addGlobalSettings(globalSettings);
        if (bool) {
            Console.log("新增成功");
        } else {
            Console.log("新增失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试修改 */
    @Test
    public void testUpdateGlobalSettings() {
        Instant begin = Instant.now();

        GlobalSettings globalSettings = globalSettingsService.getGlobalSettingsById(935257);
        if (globalSettings == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = globalSettingsService.updateGlobalSettings(globalSettings);
        if (bool) {
            Console.log("修改成功");
        } else {
            Console.log("修改失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID删除 */
    @Test
    public void testDeleteGlobalSettingsById() {
        Instant begin = Instant.now();

        boolean bool = globalSettingsService.deleteGlobalSettingsById(935257);
        if (bool) {
            Console.log("删除成功");
        } else {
            Console.log("删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID列表批量删除 */
    @Test
    public void testDeleteGlobalSettingsByIds() {
        Instant begin = Instant.now();

        List<Integer> idList = new ArrayList<>();

        boolean bool = globalSettingsService.deleteGlobalSettingsByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
