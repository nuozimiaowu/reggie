package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.pojo.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    //保存套餐的增强方法
    void saveSetMeal(SetmealDto setmealDto);

    Page<SetmealDto> pageSetmeal(Integer page, Integer pageSize, String name);

    void deleteByIds(Long[] ids);

    void changeStatus(Integer status, Long[] ids);

    SetmealDto getStealWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);
}
