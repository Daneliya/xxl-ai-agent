package com.xxl.ai.framework.tool;

import org.springframework.ai.chat.model.ToolContext;

import java.util.function.BiFunction;

/**
 * 搜索工具
 *
 * @Classname SearchTool
 * @Description TODO
 * @Date 2025/11/29 23:55
 * @Created by xxl
 */
public class SearchTool implements BiFunction<String, ToolContext, String> {

    @Override
    public String apply(String query, ToolContext context) {
        // 实现搜索逻辑
        return "搜索结果: " + query;
    }

}
