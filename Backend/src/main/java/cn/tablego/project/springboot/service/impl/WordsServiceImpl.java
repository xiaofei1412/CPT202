package cn.tablego.project.springboot.service.impl;

import cn.tablego.project.springboot.model.Words;
import cn.tablego.project.springboot.mapper.WordsMapper;
import cn.tablego.project.springboot.service.WordsService;
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
public class WordsServiceImpl extends ServiceImpl<WordsMapper, Words> implements WordsService {

}
