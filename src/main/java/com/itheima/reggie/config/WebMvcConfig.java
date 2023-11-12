package com.itheima.reggie.config;

import com.itheima.reggie.common.JacksonObjectMapper;
import com.itheima.reggie.common.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

//表示这个类是一个配置类。配置拦截器的拦截对象
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).
                addPathPatterns("/**")//拦截的路径
                //排除不需要拦截的静态资源和登录请求
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/sendMsg")
                .excludePathPatterns("/employee/login")
                .excludePathPatterns("/backend/**")
                .excludePathPatterns("/front/**");
    }

    //扩展mvc的消息转换器。
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //1.创建一个消息转换器对象 MappingJackson2HttpMessageConverter
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //TODO 核心操作：设置核心对象（如果没有设置，那么默认是ObjectMapper）
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //2.把消息转换器对象添加到集合中。
        converters.add(0, messageConverter);//第一个元素，优先级变得最高。
    }
}
