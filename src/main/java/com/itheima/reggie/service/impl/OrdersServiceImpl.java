package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.mapper.OrdersMapper;
import com.itheima.reggie.pojo.*;
import com.itheima.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrdersMapper ordersMapper;


    @Override
    public void submitOrders(Orders orders) {
        //保存到订单表里面
        //TODO 补全order表里的数据
        //从当前线程中获取用户id
        Long userId = BaseContext.getCurrentId();
        //查询用户的购物车：
        List<ShoppingCart> shoppingCartList = shoppingCartService.lambdaQuery().eq(ShoppingCart::getUserId, userId).list();
        //查询用户信息 User
        User user = userService.getById(userId);
        //查询地址的信息：
        AddressBook addressBook = addressBookService.lambdaQuery().eq(AddressBook::getId, orders.getAddressBookId()).one();
        //自己搞出来id自己维护（雪花算法生成）
        long orderId = IdWorker.getId();
        //订单的总金额
        BigDecimal total = new BigDecimal(0);

        //1. 添加订单详情数据，操作是order_detail表（多条数据）        由购物车的数据决定
        // TODO 补全List集合中的数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCartList) {
            //创建一个OrderdDetail
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setDishFlavor(cart.getDishFlavor());
            orderDetail.setDishId(cart.getDishId());
            orderDetail.setSetmealId(cart.getSetmealId());
            orderDetail.setName(cart.getName());
            orderDetail.setImage(cart.getImage());
            orderDetail.setAmount(cart.getAmount());
            orderDetailList.add(orderDetail);
            //计算总的金额
            BigDecimal multiply = (new BigDecimal(String.valueOf(orderDetail.getAmount()))).multiply(new BigDecimal(orderDetail.getNumber()));
            total = total.add(multiply);
        }
        orderDetailService.saveBatch(orderDetailList);

        orders.setId(orderId);//订单的id（自己生成的）
        orders.setNumber(String.valueOf(orderId));//订单编号
        orders.setStatus(2);//状态 订单状态 1待付款，2待派送，3已派送，4已完成，5已取消
        orders.setUserId(userId);//用户id
        orders.setAddressBookId(orders.getAddressBookId());//关联的地址簿的编号
        orders.setOrderTime(LocalDateTime.now());//下单时间
        orders.setCheckoutTime(LocalDateTime.now());//支付时间
        orders.setAmount(total);// 总金额
        //orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());//收获人
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        this.save(orders);

        //清理购物车数据
        shoppingCartService.lambdaUpdate().eq(ShoppingCart::getUserId, userId).remove();
    }
}
