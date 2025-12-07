package com.xxl.ai.framework.interceptor;

import com.alibaba.cloud.ai.graph.agent.interceptor.ModelCallHandler;
import com.alibaba.cloud.ai.graph.agent.interceptor.ModelInterceptor;
import com.alibaba.cloud.ai.graph.agent.interceptor.ModelRequest;
import com.alibaba.cloud.ai.graph.agent.interceptor.ModelResponse;
import org.springframework.ai.chat.messages.SystemMessage;

import java.util.Map;

/**
 * @Classname DynamicPromptNameInterceptor
 * @Description DynamicPromptNameInterceptor 动态提示
 * @Date 2025/12/7 19:55
 * @Created by xxl
 */
public class DynamicPromptNameInterceptor extends ModelInterceptor {

    @Override
    public ModelResponse interceptModel(ModelRequest request, ModelCallHandler handler) {
        // 从上下文中获取用户名
        Map<String, Object> context = request.getContext();
        String userName = (String) context.get("user_name");

        // 创建动态系统提示
        String systemPrompt = "你是一个有帮助的助手。称呼用户为 " + userName + "。";

        // 创建修改后的请求（示例），实际使用中需要根据具体 API 进行调整
        SystemMessage enhancedSystemMessage;
        if (request.getSystemMessage() == null) {
            enhancedSystemMessage = new SystemMessage(systemPrompt);
        } else {
            enhancedSystemMessage = new SystemMessage(request.getSystemMessage().getText() + "\n" + systemPrompt);
        }

        // Create enhanced request
        ModelRequest enhancedRequest = ModelRequest.builder(request)
                .systemMessage(enhancedSystemMessage)
                .build();

        return handler.call(enhancedRequest);
    }

    @Override
    public String getName() {
        return "DynamicPromptNameInterceptor";
    }
}
