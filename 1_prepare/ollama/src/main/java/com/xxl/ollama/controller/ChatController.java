package com.xxl.ollama.controller;

import com.xxl.ollama.service.OllamaAiInvoke;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OllamaAiInvoke ollamaAiInvoke;

    /**
     * 问答接口
     *
     * @param input 提示词
     * @return 返回结果
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "input") String input) {
        return ollamaAiInvoke.call(input);
    }

}
