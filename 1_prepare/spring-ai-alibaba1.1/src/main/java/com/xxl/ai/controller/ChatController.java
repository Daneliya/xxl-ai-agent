package com.xxl.ai.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.xxl.ai.framework.interceptor.ToolPerformanceInterceptor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
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
    @GetMapping("/test")
    public String test(@RequestParam(value = "input") String input) {
        return input;
    }

    /**
     * 问答接口
     *
     * @param input 提示词
     * @return 返回结果
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "input") String input) throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("monitored_agent")
                .model(chatModel)
//                .tools(tools)
//                .interceptors(new ModelPerformanceInterceptor())
                .interceptors(new ToolPerformanceInterceptor())
                .build();
        // 运行 Agent
        AssistantMessage message = agent.call("坏掉的东西，还能修好。可是坏掉的感情，岂可修吗？");
        System.out.println(message.getText());
        return message.getText();
    }
}
