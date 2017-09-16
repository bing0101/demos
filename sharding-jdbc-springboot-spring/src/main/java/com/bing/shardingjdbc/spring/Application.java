package com.bing.shardingjdbc.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Administrator on 2017/9/15.
 */
@ImportResource(locations = "classpath:spring/*.xml")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) // 排除DataSource自动注入（因配置sharding-jdbc时会有多个数据源，自动注入会冲突）
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
