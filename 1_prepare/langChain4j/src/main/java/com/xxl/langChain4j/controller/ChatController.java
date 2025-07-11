package com.xxl.langChain4j.controller;

import com.xxl.langChain4j.properties.AgentProperties;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
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
    private AgentProperties agentProperties;

    /**
     * 问答接口
     *
     * @param input 提示词
     * @return 返回结果
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "input") String input) {
        ChatLanguageModel qwenModel = QwenChatModel.builder()
                .apiKey(agentProperties.getLangchain4j())
                .modelName("qwen-max")
                .build();
        String answer = qwenModel.chat(input);
        System.out.println(answer);
        // http://localhost:8080/chat?input=你是谁
        // 我是Qwen，由阿里云开发的超大规模语言模型。我的目标是成为一款能够理解并生成高质量文本的AI助手，帮助用户提高创造力和生产力。无论是写作、编写代码、回答问题还是其他需要处理文字的任务，我都会尽力提供支持。您有什么具体的问题或任务需要我帮忙吗？
        return answer;
    }
}
