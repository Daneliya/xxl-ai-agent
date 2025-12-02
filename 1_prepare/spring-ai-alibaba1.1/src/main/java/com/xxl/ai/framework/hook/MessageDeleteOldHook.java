package com.xxl.ai.framework.hook;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.agent.hook.HookPosition;
import com.alibaba.cloud.ai.graph.agent.hook.ModelHook;
import com.alibaba.cloud.ai.graph.state.RemoveByHash;
import org.springframework.ai.chat.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 删除旧消息
 *
 * @Author xxl
 * @Date 2025/12/2 14:19
 */
public class MessageDeleteOldHook extends ModelHook {

    @Override
    public String getName() {
        return "delete_old_messages";
    }

    @Override
    public HookPosition[] getHookPositions() {
        return new HookPosition[]{HookPosition.AFTER_MODEL};
    }

    @Override
    public CompletableFuture<Map<String, Object>> afterModel(OverAllState state, RunnableConfig config) {
        Optional<Object> messagesOpt = state.value("messages");
        if (!messagesOpt.isPresent()) {
            return CompletableFuture.completedFuture(Map.of());
        }

        List<Message> messages = (List<Message>) messagesOpt.get();
        if (messages.size() > 2) {
            // 将最早的两条消息转为 RemoveByHash 对象以便从状态中删除
            List<Object> removeOldMessages = new ArrayList<>();
            removeOldMessages.add(RemoveByHash.of(messages.get(0)));
            removeOldMessages.add(RemoveByHash.of(messages.get(1)));
            return CompletableFuture.completedFuture(Map.of("messages", removeOldMessages));
        }

        return CompletableFuture.completedFuture(Map.of());
    }
}
