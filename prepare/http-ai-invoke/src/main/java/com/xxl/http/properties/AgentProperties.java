package com.xxl.http.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 配置信息
 *
 * @Author: xxl
 * @Date: 2024/01/22  9:52
 */
@Data
@Component
@ConfigurationProperties(prefix = "agent-key")
public class AgentProperties {

    /**
     * 阿里百炼平台key
     */
    private String apiKey;

}
