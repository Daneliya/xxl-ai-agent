/**
 * 操作日志业务服务接口
 * 定义操作日志相关的业务方法，包括增删改查等基本操作
 * 
 * @Author: xxl 
 * @Date: 2025/1/4 16:20
 */
package com.xxl.arms.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xxl.arms.dto.ArmsLogCreateDTO;
import com.xxl.arms.dto.ArmsLogQueryDTO;
import com.xxl.arms.dto.ArmsLogUpdateDTO;
import com.xxl.arms.entity.ArmsLog;

import java.util.List;
import java.util.Optional;

/**
 * 操作日志业务服务接口
 * 定义对操作日志数据的各种业务操作方法
 * 包括基本的CRUD操作以及高级查询功能
 *
 * @author xxl
 * @since 2025/1/4
 */
public interface ArmsLogService extends IService<ArmsLog> {

    /**
     * 缓存键前缀
     */
    String CACHE_KEY_ARMS_LOG = "arms:log:";

    /**
     * 缓存过期时间（秒）
     */
    long CACHE_EXPIRE_SECONDS = 300;

    /**
     * 创建新的操作日志记录
     *
     * @param createDTO 包含操作日志创建信息的DTO对象
     * @return 返回创建成功的操作日志对象，包含自动生成的主键ID
     * @throws RuntimeException 当创建操作失败时抛出异常
     */
    ArmsLog createArmsLog(ArmsLogCreateDTO createDTO);

    /**
     * 根据主键ID查找操作日志记录（带缓存）
     * 优先从Redis缓存中获取，缓存未命中时查询数据库并写入缓存
     *
     * @param id 操作日志的主键ID
     * @return 返回包含操作日志信息的Optional对象，如果记录不存在则返回空的Optional
     */
    Optional<ArmsLog> findById(Long id);

    /**
     * 根据主键ID查找操作日志记录（强制从数据库查询）
     * 用于刷新缓存或获取最新数据
     *
     * @param id 操作日志的主键ID
     * @return 返回包含操作日志信息的Optional对象，如果记录不存在则返回空的Optional
     */
    Optional<ArmsLog> findByIdFromDb(Long id);

    /**
     * 更新现有的操作日志记录
     *
     * @param updateDTO 包含操作日志更新信息的DTO对象，必须包含有效的主键ID
     * @return 返回更新后的操作日志对象
     * @throws RuntimeException 当要更新的记录不存在或更新失败时抛出异常
     */
    ArmsLog updateArmsLog(ArmsLogUpdateDTO updateDTO);

    /**
     * 根据主键ID删除操作日志记录
     *
     * @param id 操作日志的主键ID
     * @return 返回删除操作是否成功的布尔值，true表示删除成功，false表示记录不存在
     */
    boolean deleteById(Long id);

    /**
     * 根据查询条件分页获取操作日志记录
     *
     * @param queryDTO 包含分页和查询条件的DTO对象
     * @return 返回符合查询条件的分页结果，包含总记录数、当前页数据等
     */
    Page<ArmsLog> findArmsLogs(ArmsLogQueryDTO queryDTO);

    /**
     * 根据操作类型获取所有相关的操作日志记录
     *
     * @param operationType 操作类型，如：USER_LOGIN、DATA_EXPORT等
     * @return 返回符合操作类型条件的所有操作日志列表，按创建时间倒序排列
     */
    List<ArmsLog> findByOperationType(String operationType);

    /**
     * 根据用户ID获取该用户的所有操作日志记录
     *
     * @param userId 用户的唯一标识
     * @return 返回指定用户的所有操作日志列表，按创建时间倒序排列
     */
    List<ArmsLog> findByUserId(String userId);

    /**
     * 根据操作状态获取所有相关的操作日志记录
     *
     * @param status 操作状态，SUCCESS表示成功，FAILED表示失败
     * @return 返回符合状态条件的所有操作日志列表，按创建时间倒序排列
     */
    List<ArmsLog> findByStatus(String status);

    /**
     * 根据时间范围获取操作日志记录
     *
     * @param startTime 查询的开始时间
     * @param endTime 查询的结束时间
     * @return 返回指定时间范围内的所有操作日志列表，按创建时间倒序排列
     */
    List<ArmsLog> findByTimeRange(String startTime, String endTime);

    /**
     * 统计指定操作类型的记录数量
     *
     * @param operationType 操作类型
     * @return 返回指定操作类型的总记录数
     */
    Long countByOperationType(String operationType);

    /**
     * 统计指定状态的记录数量
     *
     * @param status 操作状态
     * @return 返回指定状态的总记录数
     */
    Long countByStatus(String status);
}