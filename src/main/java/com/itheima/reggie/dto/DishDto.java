package com.itheima.reggie.dto;

import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜品Dto
 */
@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors = new ArrayList<>(); //菜品口味关系
    private String categoryName; //菜品分类名称
    private Integer copies;
}
