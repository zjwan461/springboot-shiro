package com.itsu.springbootshiro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 苏犇
 * @date 2019/7/4 22:15
 */
@Data
@TableName("tb_role")
public class Role implements Serializable {
    private static final long serialVersionUID = -8179962714461276909L;
    @TableId
    private int rid;
    private String roleName;
    private String status;

    @TableField(exist = false)
    List<Perm> perms;
}
