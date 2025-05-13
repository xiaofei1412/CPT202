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
public class AMenuServiceTest {
    @Autowired
    private AMenuService aMenuService;

    /** 测试分页查询列表 */
    @Test
    public void testFindAMenuPage() {
        Instant begin = Instant.now();

        AMenuCondition condition = AMenuCondition.builder().build();

        IPage<AMenu> page = aMenuService.findAMenuPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindAMenuList() {
        Instant begin = Instant.now();

        AMenuCondition condition = AMenuCondition.builder().build();

        List<AMenu> list = aMenuService.findAMenuList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetAMenuById() {
        Instant begin = Instant.now();

        AMenu aMenu = aMenuService.getAMenuById(267284);
        Console.log("AMenu: {}", aMenu);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddAMenu() {
        Instant begin = Instant.now();

        AMenu aMenu = AMenu.builder().build();

        boolean bool = aMenuService.addAMenu(aMenu);
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
    public void testUpdateAMenu() {
        Instant begin = Instant.now();

        AMenu aMenu = aMenuService.getAMenuById(267284);
        if (aMenu == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = aMenuService.updateAMenu(aMenu);
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
    public void testDeleteAMenuById() {
        Instant begin = Instant.now();

        boolean bool = aMenuService.deleteAMenuById(267284);
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
    public void testDeleteAMenuByIds() {
        Instant begin = Instant.now();

        List<Integer> idList = new ArrayList<>();

        boolean bool = aMenuService.deleteAMenuByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
