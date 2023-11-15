package com.itheima.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.Employee;
import com.itheima.reggie.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 员工管理模块
 */
@RestController
@RequestMapping("employee")
@Api(tags = "员工模块")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("login")
    @ApiOperation("员工登录")
    public R<Employee> login(@RequestBody Employee employee, HttpSession session){
        Employee employee1 = employeeService.
                query().
                eq("username", employee.getUsername()).
                one();//西二402

        if(employee1 == null){
            //说明账号不存在
            return R.error("账号不存在");
        }

        //判断密码是否相同。
        if(!StringUtils.equals(employee1.getPassword(),
                DigestUtils.md5Hex(employee.getPassword()))){
            return R.error("密码错误");
        }

        if(employee1.getStatus() == 0){
            return R.error("账号禁用");
        }
        //说明登录成功，一定要把用户存入session中去。
        session.setAttribute("employee",employee1.getId());
        return R.success(employee1);
    }

    @PostMapping("logout")
    public R<String> logout(HttpSession session){
        //删除session里的用户信息
        session.invalidate();
        return R.success("退出成功");
    }

    //添加成员，注意密码的保护
    @PostMapping
    public R save(@RequestBody Employee employee, HttpSession httpSession){
/*        employee.setCreateUser((long)httpSession.getAttribute("employee"));
        employee.setUpdateUser((long)httpSession.getAttribute("employee"));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/
        employee.setPassword(DigestUtils.md5Hex("123456".getBytes(StandardCharsets.UTF_8)));
        //controller添加
        employeeService.save(employee);
        //改和删除通常这样子返回
        return R.success("添加成功");
    }

    //分页查询
    @GetMapping("page")
    @ApiOperation("分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "页面大小"),
            @ApiImplicitParam(name = "name",value = "员工姓名")
    })
    public R page(Integer page,Integer pageSize,String name){
        //创建一个分页对象page
        Page<Employee> pageInfo = new Page<>(page,pageSize);

        //分页查询select * from employee where name like ? limit?,?
        //流式编程。
        /*Query 的列名匹配使用的是 “数据库中的字段名(一般是下划线规则)”
         LambdaQuery 的列名匹配使用的是“Lambda的语法，偏向于对象”*/
        employeeService.lambdaQuery()
                .like(org.apache.commons.lang.StringUtils.isNotBlank(name),Employee::getName,name)
                .orderByDesc(Employee::getUpdateTime)
                .page(pageInfo);

        //返回对象
        return R.success(pageInfo);
    }

    //状态的启用和禁用
    //js处理long类型的时候只能处理前16位导致修改不成功，显示修改成功（因为R返回值是1）。但是数据库查找不到前端返回的信息，数据库没有更改，其实并没有修改成功/
    //解决方案1： @JsonSerialize(using = ToStringSerializer.class)  pojo/employee中
    //          +转json数据的时候，把属性的类型转换为string类型

    //解决方法2：扩展mvc的消息转化器。在WebMvcConfig里
    @PutMapping()
    public R<String> update(@RequestBody Employee employee,HttpSession session){

        //补全修改的信息
/*        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((long)session.getAttribute("employee"));*/

        //update employee set status=?,update_time=?,update_user=? where id=?
        employeeService.updateById(employee);

        return R.success("修改成功");
    }

    //实现消息的回显，根据id查询员工信息
    @GetMapping("{id}")
    public R<Employee> selectById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}




