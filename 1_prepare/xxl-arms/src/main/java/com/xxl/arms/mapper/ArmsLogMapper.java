package com.xxl.arms.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.paginate.Page;
import com.xxl.arms.entity.ArmsLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志数据访问层Mapper接口
 * 继承BaseMapper提供基础的CRUD操作
 * 提供自定义的查询方法，满足各种业务需求
 *
 * @author xxl
 * @since 2025/1/4
 */
@Mapper
public interface ArmsLogMapper extends BaseMapper<ArmsLog> {

    /**
     * 根据操作类型查找日志记录
     *
     * @param operationType 操作类型，如：USER_LOGIN、DATA_EXPORT等
     * @return 返回符合条件的所有操作日志列表
     */
    @Select("SELECT * FROM arms_log WHERE operation_type = #{operationType} ORDER BY created_time DESC")
    List<ArmsLog> findByOperationType(@Param("operationType") String operationType);

    /**
     * 根据用户ID查找日志记录
     *
     * @param userId 用户唯一标识
     * @return 返回指定用户的所有操作日志列表
     */
    @Select("SELECT * FROM arms_log WHERE user_id = #{userId} ORDER BY created_time DESC")
    List<ArmsLog> findByUserId(@Param("userId") String userId);

    /**
     * 根据状态查找日志记录
     *
     * @param status 操作状态，SUCCESS表示成功，FAILED表示失败
     * @return 返回符合条件的所有操作日志列表
     */
    @Select("SELECT * FROM arms_log WHERE status = #{status} ORDER BY created_time DESC")
    List<ArmsLog> findByStatus(@Param("status") String status);

    /**
     * 根据时间范围查找日志记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回指定时间范围内的所有操作日志列表
     */
    @Select("SELECT * FROM arms_log WHERE created_time BETWEEN #{startTime} AND #{endTime} ORDER BY created_time DESC")
    List<ArmsLog> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 根据操作类型和状态分页查询日志记录
     *
     * @param page          分页对象，包含页码和页面大小
     * @param operationType 操作类型
     * @param status        操作状态
     * @return 返回符合条件分页结果
     */
    @Select("SELECT * FROM arms_log WHERE operation_type = #{operationType} AND status = #{status} ORDER BY created_time DESC")
    Page<ArmsLog> findByOperationTypeAndStatus(Page<ArmsLog> page, @Param("operationType") String operationType, @Param("status") String status);

    /**
     * 根据用户ID和状态分页查询日志记录
     *
     * @param page   分页对象，包含页码和页面大小
     * @param userId 用户唯一标识
     * @param status 操作状态
     * @return 返回符合条件分页结果
     */
    @Select("SELECT * FROM arms_log WHERE user_id = #{userId} AND status = #{status} ORDER BY created_time DESC")
    Page<ArmsLog> findByUserIdAndStatus(Page<ArmsLog> page, @Param("userId") String userId, @Param("status") String status);

    /**
     * 统计指定操作类型的记录数量
     *
     * @param operationType 操作类型
     * @return 返回指定操作类型的总记录数
     */
    @Select("SELECT COUNT(*) FROM arms_log WHERE operation_type = #{operationType}")
    Long countByOperationType(@Param("operationType") String operationType);

    /**
     * 统计指定状态的记录数量
     *
     * @param status 操作状态
     * @return 返回指定状态的总记录数
     */
    @Select("SELECT COUNT(*) FROM arms_log WHERE status = #{status}")
    Long countByStatus(@Param("status") String status);

    /**
     * 根据多个条件组合查询操作日志
     *
     * @param page          分页对象
     * @param operationType 操作类型（可选）
     * @param userId        用户ID（可选）
     * @param status        状态（可选）
     * @param startTime     开始时间（可选）
     * @param endTime       结束时间（可选）
     * @return 返回符合条件分页结果
     */
//    @Select({
//            "<script>",
//            "SELECT * FROM arms_log",
//            "<where>",
//            "<if test='operationType != null and operationType != \"\"'>",
//            "AND operation_type = #{operationType}",
//            "</if>",
//            "<if test='userId != null and userId != \"\"'>",
//            "AND user_id = #{userId}",
//            "</if>",
//            "<if test='status != null and status != \"\"'>",
//            "AND status = #{status}",
//            "</if>",
//            "<if test='startTime != null'>",
//            "AND created_time >= #{startTime}",
//            "</if>",
//            "<if test='endTime != null'>",
//            "AND created_time <= #{endTime}",
//            "</if>",
//            "</where>",
//            "ORDER BY created_time DESC",
//            "</script>"
//    })
//    Page<ArmsLog> findByConditions(Page<ArmsLog> page,
//                                   @Param("operationType") String operationType,
//                                   @Param("userId") String userId,
//                                   @Param("status") String status,
//                                   @Param("startTime") LocalDateTime startTime,
//                                   @Param("endTime") LocalDateTime endTime);
}