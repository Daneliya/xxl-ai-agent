package com.xxl.ai.chatmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基于Redis的对话记忆
 *
 * @Author: xxl
 * @Date: 2025/7/11 16:46
 */
@Service
@Slf4j
public class RedisChatMemory implements ChatMemory {

    private static final String REDIS_KEY_PREFIX = "chat:history:";

    private RedisTemplate<String, Object> redisTemplate;

    public RedisChatMemory(RedisTemplate<String, Object> objectRedisTemplate) {
        this.redisTemplate = objectRedisTemplate;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        String key = REDIS_KEY_PREFIX + conversationId;
        // 存储到 Redis
        redisTemplate.opsForList().rightPushAll(key, messages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        String key = REDIS_KEY_PREFIX + conversationId;
        // 从 Redis 获取最新的 lastN 条消息
        List<Message> serializedMessages = (List) redisTemplate.opsForList().range(key, -lastN, -1);
        if (serializedMessages != null) {
            return serializedMessages;
        }
        return List.of();
    }

    @Override
    public void clear(String conversationId) {
        redisTemplate.delete(REDIS_KEY_PREFIX + conversationId);
    }

}
