package com.itheima.reggie.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类
 */
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Integer type;   //类型 1 菜品分类 2 套餐分类
    private String name;    //分类名称
    private Integer sort;    //顺序

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;    //创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;    //更新时间
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;    //创建人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;    //修改人
}
