package com.xxl.ai.framework.example;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.ai.framework.output.ContactOutput;
import com.xxl.ai.framework.output.ValidatedOutput;
import lombok.SneakyThrows;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;

import java.io.DataOutput;

/**
 * @Classname Example07_StructuredOutput
 * @Description Structured Output 结构化输出
 * @Date 2025/12/7 22:17
 * @Created by xxl
 */
public class Example07_StructuredOutput {

    public static void main(String[] args) {
//        jsonConfiguration();
//        productReviewSchemaConfiguration();
        analysisSchemaConfiguration();
    }

    /**
     * 基本 JSON Schema 示例
     */
    @SneakyThrows
    public static void jsonConfiguration() {
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

        String contactInfoSchema = """
                请按照以下JSON格式输出：
                {
                    "name": "人名",
                    "email": "电子邮箱地址",
                    "phone": "电话号码"
                }
                """;
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("contact_extractor")
                .model(chatModel)
                .outputSchema(contactInfoSchema)
                .build();
        // 调用并获取响应
        AssistantMessage result = agent.call("从以下信息提取联系方式：张三，zhangsan@example.com，(555) 123-4567");
        System.out.println(result.getText());
        // 输出:
        //        {
        //            "name": "张三",
        //            "email": "zhangsan@example.com",
        //            "phone": "(555) 123-4567"
        //        }
    }

    /**
     * 复杂嵌套 Schema 示例
     */
    @SneakyThrows
    public static void productReviewSchemaConfiguration() {
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

        String productReviewSchema = """
                请严格按照以下JSON格式返回产品评价分析：
                {
                    "rating": 1-5之间的整数评分,
                    "sentiment": "情感倾向（正面/负面/中性）",
                    "keyPoints": ["关键点1", "关键点2", "关键点3"],
                    "details": {
                        "pros": ["优点1", "优点2"],
                        "cons": ["缺点1", "缺点2"]
                    }
                }
                """;
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("review_analyzer")
                .model(chatModel)
                .outputSchema(productReviewSchema)
                .build();
        // 调用并获取响应
        AssistantMessage result = agent.call("分析评价：这个产品很棒，5星好评。配送快速，但价格稍贵。");
        System.out.println(result.getText());
        // 输出:
        //```json
        //        {
        //            "rating": 5,
        //            "sentiment": "正面",
        //            "keyPoints": ["产品很棒", "配送快速", "价格稍贵"],
        //            "details": {
        //                "pros": ["产品品质好", "配送速度快"],
        //                "cons": ["价格偏高"]
        //            }
        //        }
        //```
    }

    /**
     * 结构化分析 Schema 示例
     */
    @SneakyThrows
    public static void analysisSchemaConfiguration() {
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

        String analysisSchema = """
                请按照以下JSON格式返回文本分析结果：
                {
                    "summary": "内容摘要（50字以内）",
                    "keywords": ["关键词1", "关键词2", "关键词3"],
                    "sentiment": "情感倾向（正面/负面/中性）",
                    "entities": {
                        "persons": ["人名1", "人名2"],
                        "locations": ["地点1", "地点2"],
                        "organizations": ["组织1", "组织2"]
                    }
                }
                """;
        // 创建 Agent
        ReactAgent agent = ReactAgent.builder()
                .name("text_analyzer")
                .model(chatModel)
                .outputSchema(analysisSchema)
                .build();
        // 调用并获取响应
        AssistantMessage result = agent.call("分析这段文字：昨天，李明在北京参加了阿里巴巴公司的技术大会，感受到了创新的力量。");
        System.out.println(result.getText());
        // 输出:
        //```json
        //        {
        //            "summary": "李明在北京参加阿里巴巴技术大会，感受到创新力量。",
        //            "keywords": ["李明", "阿里巴巴", "技术大会", "创新"],
        //            "sentiment": "正面",
        //            "entities": {
        //                "persons": ["李明"],
        //                "locations": ["北京"],
        //                "organizations": ["阿里巴巴公司"]
        //            }
        //        }
        //```
    }

    // ==================== 输出类型策略 ====================

    /**
     * Try-Catch 错误处理示例
     */
    @SneakyThrows
    public static void tryCatchConfiguration() {
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
                .name("data_extractor")
                .model(chatModel)
                .outputType(DataOutput.class)
                .build();
        // 调用并获取响应
        AssistantMessage result = agent.call("提取数据");
        try {
            ObjectMapper mapper = new ObjectMapper();
            DataOutput data = mapper.readValue(result.getText(), DataOutput.class);
            // 处理数据
        } catch (JsonProcessingException e) {
            System.err.println("JSON解析失败: " + e.getMessage());
            System.err.println("原始输出: " + result.getText());
            // 回退处理
        }
    }

    /**
     * 验证模式示例
     */
    @SneakyThrows
    public static void validationPatternConfiguration() {
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
                .name("validated_agent")
                .model(chatModel)
                .outputType(ValidatedOutput.class)
                .build();
        // 调用并获取响应
        try {
            AssistantMessage result = agent.call("生成评价");
            ObjectMapper mapper = new ObjectMapper();
            ValidatedOutput output = mapper.readValue(result.getText(), ValidatedOutput.class);
            output.validate();  // 如果无效则抛出异常
            System.out.println("Valid output: " + output.getTitle());
        } catch (Exception e) {
            System.err.println("Validation failed: " + e.getMessage());
        }
    }

    /**
     * 重试模式示例
     */
    public static void retryPatternConfiguration() {
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

        ReactAgent agent = ReactAgent.builder()
                .name("retry_agent")
                .model(chatModel)
                .outputType(ContactOutput.class)
                .build();

        int maxRetries = 3;
        ContactOutput data = null;
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < maxRetries; i++) {
            try {
                AssistantMessage result = agent.call("提取数据");
                data = mapper.readValue(result.getText(), ContactOutput.class);
                break;  // 成功
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    throw new RuntimeException("多次尝试后仍然失败", e);
                }
                System.out.println("第" + (i + 1) + "次尝试失败，重试中...");
            }
        }
        if (data != null) {
            System.out.println("Successfully extracted: " + data.getName());
        }
    }

}
