package com.xxl.ai.example;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.Hook;
import com.alibaba.cloud.ai.graph.agent.hook.hip.HumanInTheLoopHook;
import com.alibaba.cloud.ai.graph.agent.hook.hip.ToolConfig;
import com.alibaba.cloud.ai.graph.agent.hook.modelcalllimit.ModelCallLimitHook;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.xxl.ai.hook.CustomStopConditionHook;
import com.xxl.ai.interceptor.DynamicPromptInterceptor;
import com.xxl.ai.interceptor.GuardrailInterceptor;
import com.xxl.ai.interceptor.ToolErrorInterceptor;
import com.xxl.ai.interceptor.ToolMonitoringInterceptor;
import com.xxl.ai.output.PoemOutput;
import com.xxl.ai.tool.SearchTool;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Agents 示例
 * <p>
 * https://java2ai.com/docs/frameworks/agent-framework/tutorials/agents
 *
 * @Author xxl
 * @Date 2025/11/28 11:45
 */
public class Example01_Agent {

    public static void main(String[] args) throws GraphRunnerException {
        // 基础模型配置
//        basicModelConfiguration();
        // 高级模型配置
//        advancedModelConfiguration();
        // 工具组件——搜索工具
//        toolSearchModelConfigurationtoolSearchModelConfiguration();
        // 工具组件——工具错误处理
//        toolErrorModelConfiguration();
        // 动态 System Prompt
//        systemPromptModelConfiguration();
        // 高级功能——使用 outputType 定义输出格式
//        advancedFeatureOutputType();
        // 高级功能——使用 outputSchema 定义输出格式
//        advancedFeatureOutputSchema();
        // 高级功能——Memory 记忆
//        advancedFeatureMemory();
        // 高级功能——Hooks 钩子
//        advancedFeatureHooks();
        // 高级功能——Interceptor 拦截器
//        advancedFeatureInterceptor();
        // 高级功能——使用 ModelCallLimitHook 限制模型调用次数
//        advancedFeatureModelCallLimitHook();
        // 高级功能——自定义停止条件 Hook
//        advancedFeatureCustomStopConditionHook();
        // 高级功能——流式输出
        advancedFeatureAgentStream();
    }

    /**
     * 基础模型配置
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

        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .build();
    }

    /**
     * 高级模型配置
     */
    public static void advancedModelConfiguration() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();

