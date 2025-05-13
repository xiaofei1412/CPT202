package cn.tablego.project.springboot.mapper;

import cn.tablego.project.springboot.model.PlatVo;
import cn.tablego.project.springboot.model.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedHashMap;
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
public interface UsersMapper extends BaseMapper<Users> {

    List<PlatVo> recent7days();
}
