package com.itheima.reggie.common;

//本地线程localThread的工具类。
public class BaseContext {
    //私有构造方法防止别人创建。
    private BaseContext(){};
    //定义一个本地线程对象
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    //提供静态方法，设置数据
    public static void setCurrentId(Long employeeId) {
        THREAD_LOCAL.set(employeeId);
    }

    //提供静态方法，获取数据
    public static Long getCurrentId() {
        return THREAD_LOCAL.get();
    }


    //提供静态方法，移除数据
    public static void removeCurrentId() {
        THREAD_LOCAL.remove();
    }

}
