package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.pojo.Category;

public interface CategoryService extends IService<Category> {

    //功能扩展,根据id删除分类：
    void deleteById(Long id);
}
