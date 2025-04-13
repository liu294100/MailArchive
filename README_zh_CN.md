# 邮件归档系统

[English](README.md)

一个强大的邮件归档系统，可自动归档企业邮件，支持各种编码格式，确保不会遗漏任何邮件。

## 功能特点

- **多邮箱支持**：配置和管理多个邮箱账户
- **实时归档**：自动实时归档邮件
- **编码兼容**：支持多种邮件编码（UTF-8、GBK、GB2312等）
- **附件管理**：自动保存和管理邮件附件
- **搜索功能**：按发件人、日期范围等搜索归档邮件
- **Web界面**：用户友好的系统管理界面
- **高性能**：异步处理和批量操作以实现高吞吐量
- **可靠性**：异常处理、重试机制和事务管理

## 技术栈

- Java 8
- Spring Boot 2.7.5
- MySQL 8.0
- Maven
- Docker
- Jenkins
- Bootstrap 5
- jQuery

## 快速开始

### 前置要求

- JDK 8
- Maven 3.6+
- MySQL 8.0
- Docker（可选）

### 数据库设置

```sql
CREATE DATABASE mail_archive CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 配置

编辑 `application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mail_archive?useUnicode=true&characterEncoding=utf8&useSSL=false
    username: your_username
    password: your_password
```

### 构建和运行

#### 使用 Maven

```bash
mvn clean package
java -jar target/mail-archive-1.0-SNAPSHOT.jar
```

#### 使用 Docker

```bash
# 构建镜像
docker build -t mail-archive .

# 运行容器
docker run -d --name mail-archive \
    -p 8080:8080 \
    -v /data/attachments:/data/attachments \
    mail-archive
```

### 访问应用

在浏览器中打开 http://localhost:8080

## 开发

### 项目结构

```
src/
├── main/
│   ├── java/
│   │   └── org/apollo/mail/
│   │       ├── controller/    # REST控制器
│   │       ├── entity/        # JPA实体
│   │       ├── repository/    # 数据访问
│   │       ├── service/       # 业务逻辑
│   │       ├── util/          # 工具类
│   │       └── MailArchiveApplication.java
│   └── resources/
│       ├── static/           # 静态资源
│       ├── templates/        # Thymeleaf模板
│       └── application.yml   # 配置文件
```

### CI/CD

项目使用 Jenkins 进行持续集成和部署。流水线包括：

1. 代码检出
2. 构建
3. 测试
4. SonarQube分析
5. Docker镜像构建
6. Docker镜像推送
7. 部署

## API文档

### 邮箱配置

```
GET    /api/mailbox          # 列出所有邮箱
POST   /api/mailbox          # 添加新邮箱
PUT    /api/mailbox/{id}     # 更新邮箱
DELETE /api/mailbox/{id}     # 删除邮箱
```

### 邮件归档

```
GET    /api/archive/search   # 搜索归档邮件
POST   /api/archive/mailbox/{id}  # 触发归档
```

## 贡献

1. Fork 本仓库
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件 