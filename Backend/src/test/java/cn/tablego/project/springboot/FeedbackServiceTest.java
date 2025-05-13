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
public class FeedbackServiceTest {
    @Autowired
    private FeedbackService feedbackService;

    /** 测试分页查询列表 */
    @Test
    public void testFindFeedbackPage() {
        Instant begin = Instant.now();

        FeedbackCondition condition = FeedbackCondition.builder().build();

        IPage<Feedback> page = feedbackService.findFeedbackPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindFeedbackList() {
        Instant begin = Instant.now();

        FeedbackCondition condition = FeedbackCondition.builder().build();

        List<Feedback> list = feedbackService.findFeedbackList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetFeedbackById() {
        Instant begin = Instant.now();

        Feedback feedback = feedbackService.getFeedbackById(376877);
        Console.log("Feedback: {}", feedback);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddFeedback() {
        Instant begin = Instant.now();

        Feedback feedback = Feedback.builder().build();

        boolean bool = feedbackService.addFeedback(feedback);
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
    public void testUpdateFeedback() {
        Instant begin = Instant.now();

        Feedback feedback = feedbackService.getFeedbackById(376877);
        if (feedback == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = feedbackService.updateFeedback(feedback);
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
    public void testDeleteFeedbackById() {
        Instant begin = Instant.now();

        boolean bool = feedbackService.deleteFeedbackById(376877);
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
    public void testDeleteFeedbackByIds() {
        Instant begin = Instant.now();

        List<Integer> idList = new ArrayList<>();

        boolean bool = feedbackService.deleteFeedbackByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
