package com.xxl.ai.framework.hook;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.hook.HookPosition;
import com.alibaba.cloud.ai.graph.agent.hook.ModelHook;
import org.springframework.ai.chat.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 修剪消息
 *
 * @Author xxl
 * @Date 2025/12/2 13:43
 */
public class MessageTrimmingHook extends ModelHook {

    private static final int MAX_MESSAGES = 3;

    @Override
    public String getName() {
        return "message_trimming";
    }

    @Override
    public HookPosition[] getHookPositions() {
        return new HookPosition[]{HookPosition.BEFORE_MODEL};
    }

    @Override
    public CompletableFuture<Map<String, Object>> beforeModel(OverAllState state, RunnableConfig config) {
        Optional<Object> messagesOpt = state.value("messages");
        if (!messagesOpt.isPresent()) {
            return CompletableFuture.completedFuture(Map.of());
        }

        List<Message> messages = (List<Message>) messagesOpt.get();

        if (messages.size() <= MAX_MESSAGES) {
            return CompletableFuture.completedFuture(Map.of()); // 无需更改
        }

        // 保留第一条消息和最后几条消息，并将中间消息标记为删除
        Message firstMsg = messages.get(0);
        int keepCount = messages.size() % 2 == 0 ? 3 : 4;
        List<Message> recentMessages = messages.subList(
                messages.size() - keepCount,
                messages.size()
        );

        List<Object> newMessages = new ArrayList<>();
        // 标记中间消息为删除（使用 RemoveByHash）
        if (messages.size() - keepCount > 1) {
            for (Message msg : messages.subList(1, messages.size() - keepCount)) {
                newMessages.add(com.alibaba.cloud.ai.graph.state.RemoveByHash.of(msg));
            }
        }

        return CompletableFuture.completedFuture(Map.of("messages", newMessages));
    }

    @Override
    public CompletableFuture<Map<String, Object>> afterModel(OverAllState state, RunnableConfig config) {
        return CompletableFuture.completedFuture(Map.of());
    }
}
