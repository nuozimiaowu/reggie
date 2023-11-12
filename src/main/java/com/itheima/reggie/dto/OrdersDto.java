package com.itheima.reggie.dto;

import com.itheima.reggie.pojo.OrderDetail;
import com.itheima.reggie.pojo.Orders;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单Dto
 */
@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> orderDetails = new ArrayList<>(); //订单详情集合
}
