package com.xxl.arms.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mybatisflex.core.audit.AuditManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Classname MyBatisFlexConfig
 * @Description TODO
 * @Date 2026/1/5 00:20
 * @Created by xxl
 */
@Configuration
public class MyBatisFlexConfig {

    private static final Logger logger = LoggerFactory.getLogger("mybatis-flex-sql");

    public MyBatisFlexConfig() {
        // 开启审计功能
        AuditManager.setAuditEnable(true);

        // 打印SQL字符串
        // 设置SQL审计收集器
        AuditManager.setMessageCollector(auditMessage -> {
//                    logger.info("【执行SQL】耗时: {}ms, SQL语句:\n{}", auditMessage.getElapsedTime(), auditMessage.getFullSql());
                    logger.info("【执行SQL】耗时: {}ms, SQL语句: {}", auditMessage.getElapsedTime(), auditMessage.getFullSql());
                }
        );
    }

    /**
     * 配置RedisTemplate
     * 使用JSON序列化器，支持对象存储到Redis
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
