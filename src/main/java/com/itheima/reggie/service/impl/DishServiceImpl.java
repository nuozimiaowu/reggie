package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomerException;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.pojo.DishFlavor;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishService dishService;

    //引入dishFlavorService来实现保存菜品的口味
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<DishDto> listDishDto(Long categoryId, String name) {
        return dishMapper.listDishDto(categoryId,name);
    }

    @Override
    //@Transactional
    public void saveDishFlavorWith(DishDto dishDto) {
        //1.添加菜品，往dish里面添加一条数据
        this.save(dishDto);
        //2.添加口味数据，向dish_flavor里面添加多条数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        //TODO 给每一个属性设置菜品的id
        flavors.forEach(flavor->{flavor.setDishId(dishDto.getId());});
        //批量保存
        dishFlavorService.saveBatch(flavors);
    }

    //扩展方法：分页查询菜品数据，包括分类名称
    @Override
    public Page<DishDto> pageDishDto(Integer page, Integer pageSize, String name) {
        //创建分页对象
        Page<DishDto> pageInfo = new Page<>(page,pageSize);
        //利用mapper分页查询。
        return dishMapper.pageDishDto(pageInfo,name);
    }

    //扩展方法：批量删除菜品，以及菜品口味数据，如果有菜品状态为启售，则不做操作
    @Override
    public void deleteByIds(Long[] ids) {
        Integer count = dishService.lambdaQuery().
                in(Dish::getId, ids).
                eq(Dish::getStatus, 1).
                count();
        if (count>1){
            throw new CustomerException("有菜品正在售卖，不能删除。");
        }
        //删除菜品信息。
        //发现直接传ids不可以，于是选择转为arraylist
        // this.removeByIds(Arrays.asList(ids));
        dishService.removeByIds(Arrays.asList(ids));
        //删除口味的信息。
        dishFlavorService.lambdaUpdate().
                in(DishFlavor::getDishId,ids).
                remove();
    }

    @Override
    public DishDto getDishByIdWithFlavor(Long id) {
        DishDto dishDto = dishMapper.getDishByIdWithFlavor(id);
        return dishDto;
    }

    @Override
    public void updateByGiven(DishDto dishDto) {
        //1.修改dish表的参数
        dishService.updateById(dishDto);
        //2.修改dishflavor的数据（先删再更新）
        //delete from dish_flavor where dish_id = ?
        dishFlavorService.lambdaUpdate()
                .eq(DishFlavor::getDishId,dishDto.getId())
                .remove();
        //然后把新的数据添加进去。
        List<DishFlavor> flavors = dishDto.getFlavors();
        //TODO 给每一个属性设置菜品的id
        flavors.forEach(flavor->{flavor.setDishId(dishDto.getId());});
        //批量保存
        dishFlavorService.saveBatch(flavors);
    }
}







