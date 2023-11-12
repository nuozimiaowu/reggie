package com.itheima.reggie.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

//全局异常处理器（添加注解就@RestControllerAdvice）
@RestControllerAdvice
public class GlobalExceptionHandler {
    //每一个方法解决一种特定的异
    @ExceptionHandler(CustomerException.class)
    public R exeception2(CustomerException customerException){
        //把错误的信息输出到控制台
        customerException.printStackTrace();
        //抓住自定义的异常，需要响应对应的信息给前端
        return R.error(customerException.getMessage());
    }
    //把为自己定义的类抓起来
    @ExceptionHandler(SQLException.class)
    public R exception1(SQLException e){
        //输出异常信息
        e.printStackTrace();
        //判断sql的异常属于什么类型；
        if(e.getMessage().startsWith("Duplicate entry")){
            //对错误信息截取
            String username = e.getMessage().split(" ")[2];
            return R.error(username+"用户名已经存在");
        }

        if(e.getMessage().contains("Data too long")){
            String s = e.getMessage().split(" ")[7];
            return R.error(s+"数据过长");
        }
        return R.error("未知错误");
    }
}
