package com.itsu.springbootshiro.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author 苏犇
 * @date 2019/6/27 23:35
 */
@Data
@TableName("tb_user")
public class User {
    @TableId
    private int uid;
    private String userName;
    private String password;
    private String status;
}
