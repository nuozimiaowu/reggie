package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {


    void saveDishFlavorWith(DishDto dishDto);

    //扩展方法：分页查询菜品数据，包括分类名称
    Page<DishDto> pageDishDto(Integer page, Integer pageSize, String name);

    //扩展方法：批量删除菜品，但是删除菜品的状态不能是起售。
    void deleteByIds(Long[] ids);

    //扩展方法：回显数据，包括菜品，菜品的分类，口味。
    DishDto getDishByIdWithFlavor(Long id);

    //扩展方法：实现更改
    void updateByGiven(DishDto dishDto);

    List<DishDto> listDishDto(Long categoryId, String name);
}