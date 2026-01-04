package com.xxl.arms.config;

import com.mybatisflex.core.audit.AuditManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname MyBatisFlexConfig
 * @Description TODO
 * @Date 2026/1/5 00:20
 * @Created by xxl
 */
@Configuration
public class MyBatisFlexConfig {

    private static final Logger logger = LoggerFactory.getLogger("mybatis-flex-sql");

    public MyBatisFlexConfig() {
        // 开启审计功能
        AuditManager.setAuditEnable(true);

        // 打印SQL字符串
        // 设置SQL审计收集器
        AuditManager.setMessageCollector(auditMessage -> {
//                    logger.info("【执行SQL】耗时: {}ms, SQL语句:\n{}", auditMessage.getElapsedTime(), auditMessage.getFullSql());
                    logger.info("【执行SQL】耗时: {}ms, SQL语句: {}", auditMessage.getElapsedTime(), auditMessage.getFullSql());
                }
        );
    }
}
