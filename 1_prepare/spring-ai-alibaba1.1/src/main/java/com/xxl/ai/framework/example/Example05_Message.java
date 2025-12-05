package com.xxl.ai.framework.example;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import lombok.SneakyThrows;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;

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
     * UserMessage 内容示例
     */
    @SneakyThrows
    public static void userMessageConfiguration() {
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
        //
        UserMessage userMsg = UserMessage.builder()
                .text("你好！")
                .metadata(Map.of(
                        "user_id", "alice",  // 可选：识别不同用户
                        "session_id", "sess_123"  // 可选：会话标识符
                ))
                .build();
        ChatResponse response3 = chatModel.call(new Prompt(userMsg));
        String answer03 = response3.getResult().getOutput().getText();
        System.out.println(answer03);
        // 从 URL 创建图像
        UserMessage userMsg2 = UserMessage.builder()
                .text("描述这张图片的内容。")
                .media(Media.builder()
                        .mimeType(MimeTypeUtils.IMAGE_JPEG)
                        .data(new URL("https://example.com/image.jpg"))
                        .build())
                .build();
        ChatResponse response4 = chatModel.call(new Prompt(userMsg2));
        String answer04 = response4.getResult().getOutput().getText();
        System.out.println(answer04);
    }

    /**
     * 图像输入示例
     */
    @SneakyThrows
    public static void imageMessageConfiguration() {
        // 从 URL
        UserMessage message = UserMessage.builder()
                .text("描述这张图片的内容。")
                .media(Media.builder()
                        .mimeType(MimeTypeUtils.IMAGE_JPEG)
                        .data(new URL("https://example.com/image.jpg"))
                        .build())
                .build();
        // 从本地文件
        UserMessage message2 = UserMessage.builder()
                .text("描述这张图片的内容。")
                .media(new Media(
                        MimeTypeUtils.IMAGE_JPEG,
                        new ClassPathResource("images/photo.jpg")
                ))
                .build();
    }

    /**
     * 图像输入示例
     */
    public static void audioMessageConfiguration() {
        UserMessage message = UserMessage.builder()
                .text("描述这段音频的内容。")
                .media(new Media(
                        MimeTypeUtils.parseMimeType("audio/wav"),
                        new ClassPathResource("audio/recording.wav")
                ))
                .build();
    }

    /**
     * 视频输入示例
     */
    @SneakyThrows
    public static void videoMessageConfiguration() {
        UserMessage message = UserMessage.builder()
                .text("描述这段视频的内容。")
                .media(Media.builder()
                        .mimeType(MimeTypeUtils.parseMimeType("video/mp4"))
                        .data(new URL("https://example.com/path/to/video.mp4"))
                        .build())
                .build();
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
        // 复制消息
        UserMessage original = new UserMessage("原始消息");
        UserMessage copy = original.copy();

        // 使用 mutate 创建修改的副本
        UserMessage modified = original.mutate()
                .text("修改后的消息")
                .metadata(Map.of("modified", true))
                .build();
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
