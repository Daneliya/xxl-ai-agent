package com.xxl.ai.framework.hook;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.hook.HookPosition;
import com.alibaba.cloud.ai.graph.agent.hook.ModelHook;
import org.springframework.messaging.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @Classname MessageTrimHook
 * @Description MessageTrimHook Before Model 示例
 * @Date 2025/12/7 20:25
 * @Created by xxl
 */
public class MessageTrimHook extends ModelHook {

    @Override
    public String getName() {
        return "trim_messages";
    }

    @Override
    public HookPosition[] getHookPositions() {
        return new HookPosition[]{HookPosition.BEFORE_MODEL};
    }

    @Override
    public CompletableFuture<Map<String, Object>> beforeModel(OverAllState state, RunnableConfig config) {
        // 访问和修改消息
        Optional<Object> messagesOpt = state.value("messages");
        if (messagesOpt.isPresent()) {
            List<Message> messages = (List<Message>) messagesOpt.get();

            if (messages.size() <= 3) {
                return CompletableFuture.completedFuture(Map.of()); // 无需更改
            }

            // 保留第一条和最后几条消息，并将中间消息标记为删除
            Message firstMsg = messages.get(0);
            List<Message> recentMessages = messages.subList(
                    messages.size() - 3,
                    messages.size()
            );

            List<Object> newMessages = new ArrayList<>();
            newMessages.add(firstMsg);
            newMessages.addAll(recentMessages);
            // 标记中间消息为删除（使用 RemoveByHash）
            if (messages.size() - 3 > 1) {
                for (Message msg : messages.subList(1, messages.size() - 3)) {
                    newMessages.add(com.alibaba.cloud.ai.graph.state.RemoveByHash.of(msg));
                }
            }

            return CompletableFuture.completedFuture(Map.of("messages", newMessages));
        }

        return CompletableFuture.completedFuture(Map.of());
    }

    @Override
    public CompletableFuture<Map<String, Object>> afterModel(OverAllState state, RunnableConfig config) {
        return CompletableFuture.completedFuture(Map.of());
    }
}