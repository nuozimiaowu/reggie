package com.itheima.reggie.controller;

import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.Orders;
import com.itheima.reggie.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单管理
 */
@RestController
@RequestMapping("order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("submit")
    public R submitOrder(@RequestBody Orders orders){
        ordersService.submitOrders(orders);
        return R.success("下单成功");
    }


}
