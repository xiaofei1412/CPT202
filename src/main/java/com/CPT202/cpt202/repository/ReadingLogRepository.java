package com.CPT202.cpt202.repository;

import com.CPT202.cpt202.model.ReadingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingLogRepository extends JpaRepository<ReadingLog, Integer> {

    // 可以在这里定义自定义的查询方法，例如根据用户 ID 查找阅读日志
    // List<ReadingLog> findByUserId(Integer userId);

    // 根据标题模糊搜索阅读日志（忽略大小写）
    // List<ReadingLog> findByTitleContainingIgnoreCase(String keyword);

    // 可以根据 PBI 的需求在这里添加更多自定义的查询方法，例如按日期范围、作者等过滤
}
