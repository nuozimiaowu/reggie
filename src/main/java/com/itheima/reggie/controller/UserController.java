package com.itheima.reggie.controller;

import com.alibaba.druid.util.StringUtils;
import com.itheima.reggie.common.R;
import com.itheima.reggie.mapper.UserMapper;
import com.itheima.reggie.pojo.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.SmsUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 移动端用户管理
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @PostMapping("sendMsg")
    //为了下一步的验证，必须把验证码存起来。
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //先获取手机号
        String phone = user.getPhone();
        //再重新生成一个随机的二维码
        String code = RandomStringUtils.random(4, false, true);

        //session.setAttribute("code",random);
        // 这个方法我们会把验证码存储在session里面，现在我们用redis来进行这个操作
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
        //发送短信
        SmsUtil.sendSms(phone,code);
        return R.success("发送成功");
    }

    @PostMapping("login")
    public R login(@RequestBody Map map, HttpSession session){
        String phone = (String)map.get("phone");
        //1.比较验证码是否正确,从redis的里面获取
        String code1 = (String)map.get("code");
        String code2 = (String)redisTemplate.opsForValue().get(phone);
        //登录失败（验证码错误）
        if(! StringUtils.equals(code1,code2)){return R.error("登录失败");}
        //如果验证码正确，就在数据库查询这个phone关联的user
        User user = userService.lambdaQuery().
                eq(User::getPhone, phone).one();
        if(user == null){
            //如果没有找到这个phone关联的user，就说明是新的用户，我们决定创建它。
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            //创建并且保存
            userService.save(user);
        }
        session.setAttribute("user",user.getId());
        //及时删除redis里的验证码
        redisTemplate.delete(phone);
        return R.success(user);
    }

}
