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
public class ContentSettingsServiceTest {
    @Autowired
    private ContentSettingsService contentSettingsService;

    /** 测试分页查询列表 */
    @Test
    public void testFindContentSettingsPage() {
        Instant begin = Instant.now();

        ContentSettingsCondition condition = ContentSettingsCondition.builder().build();

        IPage<ContentSettings> page = contentSettingsService.findContentSettingsPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindContentSettingsList() {
        Instant begin = Instant.now();

        ContentSettingsCondition condition = ContentSettingsCondition.builder().build();

        List<ContentSettings> list = contentSettingsService.findContentSettingsList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetContentSettingsById() {
        Instant begin = Instant.now();

        ContentSettings contentSettings = contentSettingsService.getContentSettingsById(812945L);
        Console.log("ContentSettings: {}", contentSettings);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddContentSettings() {
        Instant begin = Instant.now();

        ContentSettings contentSettings = ContentSettings.builder().build();

        boolean bool = contentSettingsService.addContentSettings(contentSettings);
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
    public void testUpdateContentSettings() {
        Instant begin = Instant.now();

        ContentSettings contentSettings = contentSettingsService.getContentSettingsById(812945L);
        if (contentSettings == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = contentSettingsService.updateContentSettings(contentSettings);
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
    public void testDeleteContentSettingsById() {
        Instant begin = Instant.now();

        boolean bool = contentSettingsService.deleteContentSettingsById(812945L);
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
    public void testDeleteContentSettingsByIds() {
        Instant begin = Instant.now();

        List<Long> idList = new ArrayList<>();

        boolean bool = contentSettingsService.deleteContentSettingsByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
