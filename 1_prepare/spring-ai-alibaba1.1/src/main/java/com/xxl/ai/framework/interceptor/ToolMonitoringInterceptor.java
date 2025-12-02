package com.xxl.ai.framework.interceptor;

import com.alibaba.cloud.ai.graph.agent.interceptor.ToolCallHandler;
import com.alibaba.cloud.ai.graph.agent.interceptor.ToolCallRequest;
import com.alibaba.cloud.ai.graph.agent.interceptor.ToolCallResponse;
import com.alibaba.cloud.ai.graph.agent.interceptor.ToolInterceptor;

/**
 * 监控和错误处理
 *
 * @Author xxl
 * @Date 2025/12/1 08:42
 */
public class ToolMonitoringInterceptor extends ToolInterceptor {

    @Override
    public ToolCallResponse interceptToolCall(ToolCallRequest request, ToolCallHandler handler) {
        long startTime = System.currentTimeMillis();
        try {
            ToolCallResponse response = handler.call(request);
            logSuccess(request, System.currentTimeMillis() - startTime);
            return response;
        } catch (Exception e) {
            logError(request, e, System.currentTimeMillis() - startTime);
            return ToolCallResponse.of(request.getToolCallId(), request.getToolName(),
                    "工具执行遇到问题，请稍后重试");
        }
    }

    private void logSuccess(ToolCallRequest request, long duration) {
        System.out.println("Tool " + request.getToolName() + " succeeded in " + duration + "ms");
    }

    private void logError(ToolCallRequest request, Exception e, long duration) {
        System.err.println("Tool " + request.getToolName() + " failed in " + duration + "ms: " + e.getMessage());
    }

    @Override
    public String getName() {
        return "ToolMonitoringInterceptor";
    }
}
