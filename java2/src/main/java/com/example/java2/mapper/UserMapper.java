package com.example.java2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.java2.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Mapper 接口
 *
 * @author cym
 * @since 2021-12-12
 */
@Component
@Repository
public interface UserMapper extends BaseMapper<User> {

  IPage<User> selectUserPage (Page<User> page);
}
