package com.itheima.reggie.dto;

import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.pojo.SetmealDish;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 套餐Dto
 */
@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes = new ArrayList<>(); //套餐关联的菜品集合
    private String categoryName; //分类名称
}
