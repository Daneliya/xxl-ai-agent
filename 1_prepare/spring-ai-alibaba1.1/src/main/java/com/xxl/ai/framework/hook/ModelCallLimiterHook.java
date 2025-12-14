package com.xxl.ai.framework.hook;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.hook.HookPosition;
import com.alibaba.cloud.ai.graph.agent.hook.HookPositions;
import com.alibaba.cloud.ai.graph.agent.hook.JumpTo;
import com.alibaba.cloud.ai.graph.agent.hook.ModelHook;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @Classname ModelCallLimiterHook
 * @Description ModelCallLimiterHook 调用次数限制
 * @Date 2025/12/14 22:47
 * @Created by xxl
 */
@HookPositions({HookPosition.BEFORE_MODEL, HookPosition.AFTER_MODEL})
public class ModelCallLimiterHook extends ModelHook {

    private static final String CALL_COUNT_KEY = "__model_call_count__";
    private final int maxCalls;

    public ModelCallLimiterHook(int maxCalls) {
        this.maxCalls = maxCalls;
    }

    @Override
    public String getName() {
        return "model_call_limiter";
    }

    @Override
    public CompletableFuture<Map<String, Object>> beforeModel(OverAllState state, RunnableConfig config) {
        // 读取当前调用次数
        int callCount = config.context().containsKey(CALL_COUNT_KEY)
                ? (int) config.context().get(CALL_COUNT_KEY) : 0;

        // 检查是否超过限制
        if (callCount >= maxCalls) {
            System.out.println("达到模型调用次数限制: " + maxCalls);

            // 添加终止消息
            List<Message> messages = new ArrayList<>(
                    (List<Message>) state.value("messages").orElse(new ArrayList<>())
            );
            messages.add(new AssistantMessage("已达到模型调用次数限制 (" + callCount + "/" + maxCalls + ")，Agent 执行终止。"));

            // 返回更新并跳转到结束
            return CompletableFuture.completedFuture(Map.of("messages", messages));
        }

        return CompletableFuture.completedFuture(Map.of());
    }

    @Override
    public CompletableFuture<Map<String, Object>> afterModel(OverAllState state, RunnableConfig config) {
        // 递增计数器
        int callCount = config.context().containsKey(CALL_COUNT_KEY)
                ? (int) config.context().get(CALL_COUNT_KEY) : 0;
        config.context().put(CALL_COUNT_KEY, callCount + 1);

        return CompletableFuture.completedFuture(Map.of());
    }

    @Override
    public List<JumpTo> canJumpTo() {
        return List.of(JumpTo.end);
    }
}