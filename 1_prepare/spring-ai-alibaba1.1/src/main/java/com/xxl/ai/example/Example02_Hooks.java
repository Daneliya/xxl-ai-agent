package com.xxl.ai.example;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.xxl.ai.interceptor.ModelPerformanceInterceptor;
import com.xxl.ai.interceptor.ToolPerformanceInterceptor;
import com.xxl.ai.tool.SearchTool;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;

/**
 * Hooks 和 Interceptors
 *
 * @Author xxl
 * @Date 2025/12/1 14:50
 */
public class Example02_Hooks {

    public static void main(String[] args) throws GraphRunnerException {
        // 性能监控
        hookPerformanceInterceptors();
    }

    /**
     * 性能监控
     *
     * @throws GraphRunnerException
     */
    public static void hookPerformanceInterceptors() throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 创建工具回调
        ToolCallback searchTool = FunctionToolCallback.builder("search", new SearchTool())
                .description("搜索工具")
                .inputType(String.class)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("monitored_agent")
                .model(chatModel)
                .tools(searchTool)
                .interceptors(new ModelPerformanceInterceptor())
                .interceptors(new ToolPerformanceInterceptor())
                .build();
        // 运行 Agent
        AssistantMessage message = agent.call("坏掉的东西，还能修好。可是坏掉的感情，岂可修吗？");
        System.out.println(message.getText());
    }
}
