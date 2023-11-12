package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.pojo.Dish;
import com.itheima.reggie.service.DishService;
import org.apache.commons.codec.cli.Digest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.OutputKeys;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("dish")
//@EnableTransactionManagement 开启注解必须要在启动类上加这个，否则会报错。
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    //添加菜品,但是提交的有口味这个属性，Dish里面没有提供，所以需要使用DishDto。
    @PostMapping
    public R save(@RequestBody DishDto dishDto){
        //添加菜品的时候要注意清理缓存，就是删掉redis
        String redisKey = "dish_" + dishDto.getCategoryId() +"_null";
        //对于key加密，key的风格会统一
        redisKey = "dish_" + DigestUtils.md5DigestAsHex(redisKey.getBytes());
        redisTemplate.delete(redisKey);
        redisTemplate.delete("redisKey");
        dishService.saveDishFlavorWith(dishDto);
        return R.success("菜品添加成功");
    }
    //分页查询：
    @GetMapping("page")
    public R<Page<DishDto>> page(Integer page, Integer pageSize,String name){
        //传统的分页查询无法满足数据的展示，因为要分类的名称
        Page<DishDto> pageInfo = dishService.pageDishDto(page,pageSize,name);
        return R.success(pageInfo);
    }
    /**
     * 批量删除
     */
    @DeleteMapping
    public R<String> deleteByIds(Long[] ids) {
        //批量删除，需要自己定义业务逻辑（菜品状态为启售的不能删除）\

        //我们索性把我们知道的所有的缓存全都删掉了！！
        Set<Object> keys = redisTemplate.keys("dish_*");
        redisTemplate.delete(keys);

        dishService.deleteByIds(ids);
        return R.success("删除成功");
    }
    /**
     * 启用，停用
     */
    @PostMapping("status/{status}")
    public R<String> getById(@PathVariable Integer status,Long[] ids){
        //批量修改状态。
        //update dish set status? where id in (ids)
        dishService.lambdaUpdate().
                set(Dish::getStatus,status).   //修改值
                in(Dish::getId, ids). //条件部分
                update();                      //更新操作
        return R.success("修改成功");
    }
    /**
     * 根据id查询菜品详情信息（包含多个口味）,其实就是为了回显数据。
     * 确定：返回一个对象 DishDto = Dish  +  List<DishFlavor>
     */
    @GetMapping("id/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getDishByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping()
    public R<String> updateByGiven(@PathVariable DishDto dishDto){
        //批量删除，需要自己定义业务逻辑（菜品状态为启售的不能删除）
        dishService.updateByGiven(dishDto);
        return R.success("修改成功");
    }

    //根据菜品的的菜品种类，名字来查询菜品和相关的口味信息。
    @GetMapping("list")
    public R<List<DishDto>> list(Long categoryId,String name){

        String redisKey = "dish_" + categoryId + "_" + name;
        //对于key加密，key的风格会统一
        redisKey = "dish_" + DigestUtils.md5DigestAsHex(redisKey.getBytes());

        //redis的数据类型采用string
        List<DishDto> redisData = (List<DishDto>)redisTemplate.opsForValue().get(redisKey);
        if(redisData != null){
            //如果有数据的话，就从redis里提取数据。
            return R.success(redisData);
        }

        List<DishDto> dishDtoList = dishService.listDishDto(categoryId,name);
        //如果没有数据的话，就把数据库里的数据更新到缓存里面
        redisTemplate.opsForValue().set(redisKey,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
}











