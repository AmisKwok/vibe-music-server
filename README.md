# Vibe Music Server

<p align="center">
  <img src="icons/icon.png" alt="Vibe Music Server Icon" width="120" height="120">
</p>

<p align="center">
  <strong>一款基于 Spring Boot 3 构建的高性能音乐服务后端系统</strong>
</p>

<p align="center">
  <a href="README_EN.md">English</a> | 
  <a href="README.md">简体中文</a> | 
  <a href="README_ZH_TW.md">繁體中文</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.5.10-6DB33F?style=flat-square&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=flat-square&logo=openjdk" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-8.0+-4479A1?style=flat-square&logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/Redis-7.0+-DC382D?style=flat-square&logo=redis" alt="Redis">
  <img src="https://img.shields.io/badge/Version-1.0.0-blue?style=flat-square" alt="Version">
  <img src="https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-orange?style=flat-square" alt="License">
</p>

---

## 📖 目录

- [项目简介](#项目简介)
- [功能特点](#功能特点)
- [项目特色](#项目特色)
- [技术栈](#技术栈)
- [项目架构](#项目架构)
- [快速开始](#快速开始)
- [配置说明](#配置说明)
- [构建和部署](#构建和部署)
- [更新日志](#更新日志)
- [开发路线图](#开发路线图)
- [贡献指南](#贡献指南)
- [许可证](#许可证)
- [联系方式](#联系方式)
- [致谢](#致谢)

---

## 项目简介

**Vibe Music Server** 是一个功能完善的现代化音乐服务后端系统，为 Vibe Music App 提供完整的后端支持。采用 Spring Boot 3 框架开发，支持高性能、高可用的音乐流媒体服务。

### 核心亮点

- 🚀 **高性能架构** - 基于 Spring Boot 3，支持高并发访问
- 🎵 **完整的音乐管理** - 歌手、歌曲、歌单全生命周期管理
- 🔐 **安全认证** - JWT 认证 + Spring Security 双重保障
- 💾 **分布式存储** - MinIO 分布式对象存储支持
- ⚡ **智能缓存** - Redis 热点数据缓存，响应速度极快
- 📊 **实时监控** - 完整的日志和性能监控体系
- 📱 **多端兼容** - 统一 RESTful API，支持 Android、iOS、Web

---

## 功能特点

### 🎵 音乐内容管理

- ✅ 歌手信息 CRUD 操作
- ✅ 歌曲上传、元数据管理
- ✅ 歌单创建、编辑、删除
- ✅ 多条件查询和分页支持
- ✅ 官方推荐歌单管理

### 👥 用户服务

- ✅ 用户注册/登录
- ✅ JWT 安全认证
- ✅ 用户信息管理
- ✅ 头像上传和管理
- ✅ 密码安全加密
- ✅ 基于角色的权限控制

### 💬 社交互动

- ✅ 歌曲评论和回复
- ✅ 评论点赞机制
- ✅ 歌曲收藏功能
- ✅ 用户反馈收集

### 📱 多端支持

- ✅ 统一 RESTful API
- ✅ 设备类型自动识别
- ✅ 设备使用信息记录
- ✅ 差异化服务支持

### ⚡ 高级特性

- ✅ 接口防抖机制（基于 Redis）
- ✅ 分布式缓存优化
- ✅ MinIO 对象存储
- ✅ 用户行为分析
- ✅ 设备使用情况监控

### 🔒 安全防护

- ✅ JWT 无状态认证
- ✅ 接口权限控制
- ✅ 参数全面校验
- ✅ SQL 注入防护
- ✅ XSS 攻击防护

---

## 项目特色

### 1. 分层架构设计

采用经典的 **三层架构**，实现清晰的代码分层：

```
Controller (控制层) ← 调用 → Service (业务层) ← 调用 → Mapper (数据层)
```

### 2. 统一异常处理

使用 `@ControllerAdvice` 实现全局异常处理，统一错误响应格式：

```java
@ExceptionHandler(BusinessException.class)
public Result handleBusinessException(BusinessException ex) {
    log.error("业务异常：{}", ex.getMessage(), ex);
    return new Result(ex.getCode(), ex.getMessage(), null);
}
```

### 3. 智能防抖机制

基于注解的接口防抖功能，防止恶意请求和重复提交：

```java
@RequestDebounce(key = "sendCode", expire = 60, message = "操作过于频繁")
public Result sendVerificationCode(String email) {
    // 业务逻辑
}
```

### 4. 分布式缓存方案

基于 Redis 的分布式缓存，支持多实例环境下的数据一致性：

```java
@Cacheable(value = "song", key = "#id")
public SongVO getSongById(Long id) {
    // 业务逻辑
}
```

### 5. 性能优化

- 数据库查询优化（索引、分页）
- Redis 热点数据缓存
- 连接池优化（Druid）
- 异步任务处理

---

## 技术栈

### 核心框架

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.5.10 | 后端框架 |
| Java | 21 | 编程语言 |
| Maven | 3.9+ | 项目构建工具 |

### 数据存储

| 技术 | 版本 | 说明 |
|------|------|------|
| MySQL | 8.0+ | 关系型数据库 |
| Redis | 7.0+ | 内存数据库/缓存 |
| MinIO | 最新版 | 分布式对象存储 |

### 安全认证

| 技术 | 说明 |
|------|------|
| JWT | 无状态身份认证 |
| Spring Security | 安全框架 |
| RSA | 非对称加密 |

### 工具库

| 依赖 | 说明 |
|------|------|
| MyBatis-Plus | 增强型 ORM 框架 |
| Lombok | 代码简化工具 |
| Hutool | Java 工具库 |
| Druid | 高性能数据库连接池 |
| Swagger | API 文档生成 |
| Validation | 参数校验 |

---

## 项目架构

### 整体架构

```
┌─────────────────────────────────────────────────────────┐
│                   Controller Layer                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  User    │  │  Song    │  │ Playlist │              │
│  │Controller│  │Controller│  │Controller│              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                        ↓ 调用
┌─────────────────────────────────────────────────────────┐
│                    Service Layer                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  User    │  │  Song    │  │ Playlist │              │
│  │ Service  │  │ Service  │  │ Service  │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                        ↓ 调用
┌─────────────────────────────────────────────────────────┐
│                    Mapper Layer                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  User    │  │  Song    │  │ Playlist │              │
│  │  Mapper  │  │  Mapper  │  │  Mapper  │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                        ↓ 操作
┌─────────────────────────────────────────────────────────┐
│                    Data Storage                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  MySQL   │  │  Redis   │  │  MinIO   │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
```

### 三层架构

本项目采用经典的 **三层架构** 模式：

- **Controller（控制层）**：处理 HTTP 请求和响应
  - `controller/` - REST 控制器
  - 负责参数校验、结果包装

- **Service（业务层）**：处理核心业务逻辑
  - `service/` - 业务接口
  - `service/impl/` - 业务实现
  - 抛出 `BusinessException` 处理业务异常

- **Mapper（数据层）**：处理数据库操作
  - `mapper/` - MyBatis Mapper 接口
  - `entity/` - 数据库实体类

---

## 快速开始

### 前提条件

- JDK 21 或更高版本
- MySQL 8.0+
- Redis 7.0+
- MinIO 最新版本
- Maven 3.9+

### 安装步骤

1. **克隆仓库**

```bash
# GitHub
git clone https://github.com/AmisKwok/vibe-music-server.git

# 或 Gitee（国内用户推荐）
git clone https://gitee.com/AmisKwok/vibe-music-server.git

cd vibe-music-server
```

2. **数据库初始化**

```sql
CREATE DATABASE vibe_music CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **配置环境变量**

```bash
# 复制配置文件模板
cp src/main/resources/application.yml.template src/main/resources/application.yml

# 编辑 application.yml 文件，配置数据库、Redis、MinIO 等信息
```

`application.yml` 主要配置项：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vibe_music?useUnicode=true&characterEncoding=utf8
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password

minio:
  endpoint: http://localhost:9000
  accessKey: your_access_key
  secretKey: your_secret_key
  bucketName: vibe-music
```

4. **构建项目**

```bash
mvn clean package -DskipTests
```

5. **启动服务**

```bash
java -jar target/vibe-music-server-*.jar
```

6. **访问 API 文档**

启动成功后，访问以下地址查看 API 文档：

```
http://localhost:8080/swagger-ui.html
```

---

## 配置说明

### 应用配置

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| `server.port` | 服务端口 | 8080 |
| `spring.application.name` | 应用名称 | vibe-music-server |

### 数据库配置

| 配置项 | 说明 | 示例值 |
|--------|------|--------|
| `spring.datasource.url` | MySQL 连接地址 | `jdbc:mysql://localhost:3306/vibe_music` |
| `spring.datasource.username` | 数据库用户名 | `root` |
| `spring.datasource.password` | 数据库密码 | `password` |

### Redis 配置

| 配置项 | 说明 | 示例值 |
|--------|------|--------|
| `spring.data.redis.host` | Redis 主机地址 | `localhost` |
| `spring.data.redis.port` | Redis 端口 | `6379` |
| `spring.data.redis.password` | Redis 密码 | `your_password` |

### MinIO 配置

| 配置项 | 说明 | 示例值 |
|--------|------|--------|
| `minio.endpoint` | MinIO 服务地址 | `http://localhost:9000` |
| `minio.accessKey` | 访问密钥 | `minioadmin` |
| `minio.secretKey` | 密钥 | `minioadmin` |
| `minio.bucketName` | 存储桶名称 | `vibe-music` |

### JWT 配置

| 配置项 | 说明 | 示例值 |
|--------|------|--------|
| `jwt.secret` | JWT 密钥 | `your-secret-key` |
| `jwt.expiration` | Token 有效期（毫秒） | `86400000` |

### RSA 配置

| 配置项 | 说明 | 示例值 |
|--------|------|--------|
| `rsa.public-key` | RSA 公钥 | `-----BEGIN PUBLIC KEY-----...` |
| `rsa.private-key` | RSA 私钥 | `-----BEGIN PRIVATE KEY-----...` |

---

## 构建和部署

### 本地开发

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 启动应用（开发模式）
mvn spring-boot:run
```

### 生产构建

```bash
# 构建可执行 JAR
mvn clean package -DskipTests

# 构建产物位于 target/ 目录
ls -lh target/vibe-music-server-*.jar
```

### Docker 部署

项目支持 Docker 容器化部署：

```bash
# 构建 Docker 镜像
docker build -t vibe-music-server:latest .

# 运行容器
docker run -d \
  --name vibe-music-server \
  -p 8080:8080 \
  -v /path/to/application.yml:/app/application.yml \
  vibe-music-server:latest
```

### Docker Compose

使用 Docker Compose 一键部署所有服务：

```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

---

## 更新日志

### v1.0.0 (2026-03-05)

**新增功能**
- ✨ 采用 CC BY-NC-SA 4.0 开源协议
- ✨ 优化项目架构（Service 层不再返回 Result）
- ✨ 添加全局异常处理器
- ✨ 实现 BusinessException 业务异常类
- ✨ 添加 StartupBanner 启动横幅
- ✨ 实现 RsaConsoleUtil 控制台加密工具
- ✨ 完善中文注释

**问题修复**
- 🐛 修复 Service 层耦合 Result 的架构问题
- 🐛 修复 RSA 密钥读取问题
- 🐛 修复 MyBatis-Plus 实体类注解缺失问题

**优化改进**
- ♻️ 重构所有 Service 接口和实现类
- ♻️ 重构所有 Controller 控制器
- ♻️ 优化代码结构和注释
- ⚡ 性能优化和异常处理改进

### v0.1.0 (2026-01-23)

**首次发布**
- 🎉 基础音乐管理功能
- 🎨 现代化 RESTful API 设计
- 💾 MySQL + Redis + MinIO 存储支持
- 🔐 JWT 安全认证
- 👤 用户认证系统

---

## 开发路线图

### 近期计划 (v1.1.0)

- [ ] 歌词管理功能
- [ ] 音乐推荐算法
- [ ] 播放历史记录
- [ ] 用户行为分析
- [ ] 歌单分享功能

---

## 贡献指南

我们欢迎所有形式的贡献！

### 如何贡献

1. **Fork 仓库**
```bash
git clone https://github.com/AmisKwok/vibe-music-server.git
```

2. **创建功能分支**
```bash
git checkout -b feature/your-feature-name
```

3. **提交更改**
```bash
git add .
git commit -m "✨ Add: 添加新功能描述"
```

4. **推送到分支**
```bash
git push origin feature/your-feature-name
```

5. **创建 Pull Request**

### 提交规范

使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

- `✨ Add:` 新功能
- `🐛 Fix:` 修复问题
- `♻️ Refactor:` 代码重构
- `📝 Docs:` 文档更新
- `🎨 Style:` 代码格式调整
- `⚡ Perf:` 性能优化
- `✅ Test:` 测试相关

### 代码审查

所有 PR 都需要经过代码审查：
1. 代码风格检查
2. 功能测试
3. 性能评估
4. 文档完整性

---

## 许可证

本项目采用 **Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License (CC BY-NC-SA 4.0)** 许可协议。

### 许可证特点

| 权限 | 条件 | 限制 |
|------|------|------|
| ✅ 共享 | 📝 署名 | ❌ 禁止商业使用 |
| ✅ 修改 | 🔄 相同方式共享 | ❌ 无附加限制 |

### 简单来说

**你可以：**
- ✅ 复制、分发本作品
- ✅ 修改、转换或基于本作品进行创作
- ✅ 用于学习、研究和个人使用

**你必须：**
- 📝 保留版权声明
- 📝 提供许可协议链接
- 📝 说明是否进行了修改
- 🔄 使用相同协议分发衍生作品

**你不能：**
- ❌ 用于商业目的
- ❌ 销售本软件或其修改版本
- ❌ 通过本软件获取商业利益

完整协议文本请查看 [LICENSE](LICENSE) 文件。

---

## 联系方式

### 项目地址

- **GitHub**: https://github.com/AmisKwok/vibe-music-server
- **Gitee**: https://gitee.com/AmisKwok/vibe-music-server

### 问题反馈

- **GitHub Issues**: https://github.com/AmisKwok/vibe-music-server/issues
- **Gitee Issues**: https://gitee.com/AmisKwok/vibe-music-server/issues

### 作者

- **AmisKwok**

---

## 致谢

感谢所有为这个项目做出贡献的人！

---

<p align="center">
  <strong>享受音乐，享受生活！🎧</strong>
</p>

<p align="center">
  如果这个项目对你有帮助，请给一个 ⭐️ Star 支持一下！
</p>
