package cn.tablego.project.springboot.service.impl;

import cn.tablego.project.springboot.model.Config;
import cn.tablego.project.springboot.mapper.ConfigMapper;
import cn.tablego.project.springboot.service.ConfigService;
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
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

}
