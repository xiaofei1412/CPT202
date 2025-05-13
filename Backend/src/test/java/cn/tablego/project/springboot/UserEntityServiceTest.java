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
public class UserEntityServiceTest {
    @Autowired
    private UserEntityService userEntityService;

    /** 测试分页查询列表 */
    @Test
    public void testFindUserEntityPage() {
        Instant begin = Instant.now();

        UserEntityCondition condition = UserEntityCondition.builder().build();

        IPage<UserEntity> page = userEntityService.findUserEntityPage(condition);
        Console.log("Records: {}", page.getRecords());

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试查询列表 */
    @Test
    public void testFindUserEntityList() {
        Instant begin = Instant.now();

        UserEntityCondition condition = UserEntityCondition.builder().build();

        List<UserEntity> list = userEntityService.findUserEntityList(condition);
        Console.log("List: {}", list);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试根据主键ID查询 */
    @Test
    public void testGetUserEntityById() {
        Instant begin = Instant.now();

        UserEntity userEntity = userEntityService.getUserEntityById(412711L);
        Console.log("UserEntity: {}", userEntity);

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }

    /** 测试新增 */
    @Test
    public void testAddUserEntity() {
        Instant begin = Instant.now();

        UserEntity userEntity = UserEntity.builder().build();

        boolean bool = userEntityService.addUserEntity(userEntity);
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
    public void testUpdateUserEntity() {
        Instant begin = Instant.now();

        UserEntity userEntity = userEntityService.getUserEntityById(412711L);
        if (userEntity == null) {
            Console.log("不存在");
            return;
        }
        boolean bool = userEntityService.updateUserEntity(userEntity);
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
    public void testDeleteUserEntityById() {
        Instant begin = Instant.now();

        boolean bool = userEntityService.deleteUserEntityById(412711L);
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
    public void testDeleteUserEntityByIds() {
        Instant begin = Instant.now();

        List<Long> idList = new ArrayList<>();

        boolean bool = userEntityService.deleteUserEntityByIds(idList);
        if (bool) {
            Console.log("批量删除成功");
        } else {
            Console.log("批量删除失败");
        }

        Instant end = Instant.now();
        Console.log("代码执行消耗时间: {} 毫秒", Duration.between(begin, end).toMillis());
    }
}
