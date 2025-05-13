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
 * 用户申诉表Service接口测试
 *
 * @author bianj
 * @version 1.0.0 2025-04-23
 */
@SpringBootTest
public class AppealsServiceTest {
    @Autowired
    private AppealsService appealsService;

    /** 测试分页查询用户申诉表列表 */
    @Test
    public void testFindAppealsPage() {
        Instant begin = Instant.now();

        AppealsCondition condition = AppealsCondition.builder().build();

        IPage<Appeals> page = appealsService.findAppealsPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询用户申诉表列表 */
    @Test
    public void testFindAppealsList() {
        Instant begin = Instant.now();

        AppealsCondition condition = AppealsCondition.builder().build();

        List<Appeals> list = appealsService.findAppealsList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询用户申诉表 */
    @Test
    public void testGetAppealsById() {
        Instant begin = Instant.now();

        Appeals appeals = appealsService.getAppealsById(751468);
        Console.log("Appeals: {}", appeals);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增用户申诉表 */
    @Test
    public void testAddAppeals() {
        Instant begin = Instant.now();

        Appeals appeals = Appeals.builder().build();

        boolean bool = appealsService.addAppeals(appeals);
        if (bool) {
            Console.log("新增用户申诉表成功");
        } else {
            Console.log("新增用户申诉表失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试修改用户申诉表 */
    @Test
    public void testUpdateAppeals() {
        Instant begin = Instant.now();

        Appeals appeals = appealsService.getAppealsById(751468);
        if (appeals == null) {
            Console.log("用户申诉表不存在");
            return;
        }
        boolean bool = appealsService.updateAppeals(appeals);
        if (bool) {
            Console.log("修改用户申诉表成功");
        } else {
            Console.log("修改用户申诉表失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID删除用户申诉表 */
    @Test
    public void testDeleteAppealsById() {
        Instant begin = Instant.now();

        boolean bool = appealsService.deleteAppealsById(751468);
        if (bool) {
            Console.log("删除用户申诉表成功");
        } else {
            Console.log("删除用户申诉表失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID列表批量删除用户申诉表 */
    @Test
    public void testDeleteAppealsByIds() {
        Instant begin = Instant.now();

        List<Integer> idList = new ArrayList<>();

        boolean bool = appealsService.deleteAppealsByIds(idList);
        if (bool) {
            Console.log("批量删除用户申诉表成功");
        } else {
            Console.log("批量删除用户申诉表失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
