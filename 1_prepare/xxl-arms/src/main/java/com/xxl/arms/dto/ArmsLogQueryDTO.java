package com.xxl.arms.dto;

import lombok.Data;

/**
 * 操作日志查询数据传输对象
 * 用于查询操作日志记录时封装查询条件和分页参数
 * 包含所有可能的查询条件字段和分页控制参数
 * 
 * @Author: xxl 
 * @Date: 2025/1/4 16:20
 */
@Data
public class ArmsLogQueryDTO {
    
    /**
     * 主键ID
     * 需要查询的特定操作日志记录的唯一标识符
     */
    private Long id;
    
    /**
     * 操作类型
     * 需要查询的操作日志的操作类型，如：USER_LOGIN、DATA_EXPORT等
     */
    private String operationType;
    
    /**
     * 用户ID
     * 需要查询的操作日志的用户ID
     */
    private String userId;
    
    /**
     * 状态
     * 需要查询的操作日志的状态，如：SUCCESS、FAILED等
     */
    private String status;
    
    /**
     * 开始时间
     * 查询时间范围的下限，格式为：yyyy-MM-dd HH:mm:ss
     */
    private String startTime;
    
    /**
     * 结束时间
     * 查询时间范围的上限，格式为：yyyy-MM-dd HH:mm:ss
     */
    private String endTime;
    
    /**
     * 页码
     * 分页查询的当前页码，默认从第1页开始
     */
    private Integer page = 1;
    
    /**
     * 页面大小
     * 分页查询的每页记录数，默认为10条
     */
    private Integer size = 10;
}