package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.pojo.Orders;

public interface OrdersService extends IService<Orders> {

    void submitOrders(Orders orders);
}
