package com.xxl.ai.testApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author xxl
 * @date 2025/7/11 18:05
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    void doChatWithReport2() {
        Object stock = redisTemplate.opsForValue().get("stock");
        System.out.println(stock);
    }

}
