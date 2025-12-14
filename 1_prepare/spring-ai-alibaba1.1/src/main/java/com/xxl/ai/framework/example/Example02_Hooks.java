package com.xxl.ai.framework.example;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.hip.HumanInTheLoopHook;
import com.alibaba.cloud.ai.graph.agent.hook.hip.ToolConfig;
import com.alibaba.cloud.ai.graph.agent.hook.modelcalllimit.ModelCallLimitHook;
import com.alibaba.cloud.ai.graph.agent.hook.pii.PIIDetectionHook;
import com.alibaba.cloud.ai.graph.agent.hook.pii.PIIType;
import com.alibaba.cloud.ai.graph.agent.hook.pii.RedactionStrategy;
import com.alibaba.cloud.ai.graph.agent.hook.summarization.SummarizationHook;
import com.alibaba.cloud.ai.graph.agent.interceptor.contextediting.ContextEditingInterceptor;
import com.alibaba.cloud.ai.graph.agent.interceptor.todolist.TodoListInterceptor;
import com.alibaba.cloud.ai.graph.agent.interceptor.toolemulator.ToolEmulatorInterceptor;
import com.alibaba.cloud.ai.graph.agent.interceptor.toolretry.ToolRetryInterceptor;
import com.alibaba.cloud.ai.graph.agent.interceptor.toolselection.ToolSelectionInterceptor;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.xxl.ai.framework.hook.CustomAgentHook;
import com.xxl.ai.framework.hook.CustomModelHook;
import com.xxl.ai.framework.hook.ModelCallCounterHook;
import com.xxl.ai.framework.hook.ModelCallLimiterHook;
import com.xxl.ai.framework.interceptor.*;
import com.xxl.ai.framework.tool.SearchTool;
import lombok.SneakyThrows;
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

    public static void main(String[] args) {
        // 性能监控
//        hookPerformanceInterceptors();
//        customModelHook();
//        customAgentHook();
//        customModelInterceptor();
//        customTookInterceptor();
//        modelCallCounterHook();
//        contentModerationInterceptor();
        performanceInterceptor();
    }

    /**
     * SummarizationHook 消息压缩示例
     */
    @SneakyThrows
    public static void summarizationHook() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建消息压缩 Hook
        SummarizationHook summarizationHook = SummarizationHook.builder()
                .model(chatModel)
                .maxTokensBeforeSummary(4000)
                .messagesToKeep(20)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("my_agent")
                .model(chatModel)
                .hooks(summarizationHook)
                .build();
    }

    /**
     * HumanInTheLoopHook 人机协同示例
     */
    @SneakyThrows
    public static void humanReviewHook() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建工具（示例）
        ToolCallback sendEmailTool = createSendEmailTool();
        ToolCallback deleteDataTool = createDeleteDataTool();
        // 创建 Human-in-the-Loop Hook
        HumanInTheLoopHook humanReviewHook = HumanInTheLoopHook.builder()
                .approvalOn("sendEmailTool", ToolConfig.builder().description("Please confirm sending the email.").build())
                .approvalOn("deleteDataTool", ToolConfig.builder().description("Please confirm deleting the data.").build())
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("supervised_agent")
                .model(chatModel)
                .tools(sendEmailTool, deleteDataTool)
                .hooks(humanReviewHook)
                .saver(new MemorySaver())
                .build();
    }

    /**
     * PIIDetectionHook PII 检测示例
     */
    @SneakyThrows
    public static void piiDetectionHook() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建 PIIDetection Hook
        PIIDetectionHook pii = PIIDetectionHook.builder()
                .piiType(PIIType.EMAIL)
                .strategy(RedactionStrategy.REDACT)
                .applyToInput(true)
                .build();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("secure_agent")
                .model(chatModel)
                .hooks(pii)
                .build();
    }

    /**
     * ToolRetryInterceptor 工具重试示例
     */
    @SneakyThrows
    public static void toolRetryInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建工具（示例）
        ToolCallback searchTool = createSearchTool();
        ToolCallback databaseTool = createDatabaseTool();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("resilient_agent")
                .model(chatModel)
                .tools(searchTool, databaseTool)
                .interceptors(ToolRetryInterceptor.builder()
                        .maxRetries(2)
                        .onFailure(ToolRetryInterceptor.OnFailureBehavior.RETURN_MESSAGE)
                        .build())
                .build();
    }

    /**
     * TodoListInterceptor 规划示例
     */
    @SneakyThrows
    public static void todoListInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建工具（示例）
        ToolCallback myTool = createSampleTool();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("planning_agent")
                .model(chatModel)
                .tools(myTool)
                .interceptors(TodoListInterceptor.builder().build())
                .build();
    }

    /**
     * ToolSelectionInterceptor LLM 工具选择器示例
     */
    @SneakyThrows
    public static void toolSelectionInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建工具（示例）
        ToolCallback tool1 = createSampleTool();
        ToolCallback tool2 = createSampleTool();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("smart_selector_agent")
                .model(chatModel)
                .tools(tool1, tool2)
                .interceptors(ToolSelectionInterceptor.builder().build())
                .build();
    }

    /**
     * ToolEmulatorInterceptor LLM 工具模拟器示例
     */
    @SneakyThrows
    public static void toolEmulatorInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建工具（示例）
        ToolCallback simulatedTool = createSampleTool();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("emulator_agent")
                .model(chatModel)
                .tools(simulatedTool)
                .interceptors(ToolEmulatorInterceptor.builder().model(chatModel).build())
                .build();
    }

    /**
     * ContextEditingInterceptor 上下文编辑示例
     */
    @SneakyThrows
    public static void contextEditingInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("context_aware_agent")
                .model(chatModel)
                .interceptors(ContextEditingInterceptor.builder().trigger(120000).clearAtLeast(60000).build())
                .build();
    }

    /**
     * CustomModelHook 自定义 ModelHook 示例
     */
    @SneakyThrows
    public static void customModelHook() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("custom_model_agent")
                .model(chatModel)
                .hooks(new CustomModelHook())
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("为什么半途而废的人那么多，中途岛却不是世界上人口最密集的地方？");
        System.out.println(response.getText());
    }

    /**
     * CustomAgentHook 自定义 AgentHook 示例
     */
    @SneakyThrows
    public static void customAgentHook() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("custom_agent_agent")
                .model(chatModel)
                .hooks(new CustomAgentHook())
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("对下联：过去已过去，未来尚未来");
        System.out.println(response.getText());
    }

    /**
     * LoggingInterceptor 自定义 ModelInterceptor 示例
     */
    @SneakyThrows
    public static void customModelInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("custom_model_agent")
                .model(chatModel)
                .interceptors(new LoggingInterceptor())
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("“单车欲问边”证明了早在唐朝就有了自行车");
        System.out.println(response.getText());
    }

    /**
     * ToolMonitoringInterceptor 自定义 ToolInterceptor 示例
     */
    @SneakyThrows
    public static void customTookInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("custom_took_agent")
                .model(chatModel)
                .interceptors(new ToolCustomMonitoringInterceptor())
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("听说拼多多可以假一赔十，那我开店卖假货自己买，岂不是一本十利啊");
        System.out.println(response.getText());
    }

    /**
     * 使用 ModelCallCounterHook 和 ModelCallLimiterHook
     */
    @SneakyThrows
    public static void modelCallCounterHook() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("limited_agent")
                .model(chatModel)
                .hooks(new ModelCallCounterHook())  // 监控调用统计
                .hooks(new ModelCallLimiterHook(5)) // 限制最多调用 5 次
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("公司的水太深，所以上班才会摸鱼。");
        System.out.println(response.getText());
    }

    /**
     * 内容审核 ContentModerationInterceptor 示例
     */
    @SneakyThrows
    public static void contentModerationInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("content_moderation_agent")
                .model(chatModel)
                .interceptors(new ContentModerationInterceptor())  // 内容审核
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("都说女人是水做的，被我气到冒泡的女朋友算百事还是可口?");
        System.out.println(response.getText());
    }

    /**
     * 性能监控 Interceptor 示例
     */
    @SneakyThrows
    public static void performanceInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建工具（示例）
        ToolCallback tool = createSampleTool();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("monitored_agent")
                .model(chatModel)
                .tools(tool)
                .interceptors(new ModelPerformanceInterceptor())
                .interceptors(new ToolPerformanceInterceptor())
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("我买了一斤藕，为什么半斤都是空的？");
        System.out.println(response.getText());
    }

    /**
     * 工具缓存 示例
     */
    @SneakyThrows
    public static void toolCacheInterceptor() {
        // 创建 DashScope API 实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        // 模型配置
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
        // 创建工具（示例）
        ToolCallback tool = createSampleTool();
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("monitored_agent")
                .model(chatModel)
                .tools(tool)
//                .interceptors(new ToolCacheInterceptor())
                .build();
        // 运行 Agent
        AssistantMessage response = agent.call("为什么运动员的教练不去比赛呢？");
        System.out.println(response.getText());
    }

    // ==================== 自定义 Hooks ====================

    // 创建示例工具的辅助方法
    private static ToolCallback createSendEmailTool() {
        return FunctionToolCallback.builder("sendEmailTool", (String input) -> "Email sent")
                .description("Send an email")
                .inputType(String.class)
                .build();
    }

    private static ToolCallback createDeleteDataTool() {
        return FunctionToolCallback.builder("deleteDataTool", (String input) -> "Data deleted")
                .description("Delete data")
                .inputType(String.class)
                .build();
    }

    // ==================== 自定义 Interceptors ====================

    private static ToolCallback createSearchTool() {
        return FunctionToolCallback.builder("searchTool", (String input) -> "Search results")
                .description("Search the web")
                .inputType(String.class)
                .build();
    }

    private static ToolCallback createDatabaseTool() {
        return FunctionToolCallback.builder("databaseTool", (String input) -> "Database query results")
                .description("Query database")
                .inputType(String.class)
                .build();
    }

    // ==================== 辅助类和方法 ====================

    private static ToolCallback createSampleTool() {
        return FunctionToolCallback.builder("sampleTool", (String input) -> "Sample result")
                .description("A sample tool")
                .inputType(String.class)
                .build();
    }
}
