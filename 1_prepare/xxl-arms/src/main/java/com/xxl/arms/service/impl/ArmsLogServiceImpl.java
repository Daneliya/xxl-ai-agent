package com.xxl.arms.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xxl.arms.dto.ArmsLogCreateDTO;
import com.xxl.arms.dto.ArmsLogQueryDTO;
import com.xxl.arms.dto.ArmsLogUpdateDTO;
import com.xxl.arms.entity.ArmsLog;
import com.xxl.arms.mapper.ArmsLogMapper;
import com.xxl.arms.service.ArmsLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 操作日志业务服务实现类
 * 实现了ArmsLogService接口中定义的所有业务方法
 * 使用MyBatis-Flex进行数据访问，提供完整的CRUD操作和高级查询功能
 *
 * @author xxl
 * @since 2025/1/4
 */
@Slf4j
@Service
//@RequiredArgsConstructor
public class ArmsLogServiceImpl extends ServiceImpl<ArmsLogMapper, ArmsLog> implements ArmsLogService {

    @Autowired
    private ArmsLogMapper armsLogMapper;

    /**
     * Redis模板
     * 用于操作Redis缓存
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 构造方法
     *
     * @param redisTemplate Redis模板
     */
    public ArmsLogServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 创建新的操作日志记录
     *
     * @param createDTO 包含操作日志创建信息的DTO对象
     * @return 返回创建成功的操作日志对象，包含自动生成的主键ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArmsLog createArmsLog(ArmsLogCreateDTO createDTO) {
        log.info("开始创建操作日志，操作类型: {}", createDTO.getOperationType());

        ArmsLog armsLog = new ArmsLog();
        armsLog.setOperationType(createDTO.getOperationType());
        armsLog.setOperationDesc(createDTO.getOperationDesc());
        armsLog.setUserId(createDTO.getUserId());
        armsLog.setRequestParams(createDTO.getRequestParams());
        armsLog.setResponseData(createDTO.getResponseData());
        armsLog.setExecutionTime(createDTO.getExecutionTime());
        armsLog.setStatus(createDTO.getStatus() != null ? createDTO.getStatus() : "SUCCESS");
        armsLog.setCreatedTime(LocalDateTime.now());
        armsLog.setUpdatedTime(LocalDateTime.now());

        this.save(armsLog);
        log.info("操作日志创建成功，主键ID: {}", armsLog.getId());

        return armsLog;
    }

