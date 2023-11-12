package com.itheima.reggie.controller;

import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车管理
 */
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("list")
    public R<List<ShoppingCart>> list(){
        Long currentId = BaseContext.getCurrentId();
        List<ShoppingCart> list = shoppingCartService.
                lambdaQuery().eq(ShoppingCart::getUserId, currentId).
                list();
        return R.success(list);
    }

    @PostMapping("add")
    public R add(@RequestBody ShoppingCart shoppingCart){
        ShoppingCart one = shoppingCartService.lambdaQuery().
                eq(ShoppingCart::getUserId, BaseContext.getCurrentId()).
                eq(ShoppingCart::getDishId, shoppingCart.getDishId()).
                one();
        if (one!=null){
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
            return R.success(one);
        }
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);
        shoppingCart.setNumber(1);
        shoppingCartService.save(shoppingCart);
        return R.success(shoppingCart);
    }


}
