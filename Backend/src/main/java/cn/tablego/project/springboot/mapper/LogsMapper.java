package cn.tablego.project.springboot.mapper;

import cn.tablego.project.springboot.model.Logs;
import cn.tablego.project.springboot.model.PlatVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author
 * @since 2025-04-23
 */
@Mapper
public interface LogsMapper extends BaseMapper<Logs> {

    Page<Logs> page(@Param("page") Page<Object> objectPage, @Param("params") Logs params);

    Page<Logs> page1(Page<Object> objectPage, Logs params);

    List<Logs> logs(@Param("userId") Integer userId, @Param("type") Integer type);

    Integer days(@Param("userId") Integer userId, @Param("type") Integer type);

    @MapKey("key")
    Map<String, Map<String, Integer>> typeMap(@Param("userId") Integer userId, @Param("type") Integer type);

    @MapKey("key")
    Map<String, Map<String, Integer>> weekMap(Integer userId);
    @MapKey("key")
    Map<String, Map<String, Integer>> monthMap(Integer userId);
    @MapKey("key")
    Map<Long, Map<String, Integer>> yearMap(Integer userId);

    List<PlatVo> recent7days();

}
