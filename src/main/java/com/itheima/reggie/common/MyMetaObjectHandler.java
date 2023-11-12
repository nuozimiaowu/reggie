package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component //放到容器里面。
public class MyMetaObjectHandler implements MetaObjectHandler {
        @Override
        public void insertFill(MetaObject metaObject) {
            //对metaObject的属性赋值
            metaObject.setValue("createTime", LocalDateTime.now());
            metaObject.setValue("updateTime",LocalDateTime.now());
            //拿不到session应该怎么办哪：利用jdk里面的ThreadLocal解决
            //浏览器每一个请求都会建立一个新的线程，线程的执行流程为：拦截器，controller，Service，自动填充类，mapper
            //我们在拦截器里面存入一个数据（类似于用户id，然后在自动填充类里面调用jdk里面的ThreadLocal获取。）
            //但是我们不能new  ThreadLocal，这样子就是新的线程，所以说要提供一个工具类，用来操作线程
            //ThreadLocal<Long> threadLocal = new ThreadLocal<>();  非常的错误！！！
            metaObject.setValue("createUser",BaseContext.getCurrentId());
            metaObject.setValue("updateUser",BaseContext.getCurrentId());

        }
    //添加的时候自动执行
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());

        //对metaObject的属性赋值
    }
}
