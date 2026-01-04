package com.xxl.arms.dto;

import lombok.Data;

/**
 * 操作日志创建数据传输对象
 * 用于创建新的操作日志记录时封装请求参数
 * 包含创建操作日志所需的全部字段信息
 *
 * @Author: xxl
 * @Date: 2025/1/4 16:20
 */
@Data
public class ArmsLogCreateDTO {

    /**
     * 操作类型
     * 用于标识操作的类型，如：USER_LOGIN、DATA_EXPORT、SYSTEM_CONFIG等
     */
    private String operationType;

    /**
     * 操作描述
     * 详细描述当前操作的具体内容
     */
    private String operationDesc;

    /**
     * 用户ID
     * 执行当前操作的用户唯一标识符
     */
    private String userId;

    /**
     * 请求参数
     * 以JSON格式存储的操作请求参数
     */
    private String requestParams;

    /**
     * 响应数据
     * 以JSON格式存储的操作响应结果数据
     */
    private String responseData;

    /**
     * 执行时间
     * 记录操作执行的耗时时间，如：100ms、2s等
     */
    private String executionTime;

    /**
     * 状态
     * 操作执行状态，如：SUCCESS、FAILED、PENDING等
     */
    private String status;
}