package com.xxl.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动类
 */
@ComponentScan("com.xxl")
@SpringBootApplication
public class StartAiAlibabaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartAiAlibabaApplication.class, args);
    }

}
