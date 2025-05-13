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
public class EmailConfigServiceTest {
    @Autowired
    private EmailConfigService emailConfigService;

    /** 测试分页查询列表 */
    @Test
    public void testFindEmailConfigPage() {
        Instant begin = Instant.now();

        EmailConfigCondition condition = EmailConfigCondition.builder().build();

        IPage<EmailConfig> page = emailConfigService.findEmailConfigPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindEmailConfigList() {
        Instant begin = Instant.now();

        EmailConfigCondition condition = EmailConfigCondition.builder().build();

        List<EmailConfig> list = emailConfigService.findEmailConfigList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetEmailConfigById() {
        Instant begin = Instant.now();

        EmailConfig emailConfig = emailConfigService.getEmailConfigById(753518L);
        Console.log("EmailConfig: {}", emailConfig);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddEmailConfig() {
        Instant begin = Instant.now();

        EmailConfig emailConfig = EmailConfig.builder().build();

        boolean bool = emailConfigService.addEmailConfig(emailConfig);
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
    public void testUpdateEmailConfig() {
        Instant begin = Instant.now();

        EmailConfig emailConfig = emailConfigService.getEmailConfigById(753518L);
        if (emailConfig == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = emailConfigService.updateEmailConfig(emailConfig);
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
    public void testDeleteEmailConfigById() {
        Instant begin = Instant.now();

        boolean bool = emailConfigService.deleteEmailConfigById(753518L);
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
    public void testDeleteEmailConfigByIds() {
        Instant begin = Instant.now();

        List<Long> idList = new ArrayList<>();

        boolean bool = emailConfigService.deleteEmailConfigByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
