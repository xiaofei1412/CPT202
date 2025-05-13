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
public class SensitiveWordsServiceTest {
    @Autowired
    private SensitiveWordsService sensitiveWordsService;

    /** 测试分页查询列表 */
    @Test
    public void testFindSensitiveWordsPage() {
        Instant begin = Instant.now();

        SensitiveWordsCondition condition = SensitiveWordsCondition.builder().build();

        IPage<SensitiveWords> page = sensitiveWordsService.findSensitiveWordsPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindSensitiveWordsList() {
        Instant begin = Instant.now();

        SensitiveWordsCondition condition = SensitiveWordsCondition.builder().build();

        List<SensitiveWords> list = sensitiveWordsService.findSensitiveWordsList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetSensitiveWordsById() {
        Instant begin = Instant.now();

        SensitiveWords sensitiveWords = sensitiveWordsService.getSensitiveWordsById(184479L);
        Console.log("SensitiveWords: {}", sensitiveWords);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddSensitiveWords() {
        Instant begin = Instant.now();

        SensitiveWords sensitiveWords = SensitiveWords.builder().build();

        boolean bool = sensitiveWordsService.addSensitiveWords(sensitiveWords);
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
    public void testUpdateSensitiveWords() {
        Instant begin = Instant.now();

        SensitiveWords sensitiveWords = sensitiveWordsService.getSensitiveWordsById(184479L);
        if (sensitiveWords == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = sensitiveWordsService.updateSensitiveWords(sensitiveWords);
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
    public void testDeleteSensitiveWordsById() {
        Instant begin = Instant.now();

        boolean bool = sensitiveWordsService.deleteSensitiveWordsById(184479L);
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
    public void testDeleteSensitiveWordsByIds() {
        Instant begin = Instant.now();

        List<Long> idList = new ArrayList<>();

        boolean bool = sensitiveWordsService.deleteSensitiveWordsByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
