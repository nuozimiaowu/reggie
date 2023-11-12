package com.itheima.reggie.common;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//是一个springmvc提供的拦截器
@Component//放进容器
public class LoginInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Long employee = (Long)request.getSession().getAttribute("employee");
        Long user = (Long)request.getSession().getAttribute("user");
        if(employee == null && user == null){
            //给前端返回未登录的提示，拒绝本次访问，实现页面的跳转。（给前端传过去错误信息：code：0  msg=NOTLOGIN）
            String notlogin = JSON.toJSONString(R.error("NOTLOGIN"));
            response.getWriter().write(notlogin);
            return  false;
        }
        //如果在员工登录成功的情况下，需要把员工的id存入本地线程ThreadLocal里。
/*      创建对象会导致出现多个线程，所以说要用我们自己的线程类！
        ThreadLocal<Long> objectThreadLocal = new ThreadLocal<>();
        objectThreadLocal.set(employee);*/
        BaseContext.setCurrentId(employee);
        return true;
    }
}
