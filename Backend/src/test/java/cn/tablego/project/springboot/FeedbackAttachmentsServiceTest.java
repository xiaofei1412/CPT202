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
public class FeedbackAttachmentsServiceTest {
    @Autowired
    private FeedbackAttachmentsService feedbackAttachmentsService;

    /** 测试分页查询列表 */
    @Test
    public void testFindFeedbackAttachmentsPage() {
        Instant begin = Instant.now();

        FeedbackAttachmentsCondition condition = FeedbackAttachmentsCondition.builder().build();

        IPage<FeedbackAttachments> page = feedbackAttachmentsService.findFeedbackAttachmentsPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindFeedbackAttachmentsList() {
        Instant begin = Instant.now();

        FeedbackAttachmentsCondition condition = FeedbackAttachmentsCondition.builder().build();

        List<FeedbackAttachments> list = feedbackAttachmentsService.findFeedbackAttachmentsList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetFeedbackAttachmentsById() {
        Instant begin = Instant.now();

        FeedbackAttachments feedbackAttachments = feedbackAttachmentsService.getFeedbackAttachmentsById(629125);
        Console.log("FeedbackAttachments: {}", feedbackAttachments);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddFeedbackAttachments() {
        Instant begin = Instant.now();

        FeedbackAttachments feedbackAttachments = FeedbackAttachments.builder().build();

        boolean bool = feedbackAttachmentsService.addFeedbackAttachments(feedbackAttachments);
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
    public void testUpdateFeedbackAttachments() {
        Instant begin = Instant.now();

        FeedbackAttachments feedbackAttachments = feedbackAttachmentsService.getFeedbackAttachmentsById(629125);
        if (feedbackAttachments == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = feedbackAttachmentsService.updateFeedbackAttachments(feedbackAttachments);
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
    public void testDeleteFeedbackAttachmentsById() {
        Instant begin = Instant.now();

        boolean bool = feedbackAttachmentsService.deleteFeedbackAttachmentsById(629125);
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
    public void testDeleteFeedbackAttachmentsByIds() {
        Instant begin = Instant.now();

        List<Integer> idList = new ArrayList<>();

        boolean bool = feedbackAttachmentsService.deleteFeedbackAttachmentsByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
