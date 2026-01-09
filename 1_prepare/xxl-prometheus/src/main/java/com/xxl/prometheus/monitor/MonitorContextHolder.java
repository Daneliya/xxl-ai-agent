package com.xxl.prometheus.monitor;

import lombok.extern.slf4j.Slf4j;

/**
 * 上‍下文持有者，提供 ThreadLocal 的读、写、清除方法
 *
 * @author xxl
 * @date 2026/1/9 13:27
 */
@Slf4j
public class MonitorContextHolder {

    private static final ThreadLocal<MonitorContext> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置监控上下文
     */
    public static void setContext(MonitorContext context) {
        CONTEXT_HOLDER.set(context);
    }

    /**
     * 获取当前监控上下文
     */
    public static MonitorContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除监控上下文
     */
    public static void clearContext() {
        CONTEXT_HOLDER.remove();
    }
}

