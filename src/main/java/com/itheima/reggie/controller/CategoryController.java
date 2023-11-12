package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.Category;
import com.itheima.reggie.pojo.ShoppingCart;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody Category category){
        //直接调用业务层
        categoryService.save(category);
        return R.success("添加成功");
    }

    @GetMapping("page")
    public R<Page<Category>> page(Integer page,Integer pageSize){
        //分页对象
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //直接用业务层查询数据
        categoryService.lambdaQuery().
                orderByAsc(Category::getUpdateTime).
                orderByDesc(Category::getUpdateTime).
                page(pageInfo);
        return R.success(pageInfo);
    }

    //根据id删除
    @DeleteMapping
    public R<String> deleteById(Long id){
        categoryService.deleteById(id);
        return R.success("删除成功");
    }
    //修改操作：
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    //添加菜品时候的下拉条
    @GetMapping("list")
    public R list(Integer type)
    {
        List<Category> list = categoryService.lambdaQuery().eq(type != null, Category::getType, type).list();
        return R.success(list);
    }

}