package com.xxl.arms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 *
 * @Author: xxl
 * @Date: 2025/1/4 16:20
 */
@ComponentScan(value = {"com.xxl"})
@MapperScan("com.xxl.arms.mapper*")
@SpringBootApplication
@EnableTransactionManagement
public class XxlArmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(XxlArmsApplication.class, args);
    }
}