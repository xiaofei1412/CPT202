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
public class SiteSettingsServiceTest {
    @Autowired
    private SiteSettingsService siteSettingsService;

    /** 测试分页查询列表 */
    @Test
    public void testFindSiteSettingsPage() {
        Instant begin = Instant.now();

        SiteSettingsCondition condition = SiteSettingsCondition.builder().build();

        IPage<SiteSettings> page = siteSettingsService.findSiteSettingsPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindSiteSettingsList() {
        Instant begin = Instant.now();

        SiteSettingsCondition condition = SiteSettingsCondition.builder().build();

        List<SiteSettings> list = siteSettingsService.findSiteSettingsList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetSiteSettingsById() {
        Instant begin = Instant.now();

        SiteSettings siteSettings = siteSettingsService.getSiteSettingsById(431596L);
        Console.log("SiteSettings: {}", siteSettings);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddSiteSettings() {
        Instant begin = Instant.now();

        SiteSettings siteSettings = SiteSettings.builder().build();

        boolean bool = siteSettingsService.addSiteSettings(siteSettings);
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
    public void testUpdateSiteSettings() {
        Instant begin = Instant.now();

        SiteSettings siteSettings = siteSettingsService.getSiteSettingsById(431596L);
        if (siteSettings == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = siteSettingsService.updateSiteSettings(siteSettings);
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
    public void testDeleteSiteSettingsById() {
        Instant begin = Instant.now();

        boolean bool = siteSettingsService.deleteSiteSettingsById(431596L);
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
    public void testDeleteSiteSettingsByIds() {
        Instant begin = Instant.now();

        List<Long> idList = new ArrayList<>();

        boolean bool = siteSettingsService.deleteSiteSettingsByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
