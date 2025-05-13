package cn.tablego.project.springboot.service.impl;

import cn.tablego.project.springboot.model.Messages;
import cn.tablego.project.springboot.mapper.MessagesMapper;
import cn.tablego.project.springboot.service.MessagesService;
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
public class MessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages> implements MessagesService {

}
