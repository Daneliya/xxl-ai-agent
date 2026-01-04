/**
 * 操作日志实体类
 * 用于记录系统中的所有操作日志信息
 *
 * @Author: xxl
 * @Date: 2025/1/4 16:20
 */
package com.xxl.arms.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 映射数据库中的arms_log表，用于存储操作日志信息
 * 包括操作类型、操作描述、用户ID、请求参数、响应数据、执行时间、状态等字段
 *
 * @author xxl
 * @since 2025/1/4
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table("arms_log")
public class ArmsLog {

    /**
     * 主键ID
     * 自增长主键，用于唯一标识每条操作日志记录
     */
    @Id(keyType = KeyType.Auto)
    private Long id;

    /**
     * 操作类型
     * 用于标识操作的类型，如：USER_LOGIN、DATA_EXPORT、SYSTEM_CONFIG等
     * 长度限制为50字符
     */
    private String operationType;

    /**
     * 操作描述
     * 详细描述当前操作的具体内容
     * 长度限制为500字符
     */
    private String operationDesc;

    /**
     * 用户ID
     * 执行当前操作用户的唯一标识
     * 可以为空，表示系统级操作
     */
    private String userId;

    /**
     * 请求参数
     * 当前操作的请求参数，以JSON格式存储
     * 用于记录调用接口时传入的参数
     */
    private String requestParams;

    /**
     * 响应数据
     * 当前操作的响应结果，以JSON格式存储
     * 用于记录接口返回的数据
     */
    private String responseData;

    /**
     * 执行时间
     * 当前操作的执行耗时，如：100ms、2s等
     * 用于性能监控和分析
     */
    private String executionTime;

    /**
     * 状态
     * 当前操作执行的状态，SUCCESS表示成功，FAILED表示失败
     * 长度限制为20字符
     */
    private String status;

    /**
     * 创建时间
     * 记录当前日志的创建时间
     * 由数据库自动维护
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     * 记录当前日志的最后修改时间
     * 由数据库自动维护
     */
    private LocalDateTime updatedTime;
}