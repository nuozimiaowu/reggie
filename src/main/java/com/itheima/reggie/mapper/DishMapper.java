package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    //自定义方法：分页功能
    Page<DishDto> pageDishDto(Page<DishDto> pageInfo, String name);

    //自定义方法：根据id查询菜品详情信息（包含口味数据）
    DishDto getDishByIdWithFlavor(Long id);

    List<DishDto> listDishDto(@Param("categoryId")Long categoryId,
                              @Param("name") String name);
}
