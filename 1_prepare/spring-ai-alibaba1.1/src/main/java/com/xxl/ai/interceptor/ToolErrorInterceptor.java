package com.xxl.ai.interceptor;

import com.alibaba.cloud.ai.graph.agent.interceptor.ToolCallHandler;
import com.alibaba.cloud.ai.graph.agent.interceptor.ToolCallRequest;
import com.alibaba.cloud.ai.graph.agent.interceptor.ToolCallResponse;
import com.alibaba.cloud.ai.graph.agent.interceptor.ToolInterceptor;

/**
 * 工具错误处理
 *
 * @Classname ToolErrorInterceptor
 * @Description TODO
 * @Date 2025/11/29 23:42
 * @Created by xxl
 */
public class ToolErrorInterceptor extends ToolInterceptor {

    @Override
    public ToolCallResponse interceptToolCall(ToolCallRequest request, ToolCallHandler handler) {
        try {
            return handler.call(request);
        } catch (Exception e) {
            return ToolCallResponse.of(request.getToolCallId(), request.getToolName(),
                    "Tool failed: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "ToolErrorInterceptor";
    }
}
