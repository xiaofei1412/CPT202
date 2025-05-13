package cn.tablego.project.springboot.service.impl;

import cn.tablego.project.springboot.model.Logs;
import cn.tablego.project.springboot.mapper.LogsMapper;
import cn.tablego.project.springboot.service.LogsService;
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
public class LogsServiceImpl extends ServiceImpl<LogsMapper, Logs> implements LogsService {

}
