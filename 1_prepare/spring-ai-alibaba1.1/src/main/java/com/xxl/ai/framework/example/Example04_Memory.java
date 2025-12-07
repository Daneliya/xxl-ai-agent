package com.xxl.ai.framework.example;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.checkpoint.savers.RedisSaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.xxl.ai.framework.hook.MessageSummarizationHook;
import com.xxl.ai.framework.hook.MessageTrimHook;
import com.xxl.ai.framework.hook.MessageTrimmingHook;
import com.xxl.ai.framework.hook.ValidateResponseHook;
import com.xxl.ai.framework.interceptor.DynamicPromptInterceptor;
import com.xxl.ai.framework.tool.UserInfoTool;
import lombok.SneakyThrows;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * Memory 短期记忆
 *
 * @Author xxl
 * @Date 2025/12/2 10:17
 */
public class Example04_Memory {

    public static void main(String[] args) throws GraphRunnerException {
        System.out.println("=== Memory Tutorial Examples ===");
        // 配置短期记忆 示例
//        shortTermMemoryConfiguration();
        // 使用 Redis Checkpointer 示例
//        redisMemoryConfiguration();
        // 修剪消息 示例
//        messageTrimmingConfiguration();
        // 用户信息记忆 示例
        userToolConfiguration();
    }

    /**
     * 配置短期记忆 示例
     */
    @SneakyThrows
    public static void shortTermMemoryConfiguration() {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel("deepseek-v3.2")           // 模型名称
                .withTemperature(0.3)                 // 温度参数
                .withMaxToken(500)          // 最大令牌数
                .withTopP(0.9)                        // Top-P 采样
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();
        // 创建工具
        ToolCallback getUserInfoTool = createGetUserInfoTool();
        // 配置 checkpointer
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .tools(getUserInfoTool)
                .saver(new MemorySaver())
                .build();
        // 使用 thread_id 维护对话上下文
        RunnableConfig config = RunnableConfig.builder()
                .threadId("1") // threadId 指定会话 ID
                .build();
        agent.call("你好！我叫 Bob。", config);
    }

    /**
     * 使用 Redis Checkpointer 示例
     */
    @SneakyThrows
    public static void redisMemoryConfiguration() {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel("deepseek-v3.2")           // 模型名称
                .withTemperature(0.3)                 // 温度参数
                .withMaxToken(500)          // 最大令牌数
                .withTopP(0.9)                        // Top-P 采样
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();
        // 创建工具
        ToolCallback getUserInfoTool = createGetUserInfoTool();
        // 配置 Redis checkpointer
        RedisSaver redisSaver = createRedisSaver();
        // 配置 checkpointer
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .tools(getUserInfoTool)
                .tools()
                .saver(redisSaver)
                .build();
        // 使用 thread_id 维护对话上下文
        RunnableConfig config = RunnableConfig.builder()
                .threadId("1") // threadId 指定会话 ID
                .build();
        AssistantMessage message01 = agent.call("你好！我叫 Bob。", config);
        System.out.println(message01.getText());
        AssistantMessage message02 = agent.call("你好！我叫什么。", config);
        System.out.println(message02.getText());
    }

    /**
     * 初始化 RedisSaver
     */
    public static RedisSaver createRedisSaver() {
        // 配置 Redisson 客户端
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:6379");  // Redis 地址

        RedissonClient redisson = Redisson.create(config);
        return new RedisSaver(redisson);
    }

    /**
     * 创建获取用户信息工具
     */
    private static ToolCallback createGetUserInfoTool() {
        return FunctionToolCallback.builder("get_user_info", (String userId) -> {
                    // 简化的实现
                    return "User info for: " + userId;
                })
                .description("Get user information by ID")
                .inputType(String.class)
                .build();
    }

    /**
     * MessageTrimmingHook 修剪消息示例
     */
    @SneakyThrows
    private static void messageTrimmingConfiguration() {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("trimming_agent")
                .model(chatModel)
                .hooks(new MessageTrimmingHook())
                .saver(new MemorySaver())
                .build();

        RunnableConfig config = RunnableConfig.builder()
                .threadId("1")
                .build();

        agent.call("你好，我叫 bob", config);
        agent.call("写一首关于猫的短诗", config);
        agent.call("现在对狗做同样的事情", config);
        AssistantMessage finalResponse = agent.call("我叫什么名字？", config);

        System.out.println(finalResponse.getText());
        // 输出：
        // 你叫 Bob！很高兴认识你，Bob 😊
        // 我记性还不错吧？要不要给你的名字也写首诗？😄
    }

    /**
     * MessageSummarizationHook 总结消息示例
     */
    @SneakyThrows
    private static void messageSummarizationConfiguration() {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();

        ChatModel summaryModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        MessageSummarizationHook summarizationHook = new MessageSummarizationHook(
                summaryModel,
                4000,  // 在 4000 tokens 时触发总结
                20     // 总结后保留最后 20 条消息
        );
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .hooks(summarizationHook)
                .saver(new MemorySaver())
                .build();

        RunnableConfig config = RunnableConfig.builder()
                .threadId("1")
                .build();

        agent.call("你好，我叫 bob", config);
        agent.call("写一首关于猫的短诗", config);
        agent.call("现在对狗做同样的事情", config);
        AssistantMessage finalResponse = agent.call("我叫什么名字？", config);

        System.out.println(finalResponse.getText());
    }

    /**
     * 在工具中读取短期记忆示例
     */
    @SneakyThrows
    private static void userToolConfiguration() {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 创建工具
        ToolCallback getUserInfoTool = FunctionToolCallback
                .builder("get_user_info", new UserInfoTool())
                .description("查找用户信息")
                .inputType(String.class)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .tools(getUserInfoTool)
                .saver(new MemorySaver())
                .build();

        RunnableConfig config = RunnableConfig.builder()
                .threadId("1")
                .addMetadata("user_id", "user_123")
                .build();

        AssistantMessage response = agent.call("获取用户信息", config);
        System.out.println(response.getText());
    }

    /**
     * DynamicPromptInterceptor 动态提示示例
     */
    @SneakyThrows
    private static void dynamicPromptInterceptorConfiguration() {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 定义天气查询工具
        class WeatherTool implements BiFunction<String, ToolContext, String> {
            @Override
            public String apply(String city, ToolContext toolContext) {
                return "It's always sunny in " + city + "!";
            }
        }
        ToolCallback getWeatherTool = FunctionToolCallback.builder("get_weather", new WeatherTool())
                .description("Get weather for a given city")
                .inputType(String.class)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .tools(getWeatherTool)
                .interceptors(new DynamicPromptInterceptor())
                .build();
        // 使用时传递上下文
        Map<String, Object> context = Map.of("user_name", "John Smith");
    }

    /**
     * MessageTrimHook Before Model 示例
     */
    @SneakyThrows
    private static void messageTrimHookBeforeModelConfiguration() {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .hooks(new MessageTrimHook())
                .saver(new MemorySaver())
                .build();
        AssistantMessage response = agent.call("你好");
        System.out.println(response.getText());
    }

    /**
     * ValidateResponseHook After Model 示例
     */
    @SneakyThrows
    private static void validateResponseHookAfterModelConfiguration() {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("secure_agent")
                .model(chatModel)
                .hooks(new ValidateResponseHook())
                .saver(new MemorySaver())
                .build();
        AssistantMessage response = agent.call("你好");
        System.out.println(response.getText());
    }
}
