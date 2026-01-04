package com.xxl.arms.controller;

import com.mybatisflex.core.paginate.Page;
import com.xxl.arms.dto.ArmsLogCreateDTO;
import com.xxl.arms.dto.ArmsLogQueryDTO;
import com.xxl.arms.dto.ArmsLogUpdateDTO;
import com.xxl.arms.entity.ArmsLog;
import com.xxl.arms.service.ArmsLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 操作日志控制器类
 * 处理所有与操作日志相关的HTTP请求，提供RESTful API接口
 * 支持操作日志的创建、查询、更新和删除等操作
 *
 * @Author: xxl
 * @Date: 2025/1/4 16:20
 */
@Slf4j
@RestController
@RequestMapping("/api/arms-log")
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
public class ArmsLogController {

    /**
     * 操作日志业务服务接口
     * 注入ArmsLogService用于执行操作日志相关的业务操作
     */
    @Autowired
    private ArmsLogService armsLogService;

    /**
     * 创建新的操作日志记录
     * 接收客户端提交的操作日志创建请求，将数据传递给业务服务层进行处理
     *
     * @param createDTO 包含操作日志创建信息的DTO对象
     * @return 返回创建成功的操作日志对象，包含自动生成的主键ID
     */
    @PostMapping
    public ResponseEntity<ArmsLog> createArmsLog(@RequestBody ArmsLogCreateDTO createDTO) {
        log.info("收到创建操作日志请求: {}", createDTO);
        ArmsLog result = armsLogService.createArmsLog(createDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据主键ID查找操作日志记录
     * 接收客户端通过ID查询特定操作日志的请求
     *
     * @param id 操作日志的主键ID
     * @return 返回包含操作日志信息的响应对象，如果记录不存在则返回404状态码
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArmsLog> getArmsLogById(@PathVariable Long id) {
        log.info("收到查找操作日志请求，ID: {}", id);
        Optional<ArmsLog> armsLog = armsLogService.findById(id);
        return armsLog.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 更新操作日志记录
     * 接收客户端提交的操作日志更新请求，将数据传递给业务服务层进行处理
     *
     * @param updateDTO 包含操作日志更新信息的DTO对象
     * @return 返回更新成功的操作日志对象
     */
    @PutMapping
    public ResponseEntity<ArmsLog> updateArmsLog(@RequestBody ArmsLogUpdateDTO updateDTO) {
        log.info("收到更新操作日志请求: {}", updateDTO.getId());
        try {
            ArmsLog result = armsLogService.updateArmsLog(updateDTO);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            log.error("更新操作日志失败: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 删除操作日志记录
     * 接收客户端提交的操作日志删除请求，根据ID删除指定记录
     *
     * @param id 操作日志的主键ID
     * @return 返回删除操作的结果信息，删除成功返回"删除成功"，记录不存在返回404状态码
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArmsLog(@PathVariable Long id) {
        log.info("收到删除操作日志请求，ID: {}", id);
        boolean deleted = armsLogService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok("删除成功");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 分页查询操作日志记录
     * 接收客户端提交的分页查询请求，根据条件进行分页查询
     *
     * @param queryDTO 包含分页和查询条件的DTO对象
     * @return 返回符合条件的分页结果，包含总记录数、当前页码、页面大小和当前页数据
     */
    @GetMapping
    public ResponseEntity<Page<ArmsLog>> getArmsLogs(ArmsLogQueryDTO queryDTO) {
        log.info("收到分页查询操作日志请求: {}", queryDTO);
        Page<ArmsLog> result = armsLogService.findArmsLogs(queryDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据操作类型查找日志记录
     * 接收客户端提交的操作类型查询请求，查找所有符合指定类型的操作日志
     *
     * @param operationType 操作类型，如：USER_LOGIN、DATA_EXPORT等
     * @return 返回符合条件的所有操作日志列表
     */
    @GetMapping("/type/{operationType}")
    public ResponseEntity<List<ArmsLog>> getArmsLogsByType(@PathVariable String operationType) {
        log.info("收到根据操作类型查找日志请求: {}", operationType);
        List<ArmsLog> result = armsLogService.findByOperationType(operationType);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据用户ID查找日志记录
     * 接收客户端提交的用户ID查询请求，查找所有符合指定用户ID的操作日志
     *
     * @param userId 用户ID
     * @return 返回符合条件的所有操作日志列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ArmsLog>> getArmsLogsByUserId(@PathVariable String userId) {
        log.info("收到根据用户ID查找日志请求: {}", userId);
        List<ArmsLog> result = armsLogService.findByUserId(userId);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据状态查找日志记录
     * 接收客户端提交的状态查询请求，查找所有符合指定状态的操作日志
     *
     * @param status 操作日志状态，如：SUCCESS、FAILED等
     * @return 返回符合条件的所有操作日志列表
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ArmsLog>> getArmsLogsByStatus(@PathVariable String status) {
        log.info("收到根据状态查找日志请求: {}", status);
        List<ArmsLog> result = armsLogService.findByStatus(status);
        return ResponseEntity.ok(result);
    }
}