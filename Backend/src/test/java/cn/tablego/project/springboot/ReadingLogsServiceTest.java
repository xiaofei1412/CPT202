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
public class ReadingLogsServiceTest {
    @Autowired
    private ReadingLogsService readingLogsService;

    /** 测试分页查询列表 */
    @Test
    public void testFindReadingLogsPage() {
        Instant begin = Instant.now();

        ReadingLogsCondition condition = ReadingLogsCondition.builder().build();

        IPage<ReadingLogs> page = readingLogsService.findReadingLogsPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindReadingLogsList() {
        Instant begin = Instant.now();

        ReadingLogsCondition condition = ReadingLogsCondition.builder().build();

        List<ReadingLogs> list = readingLogsService.findReadingLogsList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetReadingLogsById() {
        Instant begin = Instant.now();

        ReadingLogs readingLogs = readingLogsService.getReadingLogsById(255982);
        Console.log("ReadingLogs: {}", readingLogs);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddReadingLogs() {
        Instant begin = Instant.now();

        ReadingLogs readingLogs = ReadingLogs.builder().build();

        boolean bool = readingLogsService.addReadingLogs(readingLogs);
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
    public void testUpdateReadingLogs() {
        Instant begin = Instant.now();

        ReadingLogs readingLogs = readingLogsService.getReadingLogsById(255982);
        if (readingLogs == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = readingLogsService.updateReadingLogs(readingLogs);
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
    public void testDeleteReadingLogsById() {
        Instant begin = Instant.now();

        boolean bool = readingLogsService.deleteReadingLogsById(255982);
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
    public void testDeleteReadingLogsByIds() {
        Instant begin = Instant.now();

        List<Integer> idList = new ArrayList<>();

        boolean bool = readingLogsService.deleteReadingLogsByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
