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
public class RolesServiceTest {
    @Autowired
    private RolesService rolesService;

    /** 测试分页查询列表 */
    @Test
    public void testFindRolesPage() {
        Instant begin = Instant.now();

        RolesCondition condition = RolesCondition.builder().build();

        IPage<Roles> page = rolesService.findRolesPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindRolesList() {
        Instant begin = Instant.now();

        RolesCondition condition = RolesCondition.builder().build();

        List<Roles> list = rolesService.findRolesList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetRolesById() {
        Instant begin = Instant.now();

        Roles roles = rolesService.getRolesById(243235);
        Console.log("Roles: {}", roles);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddRoles() {
        Instant begin = Instant.now();

        Roles roles = Roles.builder().build();

        boolean bool = rolesService.addRoles(roles);
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
    public void testUpdateRoles() {
        Instant begin = Instant.now();

        Roles roles = rolesService.getRolesById(243235);
        if (roles == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = rolesService.updateRoles(roles);
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
    public void testDeleteRolesById() {
        Instant begin = Instant.now();

        boolean bool = rolesService.deleteRolesById(243235);
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
    public void testDeleteRolesByIds() {
        Instant begin = Instant.now();

        List<Integer> idList = new ArrayList<>();

        boolean bool = rolesService.deleteRolesByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
