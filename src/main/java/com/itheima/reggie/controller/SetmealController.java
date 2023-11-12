package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.pojo.SetmealDish;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    DishService dishService;

    @Autowired
    SetmealDishService setmealDishService;

    @PostMapping
    public R<String> setmeal(@RequestBody SetmealDto setmealDto){
    setmealService.saveSetMeal(setmealDto);
    return R.success("保存成功");
    }

    @GetMapping("page")
    public R<Page<SetmealDto>> page(Integer page, Integer pageSize, String name){
        Page<SetmealDto> page1 = setmealService.pageSetmeal(page,pageSize,name);
        return R.success(page1);
    }
    @DeleteMapping()
    public R<String> deleteByIds(Long[] ids){
        setmealService.deleteByIds(ids);
        return R.success("删除成功");
    }
    @PostMapping("status/{status}")
    public R<String> changeStatus(@PathVariable Integer status,Long[] ids){
        setmealService.changeStatus(status,ids);
        return R.success("修改成功");
    }

    @GetMapping("{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto dishDtoR = setmealService.getStealWithDish(id);
        return R.success(dishDtoR);
    }
    @PutMapping()
    public R update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("修改成功");
    }

    @GetMapping("list")
    public R<List<Setmeal>> list(Long categoryId,Integer status){
        List<Setmeal> list = setmealService.lambdaQuery().
                eq(Setmeal::getCategoryId, categoryId).
                eq(Setmeal::getStatus, status).list();
        return R.success(list);
    }
    @GetMapping("dish/{categoryId}")
    public R<List<Dish>> dish(@PathVariable Long categoryId){
        List<SetmealDish> list1 = setmealDishService.lambdaQuery().
                select(SetmealDish::getDishId).
                eq(SetmealDish::getSetmealId, categoryId).
                list();
        ArrayList arrayList = new ArrayList();
        list1.forEach(o->{
            Long dishId = o.getDishId();
            arrayList.add(dishId);
        });

        List<Dish> list = dishService.lambdaQuery().in(Dish::getId, arrayList).list();
        return R.success(list);
    }
}


















