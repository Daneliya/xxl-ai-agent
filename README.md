# XXL-AI-Agent 项目集合

## 项目简介

XXL-AI-Agent 是一个包含多个 AI 相关项目的集合，涵盖了从基础 AI 调用到高级应用的完整技术栈。本仓库旨在提供各种 AI 集成方案和实用工具，帮助开发者快速构建 AI 应用。

## 项目结构

本项目分为两个主要部分：

### 1. 基础准备阶段 (1_prepare/)

包含多个基础 AI 调用项目，用于学习和测试不同的 AI 集成方式：

- **dashScope-sdk/**：使用阿里云 DashScope SDK 的项目
- **http-ai-invoke/**：通过 HTTP 调用 AI 的项目
- **langChain4j/**：使用 LangChain4j 框架的项目
- **ollama/**：使用 Ollama 的本地 AI 模型项目
- **spring-ai/**：使用 Spring AI 框架的项目
- **spring-ai-alibaba/**：使用 Spring AI 集成阿里云的项目
- **spring-ai-alibaba1.1/**：使用 Spring AI 1.1 版本集成阿里云的项目
- **xxl-arms/**：日志管理系统，用于记录和管理 AI 应用的日志
- **xxl-prometheus/**：监控系统，用于监控 AI 应用的性能指标

### 2. 高级应用阶段 (2_advanced/)

包含多个高级 AI 应用项目，展示了 AI 在实际场景中的应用：

- **excel-app/**：Excel 分析应用，使用 AI 分析和处理 Excel 数据
- **love-app/**：恋爱咨询应用，基于 AI 提供恋爱建议和咨询
- **word-app/**：Word 文档处理应用，使用 AI 处理和分析 Word 文档
- **xxl-image-search-mcp-server/**：图像搜索 MCP 服务器，提供图像搜索功能

## 技术栈

- **后端框架**：Spring Boot
- **AI 集成**：Spring AI、LangChain4j、阿里云 DashScope、Ollama
- **数据存储**：Redis、MySQL
- **监控**：Prometheus、Micrometer
- **构建工具**：Maven

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- Redis (可选，用于部分项目的缓存)
- MySQL (可选，用于部分项目的数据存储)

### 构建和运行

1. 克隆仓库：

```bash
git clone <仓库地址>
cd xxl-ai-agent
```

2. 构建项目：

```bash
mvn clean install
```

3. 运行单个项目：

进入具体项目目录，执行：

```bash
mvn spring-boot:run
```

## 项目详情

### 1_prepare/ 目录

#### dashScope-sdk
- **功能**：使用阿里云 DashScope SDK 调用 AI 模型
- **主要文件**：`DashScopeAlibabaApplication.java`
- **配置文件**：`application.yml`

#### http-ai-invoke
- **功能**：通过 HTTP 请求直接调用 AI 模型 API
- **主要文件**：`HttpAiInvokeAlibabaApplication.java`
- **配置文件**：`application.yml`

#### langChain4j
- **功能**：使用 LangChain4j 框架构建 AI 应用
- **主要文件**：`LangChain4jAlibabaApplication.java`
- **配置文件**：`application.yml`

#### ollama
- **功能**：使用本地 Ollama 服务调用 AI 模型
- **主要文件**：`HttpAiInvokeAlibabaApplication.java`
- **配置文件**：`application.yml`

#### spring-ai
- **功能**：使用 Spring AI 框架调用 AI 模型
- **主要文件**：`StartAiApplication.java`
- **配置文件**：`application.yml`

#### spring-ai-alibaba
- **功能**：使用 Spring AI 集成阿里云 AI 模型
- **主要文件**：`StartAiAlibabaApplication.java`
- **配置文件**：`application.yml`

#### spring-ai-alibaba1.1
- **功能**：使用 Spring AI 1.1 版本集成阿里云 AI 模型
- **主要文件**：`StartAiAlibabaApplication.java`
- **配置文件**：`application.yml`

#### xxl-arms
- **功能**：日志管理系统，提供日志的增删改查功能
- **主要文件**：`XxlArmsApplication.java`
- **配置文件**：`application.yml`

#### xxl-prometheus
- **功能**：监控系统，使用 Prometheus 和 Micrometer 监控应用性能
- **主要文件**：`PrometheusApplication.java`
- **配置文件**：`application.yml`

### 2_advanced/ 目录

#### excel-app
- **功能**：Excel 分析应用，使用 AI 分析和处理 Excel 数据
- **主要文件**：`ExcelAiAlibabaApplication.java`
- **配置文件**：`application.yml`

#### love-app
- **功能**：恋爱咨询应用，基于 AI 提供恋爱建议和咨询
- **主要文件**：`LoveAiAlibabaApplication.java`
- **配置文件**：`application.yml`

#### word-app
- **功能**：Word 文档处理应用，使用 AI 处理和分析 Word 文档
- **主要文件**：`WordAiAlibabaApplication.java`
- **配置文件**：`application.yml`

#### xxl-image-search-mcp-server
- **功能**：图像搜索 MCP 服务器，提供图像搜索功能
- **主要文件**：`ImageMcpServerApplication.java`
- **配置文件**：`application.yml`

## 配置说明

每个项目都有自己的 `application.yml` 配置文件，需要根据实际情况修改配置：

- **AI 模型配置**：包括 API 密钥、模型名称、端点等
- **数据库配置**：包括数据库连接信息、用户名、密码等
- **Redis 配置**：包括 Redis 服务器地址、端口、密码等
- **监控配置**：包括 Prometheus 相关配置

## 技术文档

- **Spring AI 官方文档**：https://spring.io/projects/spring-ai
- **LangChain4j 官方文档**：https://docs.langchain4j.dev/
- **阿里云 DashScope 文档**：https://help.aliyun.com/product/101493.html
- **Ollama 官方文档**：https://ollama.ai/docs

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进这个项目。

## 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。

## 联系方式

- **项目维护者**：XXL
- **邮箱**：luckily126163@126.com
- **GitHub**：https://github.com/Daneliya
