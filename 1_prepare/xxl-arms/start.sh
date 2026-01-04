#!/bin/bash

# xxl-arms 模块启动脚本

echo "正在启动 xxl-arms 模块..."
echo "请确保 MySQL 和 Redis 服务已启动"
echo "MySQL 数据库: xxl_arms"
echo "Redis 服务: localhost:6379"
echo

cd "$(dirname "$0")"

echo "编译项目..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "编译成功，启动应用..."
    mvn spring-boot:run
else
    echo "编译失败，请检查错误信息"
    exit 1
fi