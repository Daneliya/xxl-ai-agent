-- 创建xxl_arms数据库
CREATE DATABASE IF NOT EXISTS xxl_arms DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE xxl_arms;

-- 创建arms_log表
DROP TABLE IF EXISTS `arms_log`;
CREATE TABLE `arms_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `operation_type` varchar(50) NOT NULL COMMENT '操作类型',
  `operation_desc` varchar(500) NOT NULL COMMENT '操作描述',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户ID',
  `request_params` text COMMENT '请求参数',
  `response_data` text COMMENT '响应数据',
  `execution_time` varchar(20) DEFAULT NULL COMMENT '执行时间',
  `status` varchar(20) NOT NULL DEFAULT 'SUCCESS' COMMENT '状态：SUCCESS/FAILED',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';