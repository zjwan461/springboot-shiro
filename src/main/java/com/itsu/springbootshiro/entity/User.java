package com.itsu.springbootshiro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 苏犇
 * @date 2019/6/27 23:35
 */
@Data
@TableName("tb_user")
public class User implements Serializable {
    private static final long serialVersionUID = -6061351457244160069L;
    @TableId
    private int uid;
    private String userName;
    private String password;
    private String status;
    @TableField(exist = false)
    private boolean rememberMe;
    @TableField(exist = false)
    private Role role;
}
