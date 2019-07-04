package com.itsu.springbootshiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itsu.springbootshiro.entity.User;

/**
 * @author 苏犇
 * @date 2019/6/27 23:40
 */

public interface UserMapper extends BaseMapper<User> {

    User getUserRolePermByUsername(String userName);

}
