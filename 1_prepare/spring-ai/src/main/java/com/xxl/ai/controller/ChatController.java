package com.xxl.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Classname ChatController
 * @Description TODO
 * @Date 2025/4/4 23:38
 * @Created by xxl
 */
@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * 问答接口
     *
     * @param input 提示词
     * @return 返回结果
     */
    @GetMapping("/chat")
    public ChatResponse chat(@RequestParam(value = "input") String input) {
       return this.chatClient.prompt()
                .user(input)
                .call()
                .chatResponse();
    }
}
