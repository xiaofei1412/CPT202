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
public class UsersServiceTest {
    @Autowired
    private UsersService usersService;

    /** 测试分页查询列表 */
    @Test
    public void testFindUsersPage() {
        Instant begin = Instant.now();

        UsersCondition condition = UsersCondition.builder().build();

        IPage<Users> page = usersService.findUsersPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindUsersList() {
        Instant begin = Instant.now();

        UsersCondition condition = UsersCondition.builder().build();

        List<Users> list = usersService.findUsersList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetUsersById() {
        Instant begin = Instant.now();

        Users users = usersService.getUsersById(915831);
        Console.log("Users: {}", users);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddUsers() {
        Instant begin = Instant.now();

        Users users = Users.builder().build();

        boolean bool = usersService.addUsers(users);
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
    public void testUpdateUsers() {
        Instant begin = Instant.now();

        Users users = usersService.getUsersById(915831);
        if (users == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = usersService.updateUsers(users);
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
    public void testDeleteUsersById() {
        Instant begin = Instant.now();

        boolean bool = usersService.deleteUsersById(915831);
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
    public void testDeleteUsersByIds() {
        Instant begin = Instant.now();

        List<Integer> idList = new ArrayList<>();

        boolean bool = usersService.deleteUsersByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
