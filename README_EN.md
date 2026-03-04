# Vibe Music Server

<p align="center">
  <img src="icons/icon.png" alt="Vibe Music Server Icon" width="120" height="120">
</p>

<p align="center">
  <strong>A High-Performance Music Service Backend System Built with Spring Boot 3</strong>
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

## 📖 Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Highlights](#highlights)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [Build and Deployment](#build-and-deployment)
- [Changelog](#changelog)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)
- [Acknowledgments](#acknowledgments)

---

## Introduction

**Vibe Music Server** is a fully-featured modern music service backend system that provides complete backend support for Vibe Music App. Built with Spring Boot 3 framework, it supports high-performance, high-availability music streaming services.

### Core Highlights

- 🚀 **High-Performance Architecture** - Built with Spring Boot 3, supports high-concurrency access
- 🎵 **Complete Music Management** - Full lifecycle management for artists, songs, and playlists
- 🔐 **Secure Authentication** - JWT authentication + Spring Security dual protection
- 💾 **Distributed Storage** - MinIO distributed object storage support
- ⚡ **Smart Caching** - Redis hot data caching, extremely fast response
- 📊 **Real-time Monitoring** - Complete logging and performance monitoring system
- 📱 **Multi-Platform Compatibility** - Unified RESTful API, supports Android, iOS, Web

---

## Features

### 🎵 Music Content Management

- ✅ Artist information CRUD operations
- ✅ Song upload and metadata management
- ✅ Playlist creation, editing, and deletion
- ✅ Multi-condition queries and pagination support
- ✅ Official recommended playlist management

### 👥 User Services

- ✅ User registration/login
- ✅ JWT secure authentication
- ✅ User information management
- ✅ Avatar upload and management
- ✅ Password security encryption
- ✅ Role-based access control

### 💬 Social Interaction

- ✅ Song comments and replies
- ✅ Comment like mechanism
- ✅ Song collection feature
- ✅ User feedback collection

### 📱 Multi-Platform Support

- ✅ Unified RESTful API
- ✅ Automatic device type recognition
- ✅ Device usage information recording
- ✅ Differentiated service support

### ⚡ Advanced Features

- ✅ Request debounce mechanism (Redis-based)
- ✅ Distributed cache optimization
- ✅ MinIO object storage
- ✅ User behavior analysis
- ✅ Device usage monitoring

### 🔒 Security Protection

- ✅ JWT stateless authentication
- ✅ API permission control
- ✅ Comprehensive parameter validation
- ✅ SQL injection protection
- ✅ XSS attack protection

---

## Highlights

### 1. Layered Architecture Design

Adopts classic **Three-Layer Architecture** for clear code separation:

```
Controller (Control Layer) ← calls → Service (Business Layer) ← calls → Mapper (Data Layer)
```

### 2. Unified Exception Handling

Uses `@ControllerAdvice` to implement global exception handling with unified error response format:

```java
@ExceptionHandler(BusinessException.class)
public Result handleBusinessException(BusinessException ex) {
    log.error("Business exception: {}", ex.getMessage(), ex);
    return new Result(ex.getCode(), ex.getMessage(), null);
}
```

### 3. Smart Debounce Mechanism

Annotation-based request debounce to prevent malicious requests and duplicate submissions:

```java
@RequestDebounce(key = "sendCode", expire = 60, message = "Operation too frequent")
public Result sendVerificationCode(String email) {
    // Business logic
}
```

### 4. Distributed Caching Solution

Redis-based distributed caching supporting data consistency in multi-instance environments:

```java
@Cacheable(value = "song", key = "#id")
public SongVO getSongById(Long id) {
    // Business logic
}
```

### 5. Performance Optimization

- Database query optimization (indexing, pagination)
- Redis hot data caching
- Connection pool optimization (Druid)
- Asynchronous task processing

---

## Tech Stack

### Core Frameworks

| Technology | Version | Description |
|------|------|------|
| Spring Boot | 3.2.0 | Backend framework |
| Java | 21 | Programming language |
| Maven | 3.6+ | Project build tool |

### Data Storage

| Technology | Version | Description |
|------|------|------|
| MySQL | 8.0+ | Relational database |
| Redis | 7.0+ | In-memory database/cache |
| MinIO | Latest | Distributed object storage |

### Security & Authentication

| Technology | Description |
|------|------|
| JWT | Stateless authentication |
| Spring Security | Security framework |
| RSA | Asymmetric encryption |

### Libraries

| Dependency | Description |
|------|------|
| MyBatis-Plus | Enhanced ORM framework |
| Lombok | Code simplification tool |
| Hutool | Java utility library |
| Druid | High-performance database connection pool |
| Swagger | API documentation generation |
| Validation | Parameter validation |

---

## Architecture

### Overall Architecture

```
┌─────────────────────────────────────────────────────────┐
│                   Controller Layer                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  User    │  │  Song    │  │ Playlist │              │
│  │Controller│  │Controller│  │Controller│              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                        ↓ calls
┌─────────────────────────────────────────────────────────┐
│                    Service Layer                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  User    │  │  Song    │  │ Playlist │              │
│  │ Service  │  │ Service  │  │ Service  │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                        ↓ calls
┌─────────────────────────────────────────────────────────┐
│                    Mapper Layer                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  User    │  │  Song    │  │ Playlist │              │
│  │  Mapper  │  │  Mapper  │  │  Mapper  │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                        ↓ operations
┌─────────────────────────────────────────────────────────┐
│                    Data Storage                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  MySQL   │  │  Redis   │  │  MinIO   │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
```

### Three-Layer Architecture

This project adopts the classic **Three-Layer Architecture** pattern:

- **Controller Layer**: Handles HTTP requests and responses
  - `controller/` - REST controllers
  - Responsible for parameter validation and result wrapping

- **Service Layer**: Handles core business logic
  - `service/` - Business interfaces
  - `service/impl/` - Business implementations
  - Throws `BusinessException` for business exceptions

- **Mapper Layer**: Handles database operations
  - `mapper/` - MyBatis Mapper interfaces
  - `entity/` - Database entity classes

---

## Quick Start

### Prerequisites

- JDK 17 or higher
- MySQL 8.0+
- Redis 7.0+
- MinIO latest version
- Maven 3.9+

### Installation Steps

1. **Clone Repository**

```bash
# GitHub
git clone https://github.com/AmisKwok/vibe-music-server.git

# Or Gitee (recommended for users in China)
git clone https://gitee.com/AmisKwok/vibe-music-server.git

cd vibe-music-server
```

2. **Database Initialization**

```sql
CREATE DATABASE vibe_music CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **Configure Environment**

```bash
# Copy configuration template
cp src/main/resources/application.yml.template src/main/resources/application.yml

# Edit application.yml file, configure database, Redis, MinIO, etc.
```

Main configuration items in `application.yml`:

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

4. **Build Project**

```bash
mvn clean package -DskipTests
```

5. **Start Service**

```bash
java -jar target/vibe-music-server-*.jar
```

6. **Access API Documentation**

After successful startup, access the following address to view API documentation:

```
http://localhost:8080/swagger-ui.html
```

---

## Configuration

### Application Configuration

| Configuration Item | Description | Default Value |
|--------|------|--------|
| `server.port` | Service port | 8080 |
| `spring.application.name` | Application name | vibe-music-server |

### Database Configuration

| Configuration Item | Description | Example Value |
|--------|------|--------|
| `spring.datasource.url` | MySQL connection address | `jdbc:mysql://localhost:3306/vibe_music` |
| `spring.datasource.username` | Database username | `root` |
| `spring.datasource.password` | Database password | `password` |

### Redis Configuration

| Configuration Item | Description | Example Value |
|--------|------|--------|
| `spring.data.redis.host` | Redis host address | `localhost` |
| `spring.data.redis.port` | Redis port | `6379` |
| `spring.data.redis.password` | Redis password | `your_password` |

### MinIO Configuration

| Configuration Item | Description | Example Value |
|--------|------|--------|
| `minio.endpoint` | MinIO service address | `http://localhost:9000` |
| `minio.accessKey` | Access key | `minioadmin` |
| `minio.secretKey` | Secret key | `minioadmin` |
| `minio.bucketName` | Bucket name | `vibe-music` |

### JWT Configuration

| Configuration Item | Description | Example Value |
|--------|------|--------|
| `jwt.secret` | JWT secret key | `your-secret-key` |
| `jwt.expiration` | Token validity period (milliseconds) | `86400000` |

### RSA Configuration

| Configuration Item | Description | Example Value |
|--------|------|--------|
| `rsa.public-key` | RSA public key | `-----BEGIN PUBLIC KEY-----...` |
| `rsa.private-key` | RSA private key | `-----BEGIN PRIVATE KEY-----...` |

---

## Build and Deployment

### Local Development

```bash
# Compile project
mvn clean compile

# Run tests
mvn test

# Start application (development mode)
mvn spring-boot:run
```

### Production Build

```bash
# Build executable JAR
mvn clean package -DskipTests

# Build artifacts located in target/ directory
ls -lh target/vibe-music-server-*.jar
```

### Docker Deployment

The project supports Docker containerized deployment:

```bash
# Build Docker image
docker build -t vibe-music-server:latest .

# Run container
docker run -d \
  --name vibe-music-server \
  -p 8080:8080 \
  -v /path/to/application.yml:/app/application.yml \
  vibe-music-server:latest
```

### Docker Compose

Use Docker Compose to deploy all services with one command:

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

---

## Changelog

### v1.0.0 (2026-03-05)

**New Features**
- ✨ Adopted CC BY-NC-SA 4.0 open source license
- ✨ Optimized project architecture (Service layer no longer returns Result)
- ✨ Added global exception handler
- ✨ Implemented BusinessException business exception class
- ✨ Added StartupBanner startup banner
- ✨ Implemented RsaConsoleUtil console encryption tool
- ✨ Improved Chinese comments

**Bug Fixes**
- 🐛 Fixed Service layer coupling with Result architecture issue
- 🐛 Fixed RSA key reading issue
- 🐛 Fixed MyBatis-Plus entity class annotation missing issue

**Improvements**
- ♻️ Refactored all Service interfaces and implementations
- ♻️ Refactored all Controller controllers
- ♻️ Optimized code structure and comments
- ⚡ Performance optimization and exception handling improvements

### v0.1.0 (2026-01-23)

**Initial Release**
- 🎉 Basic music management features
- 🎨 Modern RESTful API design
- 💾 MySQL + Redis + MinIO storage support
- 🔐 JWT secure authentication
- 👤 User authentication system

---

## Roadmap

### Near-term Plans (v1.1.0)

- [ ] Lyrics management feature
- [ ] Music recommendation algorithm
- [ ] Play history recording
- [ ] User behavior analysis
- [ ] Playlist sharing feature

---

## Contributing

We welcome all forms of contributions!

### How to Contribute

1. **Fork Repository**
```bash
git clone https://github.com/AmisKwok/vibe-music-server.git
```

2. **Create Feature Branch**
```bash
git checkout -b feature/your-feature-name
```

3. **Commit Changes**
```bash
git add .
git commit -m "✨ Add: feature description"
```

4. **Push to Branch**
```bash
git push origin feature/your-feature-name
```

5. **Create Pull Request**

### Commit Convention

Use [Conventional Commits](https://www.conventionalcommits.org/) specification:

- `✨ Add:` New feature
- `🐛 Fix:` Bug fix
- `♻️ Refactor:` Code refactoring
- `📝 Docs:` Documentation update
- `🎨 Style:` Code style adjustment
- `⚡ Perf:` Performance optimization
- `✅ Test:` Test related

### Code Review

All PRs need to go through code review:
1. Code style check
2. Functional testing
3. Performance evaluation
4. Documentation completeness

---

## License

This project is licensed under the **Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License (CC BY-NC-SA 4.0)**.

### License Features

| Permissions | Conditions | Limitations |
|------|------|------|
| ✅ Share | 📝 Attribution | ❌ No commercial use |
| ✅ Modify | 🔄 Share alike | ❌ No additional restrictions |

### In Simple Terms

**You can:**
- ✅ Copy and distribute this work
- ✅ Modify, transform, or build upon this work
- ✅ Use for learning, research, and personal purposes

**You must:**
- 📝 Keep copyright notice
- 📝 Provide license link
- 📝 Indicate if changes were made
- 🔄 Distribute derivative works under the same license

**You cannot:**
- ❌ Use for commercial purposes
- ❌ Sell this software or its modified versions
- ❌ Obtain commercial benefits through this software

See [LICENSE](LICENSE) file for full license text.

---

## Contact

### Project Links

- **GitHub**: https://github.com/AmisKwok/vibe-music-server
- **Gitee**: https://gitee.com/AmisKwok/vibe-music-server

### Issue Feedback

- **GitHub Issues**: https://github.com/AmisKwok/vibe-music-server/issues
- **Gitee Issues**: https://gitee.com/AmisKwok/vibe-music-server/issues

### Author

- **AmisKwok**

---

## Acknowledgments

Thanks to everyone who contributed to this project!

---

<p align="center">
  <strong>Enjoy music, enjoy life! 🎧</strong>
</p>

<p align="center">
  If this project helps you, please give it a ⭐️ Star!
</p>
