package com.itsu.springbootshiro.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 苏犇
 * @date 2019/7/4 22:17
 */
@Data
@TableName("tb_perm")
public class Perm implements Serializable {
    private static final long serialVersionUID = 1075651886455000016L;

    @TableId
    private int permId;
    private String permName;
}
