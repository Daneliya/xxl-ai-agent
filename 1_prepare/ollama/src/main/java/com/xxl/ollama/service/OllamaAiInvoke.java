package com.xxl.ollama.service;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;

/**
 * @Author: xxl
 * @Date: 2025/7/11 14:23
 */
public class OllamaAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel ollamaChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage output = ollamaChatModel.call(new Prompt("你好，我是小龙"))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
    }

    /**
     * 问答接口
     *
     * @param input 提示词
     * @return 返回结果
     */
    public String call(String input) {
        AssistantMessage output = ollamaChatModel.call(new Prompt(input))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
        return output.getText();
    }
}
