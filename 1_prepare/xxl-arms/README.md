# xxl-arms 模块

## 项目简介

xxl-arms 是一个基于 Spring Boot 的操作日志管理模块，支持将日志数据存储到 MySQL 数据库，并可使用 Redis 进行缓存加速。

## 技术栈

- Spring Boot 3.5.7
- MyBatis-Flex 1.9.7
- Spring Data Redis
- MySQL 8.0
- Lombok

## 项目结构

```
xxl-arms/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/xxl/arms/
│   │   │       ├── XxlArmsApplication.java          # 启动类
│   │   │       ├── controller/
│   │   │       │   └── ArmsLogController.java       # REST API 控制器
│   │   │       ├── dto/
│   │   │       │   ├── ArmsLogCreateDTO.java        # 创建 DTO
│   │   │       │   ├── ArmsLogQueryDTO.java         # 查询 DTO
│   │   │       │   └── ArmsLogUpdateDTO.java        # 更新 DTO
│   │   │       ├── entity/
│   │   │       │   └── ArmsLog.java                 # 实体类
│   │   │       ├── mapper/
│   │   │       │   └── ArmsLogMapper.java           # MyBatis-Flex Mapper
│   │   │       └── service/
│   │   │           ├── ArmsLogService.java          # 服务接口
│   │   │           └── impl/
│   │   │               └── ArmsLogServiceImpl.java  # 服务实现
│   │   └── resources/
│   │       ├── application.yml                      # 应用配置
│   │       └── schema.sql                           # 数据库脚本
│   └── test/
│       └── java/
│           └── com/xxl/arms/test/
│               └── ArmsLogTest.java                 # 测试类
├── pom.xml                                          # Maven 配置
└── start.sh                                         # 启动脚本
```

## 数据库表结构

### arms_log 表

| 字段名 | 类型 | 说明 | 备注 |
|--------|------|------|------|
| id | bigint(20) | 主键ID | AUTO_INCREMENT |
| operation_type | varchar(50) | 操作类型 | NOT NULL |
| operation_desc | varchar(500) | 操作描述 | NOT NULL |
| user_id | varchar(100) | 用户ID | 可空 |
| request_params | text | 请求参数 | JSON格式 |
| response_data | text | 响应数据 | JSON格式 |
| execution_time | varchar(20) | 执行时间 | 例如: 100ms |
| status | varchar(20) | 状态 | SUCCESS/FAILED |
| created_time | datetime | 创建时间 | 自动维护 |
| updated_time | datetime | 更新时间 | 自动维护 |

## API 接口

### 1. 创建操作日志
```
POST /api/arms-log
Content-Type: application/json

{
    "operationType": "USER_LOGIN",
    "operationDesc": "用户登录",
    "userId": "user001",
    "requestParams": "{\"username\":\"admin\",\"password\":\"***\"}",
    "responseData": "{\"token\":\"abc123\"}",
    "executionTime": "50ms",
    "status": "SUCCESS"
}
```

### 2. 根据ID查找操作日志
```
GET /api/arms-log/{id}
```

### 3. 更新操作日志
```
PUT /api/arms-log
Content-Type: application/json

{
    "id": 1,
    "operationType": "USER_LOGIN",
    "operationDesc": "用户登录（更新）",
    "status": "SUCCESS"
}
```

### 4. 删除操作日志
```
DELETE /api/arms-log/{id}
```

### 5. 分页查询操作日志
```
GET /api/arms-log?page=1&size=10&operationType=USER_LOGIN&status=SUCCESS
```

### 6. 根据操作类型查找日志
```
GET /api/arms-log/type/{operationType}
```

### 7. 根据用户ID查找日志
```
GET /api/arms-log/user/{userId}
```

### 8. 根据状态查找日志
```
GET /api/arms-log/status/{status}
```

## 配置文件

application.yml 主要配置：

```yaml
spring:
  application:
    name: xxl-arms
  
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/xxl_arms?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: xxl666
  
  # MyBatis-Flex配置
  mybatis-flex:
    # 开启驼峰命名转换
    map-underscore-to-camel-case: true
    # 开启SQL日志
    sql-log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  
  redis:
    host: localhost
    port: 6379
    password: 
    database: 0
    timeout: 3000ms
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0

server:
  port: 8080

logging:
  level:
    com.xxl.arms: DEBUG
```

## 启动说明

1. 确保 MySQL 和 Redis 服务已启动
2. 创建数据库 `xxl_arms`
3. 运行项目：
   ```bash
   cd 1_prepare/xxl-arms
   mvn spring-boot:run
   ```
   或使用启动脚本：
   ```bash
   ./start.sh
   ```

## 测试

运行测试：
```bash
mvn test
```

## 注意事项

1. 请确保 MySQL 和 Redis 服务配置正确
2. 数据库连接信息请根据实际环境修改
3. 项目使用 MyBatis-Flex 作为 ORM 框架
4. 请确保已创建 `xxl_arms` 数据库并执行相关初始化脚本
5. MyBatis-Flex 支持自动生成 SQL 和字段映射

## 示例使用

```java
@Autowired
private ArmsLogService armsLogService;

// 创建日志
ArmsLogCreateDTO createDTO = new ArmsLogCreateDTO();
createDTO.setOperationType("USER_OPERATION");
createDTO.setOperationDesc("用户操作");
createDTO.setUserId("user001");
createDTO.setRequestParams("{\"action\":\"click\"}");
createDTO.setResponseData("{\"result\":\"success\"}");
createDTO.setExecutionTime("100ms");
createDTO.setStatus("SUCCESS");
ArmsLog log = armsLogService.createArmsLog(createDTO);

// 查询日志
ArmsLogQueryDTO queryDTO = new ArmsLogQueryDTO();
queryDTO.setPage(1);
queryDTO.setSize(10);
queryDTO.setOperationType("USER_OPERATION");
queryDTO.setStatus("SUCCESS");
Page<ArmsLog> result = armsLogService.findArmsLogs(queryDTO);

// 更新日志
ArmsLogUpdateDTO updateDTO = new ArmsLogUpdateDTO();
updateDTO.setId(log.getId());
updateDTO.setOperationDesc("用户操作（已更新）");
updateDTO.setStatus("UPDATED");
ArmsLog updatedLog = armsLogService.updateArmsLog(updateDTO);

// 根据ID查找
ArmsLog foundLog = armsLogService.findArmsLogById(log.getId());

// 根据操作类型查找
List<ArmsLog> operationLogs = armsLogService.findByOperationType("USER_OPERATION");
```