package com.itheima.reggie;

import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.DeleteMapping;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
public class ReggieApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);

        log.info("项目启动成功");
        log.info("后台地址：http://localhost:8080/backend/page/login/login.html");
        log.info("前端地址：http://localhost:8080/front/index.html");
    }
}
