package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomerException;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.pojo.Setmeal;
import com.itheima.reggie.pojo.SetmealDish;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealService setmealService;

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public void saveSetMeal(SetmealDto setmealDto) {
        //1.操作setmeal表，添加一条数据
        //先保存到套餐表里以后，雪花算fa自动生产id，id就可以和套餐的setmeal_dish链接
        this.save(setmealDto);
        //2.添加套餐菜品的关联数据，操作setmeal_dish表，添加多条数据。
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.forEach(o->{o.setSetmealId(setmealDto.getId());});

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public Page<SetmealDto> pageSetmeal(Integer page, Integer pageSize, String name) {
        Page<SetmealDto> page2 = new Page<>(page,pageSize);
        return setmealMapper.pageSetmeal(page2,name);
    }

    @Override
    public void deleteByIds(Long[] ids) {
        //需求1：如果ids里面有商品正在售卖的话，那么就抛出异常。
        //select count(*) from  setmeal where id in ids and setmeal.status=1;
        Integer count = setmealService.lambdaQuery().in(Setmeal::getId, ids).eq(Setmeal::getStatus, 1).count();
        if(count>=1){
            throw new CustomerException("批量删除的套餐中，存在套餐正在售卖，请先停售，然后删除");
        }
        //现在，可以开始删除嘞！
        setmealService.removeByIds(Arrays.asList(ids));

        //你第一次的时候，用了这种方法，但是ids在setmealDishService里面并不是主键，所以这种方法是不对的。
        //setmealDishService.removeByIds(Arrays.asList(ids));
        setmealDishService.lambdaUpdate().
                in(SetmealDish::getSetmealId,ids).
                remove();
    }

    @Override
    public void changeStatus(Integer status, Long[] ids) {
        setmealService.lambdaUpdate().set(Setmeal::getStatus,status).
                in(Setmeal::getId,ids).update();
    }

    @Override
    public SetmealDto getStealWithDish(Long id) {
        return setmealMapper.getStealWithDish(id);
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        setmealService.updateById(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishService.lambdaUpdate().
                eq(SetmealDish::getSetmealId,setmealDto.getId()).
                remove();
        //TODO 给每一个SetDish设置套餐的id
        setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealDto.getId()));
        //批量保存
        setmealDishService.saveBatch(setmealDishes);
    }
}
