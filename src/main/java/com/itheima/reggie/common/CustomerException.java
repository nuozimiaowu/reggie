package com.itheima.reggie.common;

public class CustomerException extends RuntimeException{
    //自定义一个异常，在删除关联的菜品类的时候，抛出这个异常

    public CustomerException() {
    }

    public CustomerException(String msg) {
        //把错误信息传给父类
        super(msg);
    }
}
