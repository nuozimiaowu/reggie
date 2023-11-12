package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomerException;
import com.itheima.reggie.common.R;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.pojo.Category;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void deleteById(Long id) {
        //1。根据分类的id查询菜品
        //select * from dish where category_id =?
        Integer count1 = dishService.lambdaQuery().eq(Dish::getCategoryId, id).count();
        //2。根据分类的id去查询套餐
        //select * from setmeal where category_id =?
        Integer count2 = setmealService.lambdaQuery().eq(Setmeal::getId, id).count();

        if(count1>0){
            //出现了不能删除的情况，让代码停止结束，抛出一个异常。
            throw new CustomerException("该分类不能删除，有【菜品】关联数据！");
        }
        if(count2>0){
            //出现了不能删除的情况，让代码停止结束，抛出一个异常。
            throw new CustomerException("该分类不能删除，有【套餐】关联数据！");
        }
        //3.根据id删除数据
        // delete from category where category_id=?
        removeById(id);

    }
}
