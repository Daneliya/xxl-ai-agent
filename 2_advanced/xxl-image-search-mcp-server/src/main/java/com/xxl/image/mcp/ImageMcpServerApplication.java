package com.xxl.image.mcp;

import com.xxl.image.mcp.tool.ImageSearchTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 启动类
 *
 * @author xxl
 * @date 2026/2/10 16:28
 */
@SpringBootApplication
public class ImageMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageMcpServerApplication.class, args);
    }

    /**
     * 注册工具
     *
     * @param imageSearchTool
     * @return
     */
    @Bean
    public ToolCallbackProvider imageSearchTools(ImageSearchTool imageSearchTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(imageSearchTool)
                .build();
    }
}
