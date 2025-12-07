package com.xxl.ai.framework.hook;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.hook.HookPosition;
import com.alibaba.cloud.ai.graph.agent.hook.ModelHook;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @Classname ValidateResponseHook
 * @Description ValidateResponseHook After Model 敏感词工具
 * @Date 2025/12/7 20:16
 * @Created by xxl
 */
public class ValidateResponseHook extends ModelHook {

    private static final List<String> STOP_WORDS =
            List.of("password", "secret", "api_key");

    @Override
    public String getName() {
        return "validate_response";
    }

    @Override
    public HookPosition[] getHookPositions() {
        return new HookPosition[]{HookPosition.AFTER_MODEL};
    }

    @Override
    public CompletableFuture<Map<String, Object>> beforeModel(OverAllState state, RunnableConfig config) {
        return CompletableFuture.completedFuture(Map.of());
    }

    @Override
    public CompletableFuture<Map<String, Object>> afterModel(OverAllState state, RunnableConfig config) {
        Optional<Object> messagesOpt = state.value("messages");
        if (!messagesOpt.isPresent()) {
            return CompletableFuture.completedFuture(Map.of());
        }

        List<Message> messages = (List<Message>) messagesOpt.get();
        if (messages.isEmpty()) {
            return CompletableFuture.completedFuture(Map.of());
        }

        Message lastMessage = messages.get(messages.size() - 1);
        String content = lastMessage.getText();

        // 检查是否包含敏感词
        for (String stopWord : STOP_WORDS) {
            if (content.toLowerCase().contains(stopWord)) {
                // 移除包含敏感词的消息
                List<Message> filtered = messages.subList(0, messages.size() - 1);
                filtered.add(new AssistantMessage("\n抱歉，我无法提供该信息。\n"));
                return CompletableFuture.completedFuture(Map.of("messages", filtered));
            }
        }

        return CompletableFuture.completedFuture(Map.of());
    }
}
