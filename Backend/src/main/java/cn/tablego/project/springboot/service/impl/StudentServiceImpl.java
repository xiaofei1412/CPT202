package cn.tablego.project.springboot.service.impl;

import cn.tablego.project.springboot.model.Student;
import cn.tablego.project.springboot.mapper.StudentMapper;
import cn.tablego.project.springboot.service.StudentService;
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
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

}
