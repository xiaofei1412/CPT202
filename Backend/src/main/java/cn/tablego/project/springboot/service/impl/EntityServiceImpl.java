package cn.tablego.project.springboot.service.impl;

import cn.tablego.project.springboot.model.Entity;
import cn.tablego.project.springboot.mapper.EntityMapper;
import cn.tablego.project.springboot.service.EntityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2025-04-23
 */
@Service
public class EntityServiceImpl extends ServiceImpl<EntityMapper, Entity> implements EntityService {

}