        // 创建 ChatModel
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(DashScopeChatOptions.builder()
                        .withTemperature(0.7) // 控制随机性
                        .withMaxToken(2000) // 最大输出长度
                        .withTopP(0.9) // 核采样参数
                        .build())
                .build();

        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .build();
    }

    /**
     * 工具组件——搜索工具
     *
     * @throws GraphRunnerException 异常
     */
    public static void toolSearchModelConfiguration() throws GraphRunnerException {
        // 创建模型实例
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
                .name("search_agent")
                .model(chatModel)
                .tools(searchTool)
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("查询杭州天气并推荐活动");
        System.out.println(response.getText());
    }

    /**
     * 工具组件——工具错误处理
     *
     * @throws GraphRunnerException 异常
     */
    public static void toolErrorModelConfiguration() throws GraphRunnerException {
        // 创建模型实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("error_agent")
                .model(chatModel)
                .interceptors(new ToolErrorInterceptor())
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("Who are you?");
        System.out.println(response.getText());
    }

    /**
     * 动态 System Prompt
     *
     * @throws GraphRunnerException 异常
     */
    public static void systemPromptModelConfiguration() throws GraphRunnerException {
        // 创建模型实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("adaptive_agent")
                .model(chatModel)
                .interceptors(new DynamicPromptInterceptor())
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("Spring AI Alibaba是个什么框架?");
        System.out.println(response.getText());
    }

    /**
     * 高级功能——使用 outputType 定义输出格式
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureOutputType() throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        ReactAgent agent = ReactAgent.builder()
                .name("poem_agent")
                .model(chatModel)
                .outputType(PoemOutput.class)
                .saver(new MemorySaver())
                .build();

        AssistantMessage message = agent.call("帮我写一首关于春天的诗歌。");
        System.out.println(message.getText());
//        {
//            "content": "春风轻拂绿意生，\n细雨如丝润无声。\n桃花笑映晨光里，\n柳絮飘飞暮色中。\n溪水潺潺歌新曲，\n燕子呢喃筑旧踪。\n万物复苏心亦暖，\n人间最美是春浓。",
//            "style": "古典",
//            "title": "春之韵"
//        }
    }

    /**
     * 高级功能——使用 outputSchema 定义输出格式
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureOutputSchema() throws GraphRunnerException {
        String customSchema = """
                请严格按照以下JSON格式返回结果：
                {
                    "summary": "内容摘要",
                    "keywords": ["关键词1", "关键词2", "关键词3"],
                    "sentiment": "情感倾向（正面/负面/中性）",
                    "confidence": 0.95
                }
                """;
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        ReactAgent agent = ReactAgent.builder()
                .name("analysis_agent")
                .model(chatModel)
                .outputSchema(customSchema)
                .saver(new MemorySaver())
                .build();

        AssistantMessage response = agent.call("分析这段文本：春天来了，万物复苏。");
        System.out.println(response.getText());
    }

    /**
     * 高级功能——Memory 记忆
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureMemory() throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 配置内存存储
        ReactAgent agent = ReactAgent.builder()
                .name("chat_agent")
                .model(chatModel)
                .saver(new MemorySaver())
                .build();

        // 使用 thread_id 维护对话上下文
        RunnableConfig config = RunnableConfig.builder()
                .threadId("user_123")
                .build();

        AssistantMessage message01 = agent.call("我叫张三", config);
        System.out.println(message01.getText());
        AssistantMessage message02 = agent.call("我叫什么名字？", config);
        System.out.println(message02.getText());  // 输出: "你叫张三"
    }

    /**
     * 高级功能——Hooks 钩子
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureHooks() throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        // 创建 hook
        Hook humanInTheLoopHook = HumanInTheLoopHook.builder()
                .approvalOn("getWeatherTool", ToolConfig.builder().description("Please confirm tool execution.").build())
                .build();

        ReactAgent agent = ReactAgent.builder()
                .name("schema_agent")
                .model(chatModel)
                .hooks(humanInTheLoopHook)
                .saver(new MemorySaver())
                .build();
        AssistantMessage message = agent.call("帮我写一首关于春天的诗歌。");
        System.out.println(message.getText());
    }

    /**
     * 高级功能——Interceptor 拦截器
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureInterceptor() throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();

        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                // 内容安全检查、监控和错误处理
                .interceptors(List.of(new GuardrailInterceptor(), new ToolMonitoringInterceptor()))
                .saver(new MemorySaver())
                .build();
        AssistantMessage message = agent.call("帮我写一首关于春天的诗歌。");
        System.out.println(message.getText());
    }

    /**
     * 高级功能——使用 ModelCallLimitHook 限制模型调用次数
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureModelCallLimitHook() throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();

        // 使用内置的 ModelCallLimitHook 限制模型调用次数
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .hooks(ModelCallLimitHook.builder().runLimit(5).build())  // 限制最多调用 5 次
                .saver(new MemorySaver())
                .build();

        AssistantMessage message = agent.call("帮我写一首关于春天的诗歌。");
        System.out.println(message.getText());
    }

    /**
     * 高级功能——自定义停止条件 Hook
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureCustomStopConditionHook() throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();

        // 使用内置的 ModelCallLimitHook 限制模型调用次数
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .hooks(new CustomStopConditionHook())
                .saver(new MemorySaver())
                .build();

        AssistantMessage message = agent.call("帮我写一首关于春天的诗歌。");
        System.out.println(message.getText());
    }

    /**
     * 高级功能——流式输出
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureAgentStream() throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();

        // 使用内置的 ModelCallLimitHook 限制模型调用次数
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .hooks(new CustomStopConditionHook())
                .saver(new MemorySaver())
                .build();

        Flux<NodeOutput> stream = agent.stream("帮我写一首关于春天的诗歌。");
        stream.subscribe(
                response -> System.out.println("进度: " + response),
                error -> System.err.println("错误: " + error),
                () -> System.out.println("完成")
        );
    }
}
