package com.xxl.ai.framework.example;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import lombok.SneakyThrows;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Models 模型
 *
 * @Author xxl
 * @Date 2025/12/5 15:02
 */
public class Example06_Models {

    public static void main(String[] args) {
//        dashScopeChatOptionsConfiguration();
//        runtimeOptionsConfiguration();
//        streamConfiguration();
//        messagesConfiguration();
        functionConfiguration();
    }

    /**
     * 基础模型配置示例
     */
    public static void basicModelConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
    }

    /**
     * 简单调用示例
     */
    public static void simpleCellConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 使用字符串直接调用
        String response = chatModel.call("介绍一下Spring框架");
        System.out.println(response);
    }

    /**
     * 使用 Prompt 调用示例
     */
    public static void promptConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();

        // 创建 Prompt
        Prompt prompt = new Prompt(new UserMessage("解释什么是微服务架构"));

        // 调用并获取响应
        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * DashScopeChatOptions 配置示例
     */
    public static void dashScopeChatOptionsConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel("deepseek-v3.2")           // 模型名称
                .withTemperature(0.7)                 // 温度参数
                .withMaxToken(2000)         // 最大令牌数
                .withTopP(0.9)                        // Top-P 采样
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();

        // 创建 Prompt
        Prompt prompt = new Prompt(new UserMessage("解释什么是微服务架构"));

        // 调用并获取响应
        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * 运行时覆盖选项示例
     */
    public static void runtimeOptionsConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        // 模型配置
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel("deepseek-v3.2")           // 模型名称
                .withTemperature(0.7)                 // 温度参数
                .withMaxToken(2000)         // 最大令牌数
                .withTopP(0.9)                        // Top-P 采样
                .build();

        // 模型运行时配置（覆盖主配置）
        DashScopeChatOptions runtimeOptions = DashScopeChatOptions.builder()
                .withModel("deepseek-v3.2")           // 模型名称
                .withTemperature(0.3)                 // 温度参数
                .withMaxToken(500)          // 最大令牌数
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();

        // 创建 Prompt
        Prompt prompt = new Prompt(new UserMessage("用一句话总结Java的特点"), runtimeOptions);

        // 调用并获取响应
        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * 流式响应示例
     */
    public static void streamConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel("deepseek-v3.2")           // 模型名称
                .withTemperature(0.3)                 // 温度参数
                .withMaxToken(500)          // 最大令牌数
                .withTopP(0.9)                        // Top-P 采样
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();

        // 创建 Prompt
        Prompt prompt = new Prompt("详细解释Spring Boot的自动配置原理");

        // 使用流式 API
        Flux<ChatResponse> responseStream = chatModel.stream(prompt);

        // 订阅并处理流式响应
        responseStream.subscribe(
                chatResponse -> {
                    String content = chatResponse.getResult()
                            .getOutput()
                            .getText();
                    System.out.print(content);
                },
                error -> System.err.println("错误: " + error.getMessage()),
                () -> System.out.println("流式响应完成")
        );
    }

    /**
     * 多轮对话示例
     */
    public static void messagesConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel("deepseek-v3.2")           // 模型名称
                .withTemperature(0.3)                 // 温度参数
                .withMaxToken(500)          // 最大令牌数
                .withTopP(0.9)                        // Top-P 采样
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();

        // 创建对话历史
        List<Message> messages = List.of(
                new SystemMessage("你是一个Java专家"),
                new UserMessage("什么是Spring Boot?"),
                new AssistantMessage("Spring Boot是..."),
                new UserMessage("它有什么优势?")
        );

        Prompt prompt = new Prompt(messages);
        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * 函数调用示例
     */
    public static void functionConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        // 定义函数工具
        ToolCallback weatherFunction = FunctionToolCallback.builder("getWeather", (String city) -> {
                    // 实际的天气查询逻辑
                    return "晴朗，25°C";
                })
                .description("获取指定城市的天气")
                .inputType(String.class)
                .build();

        // 使用函数
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel("deepseek-v3.2")           // 模型名称
                .withTemperature(0.3)                 // 温度参数
                .withMaxToken(500)          // 最大令牌数
                .withTopP(0.9)                        // Top-P 采样
                .withToolCallbacks(List.of(weatherFunction))
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();

        Prompt prompt = new Prompt("北京的天气怎么样?", options);
        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * 与 ReactAgent 集成示例
     */
    @SneakyThrows
    public static void reactAgentConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        // 使用函数
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel("deepseek-v3.2")           // 模型名称
                .withTemperature(0.3)                 // 温度参数
                .withMaxToken(500)          // 最大令牌数
                .withTopP(0.9)                        // Top-P 采样
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();

        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .systemPrompt("你是一个有帮助的AI助手")
                .build();

        // 调用 Agent
        AssistantMessage response = agent.call("帮我分析这个问题");
        System.out.println(response.getText());
    }
}
