package com.xxl.ai.framework.example;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import lombok.SneakyThrows;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Message 消息
 *
 * @Author xxl
 * @Date 2025/12/5 15:02
 */
public class Example05_Message {

    public static void main(String[] args) {
        chatModelBuildMessageConfiguration();
    }

    /**
     * 基础消息使用示例
     */
    public static void basicMessageConfiguration() {
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

        // 创建消息对象
        SystemMessage systemMsg = new SystemMessage("你是一个有帮助的助手。");
        UserMessage userMsg = new UserMessage("你好，你好吗？");

        // 与聊天模型一起使用
        List<org.springframework.ai.chat.messages.Message> messages = List.of(systemMsg, userMsg);
        Prompt prompt = new Prompt(messages);
        ChatResponse response = chatModel.call(prompt);  // 返回 ChatResponse，包含 AssistantMessage
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * 文本提示示例
     */
    public static void textConfiguration() {
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

        // 使用字符串直接调用
        String response = chatModel.call("写一首关于春天的俳句");
        System.out.println(response);
    }

    /**
     * 消息提示示例
     */
    public static void messageConfiguration() {
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

        // 创建消息对象
        List<org.springframework.ai.chat.messages.Message> messages = List.of(
                new SystemMessage("你是一个诗歌专家"),
                new UserMessage("写一首关于春天的俳句"),
                new AssistantMessage("樱花盛开时...")
        );
        Prompt prompt = new Prompt(messages);
        ChatResponse response = chatModel.call(prompt);
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * SystemMessage 基础指令示例
     */
    public static void systemMessageConfiguration() {
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

        // 基础指令
        SystemMessage systemMsg = new SystemMessage("你是一个有帮助的编程助手。");

        List<org.springframework.ai.chat.messages.Message> messages = List.of(
                systemMsg,
                new UserMessage("如何创建 REST API？")
        );
        // 调用并获取响应
        ChatResponse response = chatModel.call(new Prompt(messages));
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * SystemMessage 详细角色设定示例
     */
    public static void systemDetailMessageConfiguration() {
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

        // 详细的角色设定
        SystemMessage systemMsg = new SystemMessage("""
                你是一位资深的 Java 开发者，擅长 Web 框架。
                始终提供代码示例并解释你的推理。
                在解释中要简洁但透彻。
                """);

        List<org.springframework.ai.chat.messages.Message> messages = List.of(
                systemMsg,
                new UserMessage("如何创建 REST API？")
        );
        // 调用并获取响应
        ChatResponse response = chatModel.call(new Prompt(messages));
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * UserMessage 文本内容示例
     */
    @SneakyThrows
    public static void userMessageTextConfiguration() {
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

        // 调用并获取响应
        // 使用消息对象
        ChatResponse response = chatModel.call(
                new Prompt(List.of(new UserMessage("什么是机器学习？")))
        );
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
        // 使用字符串快捷方式
        // 使用字符串是单个 UserMessage 的快捷方式
        String response2 = chatModel.call("什么是机器学习？");
        System.out.println(response2);
    }

    /**
     * UserMessage 消息元数据示例
     */
    @SneakyThrows
    public static void userMessageMateConfiguration() {
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

        // 调用并获取响应
        UserMessage userMsg = UserMessage.builder()
                .text("你好！")
                .metadata(Map.of(
                        "user_id", "alice",  // 可选：识别不同用户
                        "session_id", "sess_123"  // 可选：会话标识符
                ))
                .build();
        ChatResponse response = chatModel.call(new Prompt(userMsg));
        String answer0 = response.getResult().getOutput().getText();
        System.out.println(answer0);
    }

    /**
     * UserMessage 多模态内容示例
     */
    @SneakyThrows
    public static void userMessageUrlConfiguration() {
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

        // 调用并获取响应
        // 从 URL 创建图像
        UserMessage userMsg = UserMessage.builder()
                .text("描述这张图片的内容。")
                .media(Media.builder()
                        .mimeType(MimeTypeUtils.IMAGE_JPEG)
                        .data(new URL("https://example.com/image.jpg"))
                        .build())
                .build();
        ChatResponse response = chatModel.call(new Prompt(userMsg));
        String answer0 = response.getResult().getOutput().getText();
        System.out.println(answer0);
    }

    /**
     * AssistantMessage 基础使用示例
     */
    @SneakyThrows
    public static void assistantMessageBasicConfiguration() {
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

        ChatResponse response = chatModel.call(new Prompt("解释 AI"));
        AssistantMessage aiMessage = response.getResult().getOutput();
        System.out.println(aiMessage.getText());
    }

    /**
     * 手动创建 AssistantMessage 示例
     */
    @SneakyThrows
    public static void assistantMessageManualConfiguration() {
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

        // 手动创建 AI 消息（例如，用于对话历史）
        AssistantMessage aiMsg = new AssistantMessage("我很乐意帮助你回答这个问题！");

        // 添加到对话历史
        List<org.springframework.ai.chat.messages.Message> messages = List.of(
                new SystemMessage("你是一个有帮助的助手"),
                new UserMessage("你能帮我吗？"),
                aiMsg,  // 插入，就像它来自模型一样
                new UserMessage("太好了！2+2 等于多少？")
        );

        ChatResponse response = chatModel.call(new Prompt(messages));
        AssistantMessage aiMessage = response.getResult().getOutput();
        System.out.println(aiMessage.getText());
    }

    /**
     * AssistantMessage 工具调用示例
     */
    @SneakyThrows
    public static void assistantMessageToolConfiguration() {
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

        ChatResponse response = chatModel.call(new Prompt("你好"));
        AssistantMessage aiMessage = response.getResult().getOutput();

        if (aiMessage.hasToolCalls()) {
            for (AssistantMessage.ToolCall toolCall : aiMessage.getToolCalls()) {
                System.out.println("Tool: " + toolCall.name());
                System.out.println("Args: " + toolCall.arguments());
                System.out.println("ID: " + toolCall.id());
            }
        }
    }

    /**
     * Token 使用信息访问示例
     */
    @SneakyThrows
    public static void assistantMessageTokenConfiguration() {
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

        ChatResponse response = chatModel.call(new Prompt("你好！"));
        ChatResponseMetadata metadata = response.getMetadata();

        // 访问使用信息
        if (metadata != null && metadata.getUsage() != null) {
            System.out.println("Input tokens: " + metadata.getUsage().getPromptTokens());
            System.out.println("Output tokens: " + metadata.getUsage().getCompletionTokens());
            System.out.println("Total tokens: " + metadata.getUsage().getTotalTokens());
        }
    }

    /**
     * 流式输出示例
     */
    @SneakyThrows
    public static void assistantMessageStreamConfiguration() {
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

        Flux<ChatResponse> responseStream = chatModel.stream(new Prompt("你好"));

        StringBuilder fullResponse = new StringBuilder();
        responseStream.subscribe(
                chunk -> {
                    String content = chunk.getResult().getOutput().getText();
                    fullResponse.append(content);
                    System.out.print(content);
                }
        );
    }

    /**
     * ToolResponseMessage 工具响应消息示例
     */
    @SneakyThrows
    public static void toolResponseMessageConfiguration() {
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

        // 在模型进行工具调用后
        AssistantMessage aiMessage = AssistantMessage.builder()
                .content("")
                .toolCalls(List.of(
                        new AssistantMessage.ToolCall(
                                "call_123",
                                "tool",
                                "get_weather",
                                "{\"location\": \"San Francisco\"}"
                        )
                ))
                .build();

        // 执行工具并创建结果消息
        String weatherResult = "晴朗，22°C";
        ToolResponseMessage toolMessage = ToolResponseMessage.builder()
                .responses(List.of(
                        new ToolResponseMessage.ToolResponse("call_123", "get_weather", weatherResult)
                ))
                .build();

        // 继续对话
        List<org.springframework.ai.chat.messages.Message> messages = List.of(
                new UserMessage("旧金山的天气怎么样？"),
                aiMessage,      // 模型的工具调用
                toolMessage     // 工具执行结果
        );
        ChatResponse response = chatModel.call(new Prompt(messages));
        System.out.println(response.getResult().getOutput().getText());
    }

    /**
     * 图像输入示例
     */
    @SneakyThrows
    public static void imageMessageConfiguration() {
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
        // 从 URL
        UserMessage urlMessage = UserMessage.builder()
                .text("描述这张图片的内容。")
                .media(Media.builder()
                        .mimeType(MimeTypeUtils.IMAGE_JPEG)
                        .data(new URL("https://example.com/image.jpg"))
                        .build())
                .build();
        // 从本地文件
        UserMessage localMessage = UserMessage.builder()
                .text("描述这张图片的内容。")
                .media(new Media(
                        MimeTypeUtils.IMAGE_JPEG,
                        new ClassPathResource("images/photo.jpg")
                ))
                .build();
        ChatResponse response = chatModel.call(new Prompt(urlMessage, localMessage));
        System.out.println(response.getResult().getOutput().getText());
    }

    /**
     * 音频输入示例
     */
    public static void audioMessageConfiguration() {
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

        UserMessage message = UserMessage.builder()
                .text("描述这段音频的内容。")
                .media(new Media(
                        MimeTypeUtils.parseMimeType("audio/wav"),
                        new ClassPathResource("audio/recording.wav")
                ))
                .build();
        ChatResponse response = chatModel.call(new Prompt(message));
        System.out.println(response.getResult().getOutput().getText());
    }

    /**
     * 视频输入示例
     */
    @SneakyThrows
    public static void videoMessageConfiguration() {
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

        UserMessage message = UserMessage.builder()
                .text("描述这段视频的内容。")
                .media(Media.builder()
                        .mimeType(MimeTypeUtils.parseMimeType("video/mp4"))
                        .data(new URL("https://example.com/path/to/video.mp4"))
                        .build())
                .build();
        ChatResponse response = chatModel.call(new Prompt(message));
        System.out.println(response.getResult().getOutput().getText());
    }

    // ==================== 与 Chat Models 一起使用 ====================

    /**
     * Chat Models — 基础对话示例
     */
    @SneakyThrows
    public static void chatModelBasicMessageConfiguration() {
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

        List<Message> conversationHistory = new ArrayList<>();

        // 第一轮对话
        conversationHistory.add(new UserMessage("你好！"));
        ChatResponse response1 = chatModel.call(new Prompt(conversationHistory));
        conversationHistory.add(response1.getResult().getOutput());

        // 第二轮对话
        conversationHistory.add(new UserMessage("你能帮我学习 Java 吗？"));
        ChatResponse response2 = chatModel.call(new Prompt(conversationHistory));
        conversationHistory.add(response2.getResult().getOutput());

        // 第三轮对话
        conversationHistory.add(new UserMessage("从哪里开始？"));
        ChatResponse response3 = chatModel.call(new Prompt(conversationHistory));

        String answer = response3.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * Chat Models — Builder 模式示例
     */
    @SneakyThrows
    public static void chatModelBuildMessageConfiguration() {
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

        List<Message> conversationHistory = new ArrayList<>();

        // UserMessage with builder
        UserMessage userMsg = UserMessage.builder()
                .text("你好，我想学习 Spring AI Alibaba")
                .metadata(Map.of("user_id", "user_123"))
                .build();
        conversationHistory.add(userMsg);

        // SystemMessage with builder
        SystemMessage systemMsg = SystemMessage.builder()
                .text("你是一个 Spring 框架专家")
                .metadata(Map.of("version", "1.0"))
                .build();
        conversationHistory.add(systemMsg);

        // AssistantMessage with builder
        AssistantMessage assistantMsg = AssistantMessage.builder()
                .content("我很乐意帮助你学习 Spring AI Alibaba！")
                .build();
        conversationHistory.add(assistantMsg);

        ChatResponse response = chatModel.call(new Prompt(conversationHistory));
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }

    /**
     * Chat Models — 消息复制和修改
     */
    public static void messageCopyAndModify() {
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

        // 复制消息
        UserMessage original = new UserMessage("原始消息");
        UserMessage copy = original.copy();

        // 使用 mutate 创建修改的副本
        UserMessage modified = original.mutate()
                .text("修改后的消息")
                .metadata(Map.of("modified", true))
                .build();

        ChatResponse response = chatModel.call(new Prompt(copy, modified));
        String answer = response.getResult().getOutput().getText();
        System.out.println(answer);
    }


    // ==================== 在 ReactAgent 中使用 ====================

    /**
     * 在 ReactAgent 中使用消息
     */
    public static void messagesInReactAgent() throws GraphRunnerException {
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

        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .systemPrompt("你是一个有帮助的助手")
                .build();

        // 使用字符串
        AssistantMessage response1 = agent.call("你好");

        // 使用 UserMessage
        UserMessage userMsg = new UserMessage("帮我写一首诗");
        AssistantMessage response2 = agent.call(userMsg);

        // 使用消息列表
        List<Message> messages = List.of(
                new UserMessage("我喜欢春天"),
                new UserMessage("写一首关于春天的诗")
        );
        AssistantMessage response3 = agent.call(messages);
    }
}
