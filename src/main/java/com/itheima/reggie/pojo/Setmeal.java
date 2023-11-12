package com.itheima.reggie.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 套餐
 */
@Data
public class Setmeal implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long categoryId;    //分类id
    private String name;    //套餐名称
    private BigDecimal price;    //套餐价格
    private Integer status;    //状态 0:停用 1:启用
    private String code;    //编码
    private String description;    //描述信息
    private String image;    //图片

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; //创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; //更新时间
    @TableField(fill = FieldFill.INSERT)
    private Long createUser; //创建人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;//更新人
}
