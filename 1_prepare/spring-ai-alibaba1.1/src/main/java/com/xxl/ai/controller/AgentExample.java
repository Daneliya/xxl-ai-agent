package com.xxl.ai.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.agent.hook.Hook;
import com.alibaba.cloud.ai.graph.agent.hook.hip.HumanInTheLoopHook;
import com.alibaba.cloud.ai.graph.agent.hook.hip.ToolConfig;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @Author xxl
 * @Date 2025/11/28 11:45
 */
public class AgentExample {

    public static void main(String[] args) throws GraphRunnerException {
//        simpleModelConfiguration();
//        basicModelConfiguration();
        // 高级功能——使用 outputSchema 定义输出格式
//        advancedFeatureOutputSchema();
        // 高级功能——使用 invoke 方法获取完整状态
//        advancedFeatureInvoke();
        // 高级功能——使用 Hooks 扩展功能
        advancedFeatureHooks();
    }

    /**
     * 示例一：简单的基础模型配置
     *
     * @throws GraphRunnerException
     */
    public static void simpleModelConfiguration() throws GraphRunnerException {
        // 创建模型实例
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();

        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                .instruction("You are a helpful weather forecast assistant.")
                .build();

        // 运行 Agent
        agent.call("what is the weather in Hangzhou?");
    }

    /**
     * 示例一：第一个基础模型配置
     *
     * @throws GraphRunnerException
     */
    public static void basicModelConfiguration() throws GraphRunnerException {
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
        ToolCallback weatherTool = FunctionToolCallback.builder("get_weather", new WeatherTool())
                .description("Get weather for a given city")
                .inputType(String.class)
                .build();

        // 创建 agent
        ReactAgent agent = ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                .tools(weatherTool)
                .systemPrompt("You are a helpful assistant")
                .saver(new MemorySaver())
                .build();

        // 运行 agent
        AssistantMessage response = agent.call("what is the weather in San Francisco, please answer in Chinese");
        System.out.println(response.getText());
    }


    public static void realbasicModelConfiguration() {

    }

    /**
     * 高级功能——使用 outputSchema 定义输出格式
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureOutputSchema() throws GraphRunnerException {
        String customSchema = """
                请按照以下JSON格式输出：
                {
                    "title": "标题",
                    "content": "内容",
                    "style": "风格"
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
                .name("schema_agent")
                .model(chatModel)
                .saver(new MemorySaver())
                .outputSchema(customSchema)
                .build();

        AssistantMessage message = agent.call("帮我写一首关于春天的诗歌。");
        System.out.println(message.getText());
//        ```json
//        {
//            "title": "春之絮语",
//            "content": "春风轻拂绿意浓，\n细雨如丝润花丛。\n柳枝摇曳舞新翠，\n桃李争芳映晴空。\n溪水潺潺歌欢畅，\n燕语呢喃绕帘栊。\n万物复苏心亦暖，\n人间处处是春容。",
//            "style": "古典诗意"
//        }
//        ```
    }

    /**
     * 高级功能——使用 invoke 方法获取完整状态
     *
     * @throws GraphRunnerException
     */
    public static void advancedFeatureInvoke() throws GraphRunnerException {
        // 初始化 ChatModel
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
                .build();
        ChatModel chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
        ReactAgent agent = ReactAgent.builder()
                .name("schema_agent")
                .model(chatModel)
                .saver(new MemorySaver())
                .build();
        Optional<OverAllState> result = agent.invoke("帮我写一首诗。");

        if (result.isPresent()) {
            OverAllState state = result.get();
            // 访问消息历史
            List<Message> messages = state.value("messages", new ArrayList<>());
            // 访问其他状态信息
            System.out.println(state);
            System.out.println(messages);
        }
//        {"OverAllState":{"data":{"input":"帮我写一首诗。","messages":[{"messageType":"USER","metadata":{"messageType":"USER"},"media":[],"text":"帮我写一首诗。"},{"messageType":"ASSISTANT","metadata":{"finishReason":"STOP","search_info":"","role":"ASSISTANT","id":"802132ec-3c88-4aab-8816-c99badcda917","messageType":"ASSISTANT","reasoningContent":""},"toolCalls":[],"media":[],"text":"春风拂柳绿成行，  \n细雨轻沾花自香。  \n燕语呢喃穿旧巷，  \n纸鸢牵梦到斜阳。  \n\n山远含烟藏古寺，  \n水柔如练绕村庄。  \n莫问归期何处定，  \n心安已是好时光。"}]}}}
//[UserMessage {content='帮我写一首诗。', metadata={messageType=USER}, messageType=USER}, AssistantMessage [messageType=ASSISTANT, toolCalls=[], textContent=春风拂柳绿成行，
//        细雨轻沾花自香。
//        燕语呢喃穿旧巷，
//        纸鸢牵梦到斜阳。
//
//        山远含烟藏古寺，
//        水柔如练绕村庄。
//        莫问归期何处定，
//        心安已是好时光。, metadata={finishReason=STOP, search_info=, role=ASSISTANT, id=802132ec-3c88-4aab-8816-c99badcda917, messageType=ASSISTANT, reasoningContent=}]]
    }

    /**
     * 高级功能——使用 Hooks 扩展功能
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
}
