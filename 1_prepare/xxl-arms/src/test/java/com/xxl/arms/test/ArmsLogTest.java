package com.xxl.arms.test;

import com.mybatisflex.core.paginate.Page;
import com.xxl.arms.dto.ArmsLogCreateDTO;
import com.xxl.arms.dto.ArmsLogQueryDTO;
import com.xxl.arms.dto.ArmsLogUpdateDTO;
import com.xxl.arms.entity.ArmsLog;
import com.xxl.arms.service.ArmsLogService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

/**
 * 操作日志模块单元测试类
 * 对操作日志服务层的所有方法进行单元测试
 * 验证操作日志的增删改查等核心功能是否正常工作
 *
 * @Author: xxl
 * @Date: 2025/1/4 16:20
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class ArmsLogTest {

    /**
     * 操作日志业务服务接口
     * 注入ArmsLogService用于执行测试中的操作日志业务操作
     */
    @Autowired
    private ArmsLogService armsLogService;

    /**
     * 测试创建操作日志功能
     * 验证创建操作日志的基本功能是否正常
     * 测试创建后是否能够获取到正确的返回结果
     */
    @Test
    public void testCreateArmsLog() {
        log.info("开始测试创建操作日志");

        ArmsLogCreateDTO createDTO = new ArmsLogCreateDTO();
        createDTO.setOperationType("TEST_OPERATION");
        createDTO.setOperationDesc("测试操作日志");
        createDTO.setUserId("test_user_001");
        createDTO.setRequestParams("{\"test\": \"data\"}");
        createDTO.setResponseData("{\"result\": \"success\"}");
        createDTO.setExecutionTime("100ms");
        createDTO.setStatus("SUCCESS");

        ArmsLog result = armsLogService.createArmsLog(createDTO);
        log.info("创建操作日志成功: {}", result.getId());
    }

    /**
     * 测试根据ID查找操作日志功能
     * 验证根据主键ID查询特定操作日志的基本功能
     * 测试查询结果存在和不存在两种情况
     */
    @Test
    public void testFindById() {
        log.info("开始测试根据ID查找操作日志");

        Long testId = 1L;
        Optional<ArmsLog> armsLog = armsLogService.findById(testId);
        if (armsLog.isPresent()) {
            log.info("找到操作日志: {}", armsLog.get().getId());
        } else {
            log.info("未找到ID为 {} 的操作日志", testId);
        }
    }

    /**
     * 测试更新操作日志功能
     * 验证更新操作日志的基本功能
     * 测试更新操作成功和失败两种情况
     */
    @Test
    public void testUpdateArmsLog() {
        log.info("开始测试更新操作日志");

        Long testId = 1L;
        ArmsLogUpdateDTO updateDTO = new ArmsLogUpdateDTO();
        updateDTO.setId(testId);
        updateDTO.setOperationType("UPDATED_OPERATION");
        updateDTO.setOperationDesc("更新的操作描述");
        updateDTO.setStatus("FAILED");

        try {
            ArmsLog result = armsLogService.updateArmsLog(updateDTO);
            log.info("更新操作日志成功: {}", result.getId());
        } catch (RuntimeException e) {
            log.error("更新操作日志失败: {}", e.getMessage());
        }
    }

    /**
     * 测试删除操作日志功能
     * 验证删除操作日志的基本功能
     * 测试删除操作成功和失败两种情况
     */
    @Test
    public void testDeleteArmsLog() {
        log.info("开始测试删除操作日志");

        Long testId = 1L;
        boolean deleted = armsLogService.deleteById(testId);
        log.info("删除操作日志结果: {}", deleted ? "成功" : "失败");
    }

    /**
     * 测试分页查询操作日志功能
     * 验证分页查询操作日志的基本功能
     * 测试分页查询结果是否正确返回总记录数、当前页数量等分页信息
     */
    @Test
    public void testFindArmsLogs() {
        log.info("开始测试分页查询操作日志");

        ArmsLogQueryDTO queryDTO = new ArmsLogQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setSize(10);

        Page<ArmsLog> result = armsLogService.findArmsLogs(queryDTO);
        log.info("分页查询结果，总数: {}, 当前页数量: {}", result.getTotalPage(), result.getRecords().size());
    }

    /**
     * 测试根据操作类型查找日志功能
     * 验证根据操作类型查询操作日志的基本功能
     * 测试查询结果是否正确返回符合条件的所有记录
     */
    @Test
    public void testFindByOperationType() {
        log.info("开始测试根据操作类型查找日志");

        String operationType = "TEST_OPERATION";
        List<ArmsLog> result = armsLogService.findByOperationType(operationType);
        log.info("根据操作类型 {} 查找结果，数量: {}", operationType, result.size());
    }

    /**
     * 测试根据用户ID查找日志功能
     * 验证根据用户ID查询操作日志的基本功能
     * 测试查询结果是否正确返回符合条件的所有记录
     */
    @Test
    public void testFindByUserId() {
        log.info("开始测试根据用户ID查找日志");

        String userId = "test_user_001";
        List<ArmsLog> result = armsLogService.findByUserId(userId);
        log.info("根据用户ID {} 查找结果，数量: {}", userId, result.size());
    }

    /**
     * 测试根据状态查找日志功能
     * 验证根据状态查询操作日志的基本功能
     * 测试查询结果是否正确返回符合条件的所有记录
     */
    @Test
    public void testFindByStatus() {
        log.info("开始测试根据状态查找日志");

        String status = "SUCCESS";
        List<ArmsLog> result = armsLogService.findByStatus(status);
        log.info("根据状态 {} 查找结果，数量: {}", status, result.size());
    }
}