package cn.tablego.project.springboot.service.impl;

import cn.tablego.project.springboot.model.Users;
import cn.tablego.project.springboot.mapper.UsersMapper;
import cn.tablego.project.springboot.service.UsersService;
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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