    /**
     * 根据主键ID查找操作日志记录
     *
     * @param id 操作日志的主键ID
     * @return 返回包含操作日志信息的Optional对象，如果记录不存在则返回空的Optional
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ArmsLog> findById(Long id) {
        log.debug("根据主键ID查找操作日志: {}", id);

        String cacheKey = CACHE_KEY_ARMS_LOG + id;

        try {
            // 1. 先从Redis缓存中查询
            Object cachedObj = redisTemplate.opsForValue().get(cacheKey);
            if (cachedObj != null) {
                log.debug("从Redis缓存中获取操作日志，ID: {}", id);
                if (cachedObj instanceof ArmsLog) {
                    return Optional.of((ArmsLog) cachedObj);
                }
            }
        } catch (Exception e) {
            log.warn("从Redis缓存获取操作日志失败，ID: {}, 错误: {}", id, e.getMessage());
        }

        // 2. 缓存未命中，从数据库查询
        log.debug("缓存未命中，从数据库查询操作日志，ID: {}", id);
        Optional<ArmsLog> result = findByIdFromDb(id);

        // 3. 将查询结果写入缓存
        result.ifPresent(armsLog -> {
            try {
                redisTemplate.opsForValue().set(cacheKey, armsLog, CACHE_EXPIRE_SECONDS, TimeUnit.SECONDS);
                log.debug("将操作日志写入Redis缓存，ID: {}", id);
            } catch (Exception e) {
                log.warn("将操作日志写入Redis缓存失败，ID: {}, 错误: {}", id, e.getMessage());
            }
        });

        return result;
    }

    /**
     * 根据主键ID查找操作日志记录（强制从数据库查询）
     *
     * @param id 操作日志的主键ID
     * @return 返回包含操作日志信息的Optional对象，如果记录不存在则返回空的Optional
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ArmsLog> findByIdFromDb(Long id) {
        log.debug("从数据库查询操作日志，ID: {}", id);
        ArmsLog armsLog = this.getById(id);
        return Optional.ofNullable(armsLog);
    }

    /**
     * 更新现有的操作日志记录
     *
     * @param updateDTO 包含操作日志更新信息的DTO对象，必须包含有效的主键ID
     * @return 返回更新后的操作日志对象
     * @throws RuntimeException 当要更新的记录不存在或更新失败时抛出异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArmsLog updateArmsLog(ArmsLogUpdateDTO updateDTO) {
        log.info("开始更新操作日志，主键ID: {}", updateDTO.getId());

        ArmsLog existingLog = this.getById(updateDTO.getId());
        if (existingLog == null) {
            log.error("要更新的操作日志不存在，主键ID: {}", updateDTO.getId());
            throw new RuntimeException("操作日志不存在，主键ID: " + updateDTO.getId());
        }

        existingLog.setOperationType(updateDTO.getOperationType());
        existingLog.setOperationDesc(updateDTO.getOperationDesc());
        existingLog.setUserId(updateDTO.getUserId());
        existingLog.setRequestParams(updateDTO.getRequestParams());
        existingLog.setResponseData(updateDTO.getResponseData());
        existingLog.setExecutionTime(updateDTO.getExecutionTime());
        existingLog.setStatus(updateDTO.getStatus());
        existingLog.setUpdatedTime(LocalDateTime.now());

        boolean updated = this.updateById(existingLog);
        if (!updated) {
            log.error("更新操作日志失败，主键ID: {}", updateDTO.getId());
            throw new RuntimeException("更新操作日志失败，主键ID: " + updateDTO.getId());
        }

        // 清除缓存
        String cacheKey = CACHE_KEY_ARMS_LOG + updateDTO.getId();
        try {
            redisTemplate.delete(cacheKey);
            log.debug("清除操作日志缓存，ID: {}", updateDTO.getId());
        } catch (Exception e) {
            log.warn("清除操作日志缓存失败，ID: {}, 错误: {}", updateDTO.getId(), e.getMessage());
        }

        log.info("操作日志更新成功，主键ID: {}", updateDTO.getId());
        return existingLog;
    }

    /**
     * 根据主键ID删除操作日志记录
     *
     * @param id 操作日志的主键ID
     * @return 返回删除操作是否成功的布尔值，true表示删除成功，false表示记录不存在
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        log.info("开始删除操作日志，主键ID: {}", id);

        ArmsLog existingLog = this.getById(id);
        if (existingLog == null) {
            log.warn("要删除的操作日志不存在，主键ID: {}", id);
            return false;
        }

        boolean deleted = this.removeById(id);
        if (deleted) {
            // 清除缓存
            String cacheKey = CACHE_KEY_ARMS_LOG + id;
            try {
                redisTemplate.delete(cacheKey);
                log.debug("清除操作日志缓存，ID: {}", id);
            } catch (Exception e) {
                log.warn("清除操作日志缓存失败，ID: {}, 错误: {}", id, e.getMessage());
            }
            log.info("操作日志删除成功，主键ID: {}", id);
            return true;
        } else {
            log.error("删除操作日志失败，主键ID: {}", id);
            return false;
        }
    }

    /**
     * 根据查询条件分页获取操作日志记录
     *
     * @param queryDTO 包含分页和查询条件的DTO对象
     * @return 返回符合查询条件的分页结果，包含总记录数、当前页数据等
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ArmsLog> findArmsLogs(ArmsLogQueryDTO queryDTO) {
        log.debug("开始分页查询操作日志，查询条件: {}", queryDTO);

        Page<ArmsLog> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());

        try {
            // 解析时间参数
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            if (queryDTO.getStartTime() != null && !queryDTO.getStartTime().trim().isEmpty()) {
                startTime = LocalDateTime.parse(queryDTO.getStartTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
            if (queryDTO.getEndTime() != null && !queryDTO.getEndTime().trim().isEmpty()) {
                endTime = LocalDateTime.parse(queryDTO.getEndTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
//            QueryWrapper queryWrapper = QueryWrapper.create()
//                    .where(ArmsLog::getUserId).eq(queryDTO.getUserId())
//                    .and(ArmsLog::getOperationType).eq(queryDTO.getOperationType())
//                    .and(ArmsLog::getStatus).eq(queryDTO.getStatus())
//                    .and(ArmsLog::getCreatedTime).ge(startTime)
//                    .and(ArmsLog::getCreatedTime).le(endTime);
            // 构建查询条件
            QueryWrapper queryWrapper = QueryWrapper.create();
            // 1. 用户ID（Integer/Long等引用类型，判断 != null）
            if (queryDTO.getUserId() != null) {
                queryWrapper.where(ArmsLog::getUserId).eq(queryDTO.getUserId());
            }
            // 2. 操作类型（若为字符串类型，用 StringUtils.isNotBlank() 排除 null/空字符串/空格）
            if (queryDTO.getOperationType() != null) {
                queryWrapper.and(ArmsLog::getOperationType).eq(queryDTO.getOperationType());
            }
            // 3. 状态（Integer/Long等引用类型，判断 != null）
            if (queryDTO.getStatus() != null) {
                queryWrapper.and(ArmsLog::getStatus).eq(queryDTO.getStatus());
            }
            // 4. 创建时间起始值（Date/LocalDateTime等引用类型，判断 != null）
            if (startTime != null) {
                queryWrapper.and(ArmsLog::getCreatedTime).ge(startTime);
            }
            // 5. 创建时间结束值（Date/LocalDateTime等引用类型，判断 != null）
            if (endTime != null) {
                queryWrapper.and(ArmsLog::getCreatedTime).le(endTime);
            }
            Page<ArmsLog> pageResult = this.page(page, queryWrapper);

            log.debug("分页查询完成，总记录数: {}, 当前页大小: {}", pageResult.getPageNumber(), pageResult.getTotalRow());
            return pageResult;
        } catch (Exception e) {
            log.error("分页查询操作日志失败，查询条件: {}", queryDTO, e);
            throw new RuntimeException("分页查询操作日志失败: " + e.getMessage(), e);
        }
    }

    /**
     * 根据操作类型获取所有相关的操作日志记录
     *
     * @param operationType 操作类型，如：USER_LOGIN、DATA_EXPORT等
     * @return 返回符合操作类型条件的所有操作日志列表，按创建时间倒序排列
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArmsLog> findByOperationType(String operationType) {
        log.debug("根据操作类型查询日志，操作类型: {}", operationType);
        // 使用Mapper进行自定义查询
        List<ArmsLog> result = armsLogMapper.findByOperationType(operationType);
        log.debug("操作类型查询完成，结果数量: {}", result.size());
        return result;
    }

    /**
     * 根据用户ID获取该用户的所有操作日志记录
     *
     * @param userId 用户的唯一标识
     * @return 返回指定用户的所有操作日志列表，按创建时间倒序排列
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArmsLog> findByUserId(String userId) {
        log.debug("根据用户ID查询日志，用户ID: {}", userId);
        // 使用Mapper进行自定义查询
        List<ArmsLog> result = armsLogMapper.findByUserId(userId);
        log.debug("用户ID查询完成，结果数量: {}", result.size());
        return result;
    }

    /**
     * 根据操作状态获取所有相关的操作日志记录
     *
     * @param status 操作状态，SUCCESS表示成功，FAILED表示失败
     * @return 返回符合状态条件的所有操作日志列表，按创建时间倒序排列
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArmsLog> findByStatus(String status) {
        log.debug("根据状态查询日志，状态: {}", status);
        // 使用Mapper进行自定义查询
        List<ArmsLog> result = armsLogMapper.findByStatus(status);
        log.debug("状态查询完成，结果数量: {}", result.size());
        return result;
    }

    /**
     * 根据时间范围获取操作日志记录
     *
     * @param startTime 查询的开始时间，格式: yyyy-MM-dd HH:mm:ss
     * @param endTime   查询的结束时间，格式: yyyy-MM-dd HH:mm:ss
     * @return 返回指定时间范围内的所有操作日志列表，按创建时间倒序排列
     */
    @Override
    @Transactional(readOnly = true)
    public List<ArmsLog> findByTimeRange(String startTime, String endTime) {
        log.debug("根据时间范围查询日志，开始时间: {}, 结束时间: {}", startTime, endTime);

        try {
            LocalDateTime start = LocalDateTime.parse(startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime end = LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // 使用Mapper进行自定义查询
            List<ArmsLog> result = armsLogMapper.findByCreatedTimeBetween(start, end);
            log.debug("时间范围查询完成，结果数量: {}", result.size());
            return result;
        } catch (Exception e) {
            log.error("时间范围查询操作日志失败，开始时间: {}, 结束时间: {}", startTime, endTime, e);
            throw new RuntimeException("时间范围查询操作日志失败: " + e.getMessage(), e);
        }
    }

    /**
     * 统计指定操作类型的记录数量
     *
     * @param operationType 操作类型
     * @return 返回指定操作类型的总记录数
     */
    @Override
    @Transactional(readOnly = true)
    public Long countByOperationType(String operationType) {
        log.debug("统计操作类型记录数量，操作类型: {}", operationType);
        // 使用Mapper进行自定义查询
        Long count = armsLogMapper.countByOperationType(operationType);
        log.debug("操作类型统计完成，数量: {}", count);
        return count;
    }

    /**
     * 统计指定状态的记录数量
     *
     * @param status 操作状态
     * @return 返回指定状态的总记录数
     */
    @Override
    @Transactional(readOnly = true)
    public Long countByStatus(String status) {
        log.debug("统计状态记录数量，状态: {}", status);
        // 使用Mapper进行自定义查询
        Long count = armsLogMapper.countByStatus(status);
        log.debug("状态统计完成，数量: {}", count);
        return count;
    }
}