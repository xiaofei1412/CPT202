package cn.tablego.project.springboot;

import java.time.Duration;
import java.time.Instant;
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
public class AMenuSeqServiceTest {
    @Autowired
    private AMenuSeqService aMenuSeqService;

    /** 测试分页查询列表 */
    @Test
    public void testFindAMenuSeqPage() {
        Instant begin = Instant.now();

        AMenuSeqCondition condition = AMenuSeqCondition.builder().build();

        IPage<AMenuSeq> page = aMenuSeqService.findAMenuSeqPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindAMenuSeqList() {
        Instant begin = Instant.now();

        AMenuSeqCondition condition = AMenuSeqCondition.builder().build();

        List<AMenuSeq> list = aMenuSeqService.findAMenuSeqList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddAMenuSeq() {
        Instant begin = Instant.now();

        AMenuSeq aMenuSeq = AMenuSeq.builder().build();

        boolean bool = aMenuSeqService.addAMenuSeq(aMenuSeq);
        if (bool) {
            Console.log("新增成功");
        } else {
            Console.log("新增失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
