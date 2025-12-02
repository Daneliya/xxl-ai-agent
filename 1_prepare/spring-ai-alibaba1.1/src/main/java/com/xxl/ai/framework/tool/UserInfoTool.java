package com.xxl.ai.framework.tool;

import com.alibaba.cloud.ai.graph.RunnableConfig;
import org.springframework.ai.chat.model.ToolContext;

import java.util.function.BiFunction;

/**
 * 用户信息记忆
 *
 * @Author xxl
 * @Date 2025/12/2 17:18
 */
public class UserInfoTool implements BiFunction<String, ToolContext, String> {

    @Override
    public String apply(String query, ToolContext toolContext) {
        // 从上下文中获取用户信息
        RunnableConfig config = (RunnableConfig) toolContext.getContext().get("config");
        String userId = (String) config.metadata("user_id").orElse("");

        if ("user_123".equals(userId)) {
            return "用户是 John Smith";
        } else {
            return "未知用户";
        }
    }
}
