# Vibe Music Server

<p align="center">
  <img src="icons/icon.png" alt="Vibe Music Server Icon" width="120" height="120">
</p>

<p align="center">
  <strong>一款基於 Spring Boot 3 構建的高性能音樂服務後端系統</strong>
</p>

<p align="center">
  <a href="README_EN.md">English</a> | 
  <a href="README.md">简体中文</a> | 
  <a href="README_ZH_TW.md">繁體中文</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.2.0-6DB33F?style=flat-square&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-8.0+-4479A1?style=flat-square&logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/Redis-7.0+-DC382D?style=flat-square&logo=redis" alt="Redis">
  <img src="https://img.shields.io/badge/Version-1.0.0-blue?style=flat-square" alt="Version">
  <img src="https://img.shields.io/badge/License-CC%20BY--NC--SA%204.0-orange?style=flat-square" alt="License">
</p>

---

## 📖 目錄

- [專案簡介](#專案簡介)
- [功能特點](#功能特點)
- [專案特色](#專案特色)
- [技術棧](#技術棧)
- [專案架構](#專案架構)
- [快速開始](#快速開始)
- [配置說明](#配置說明)
- [構建和部署](#構建和部署)
- [更新日誌](#更新日誌)
- [開發路線圖](#開發路線圖)
- [貢獻指南](#貢獻指南)
- [許可證](#許可證)
- [聯繫方式](#聯繫方式)
- [致謝](#致謝)

---

## 專案簡介

**Vibe Music Server** 是一個功能完善的現代化音樂服務後端系統，為 Vibe Music App 提供完整的後端支持。採用 Spring Boot 3 框架開發，支持高性能、高可用的音樂串流媒體服務。

### 核心亮點

- 🚀 **高性能架構** - 基於 Spring Boot 3，支持高並發訪問
- 🎵 **完整的音樂管理** - 歌手、歌曲、歌單全生命週期管理
- 🔐 **安全認證** - JWT 認證 + Spring Security 雙重保障
- 💾 **分佈式存儲** - MinIO 分佈式對象存儲支持
- ⚡ **智能緩存** - Redis 熱點數據緩存，響應速度極快
- 📊 **實時監控** - 完整的日誌和性能監控體系
- 📱 **多端兼容** - 統一 RESTful API，支持 Android、iOS、Web

---

## 功能特點

### 🎵 音樂內容管理

- ✅ 歌手信息 CRUD 操作
- ✅ 歌曲上傳、元數據管理
- ✅ 歌單創建、編輯、刪除
- ✅ 多條件查詢和分頁支持
- ✅ 官方推薦歌單管理

### 👥 用戶服務

- ✅ 用戶註冊/登錄
- ✅ JWT 安全認證
- ✅ 用戶信息管理
- ✅ 頭像上傳和管理
- ✅ 密碼安全加密
- ✅ 基於角色的權限控制

### 💬 社交互動

- ✅ 歌曲評論和回覆
- ✅ 評論點讚機制
- ✅ 歌曲收藏功能
- ✅ 用戶反饋收集

### 📱 多端支持

- ✅ 統一 RESTful API
- ✅ 設備類型自動識別
- ✅ 設備使用信息記錄
- ✅ 差異化服務支持

### ⚡ 高級特性

- ✅ 接口防抖機制（基於 Redis）
- ✅ 分佈式緩存優化
- ✅ MinIO 對象存儲
- ✅ 用戶行為分析
- ✅ 設備使用情況監控

### 🔒 安全防護

- ✅ JWT 無狀態認證
- ✅ 接口權限控制
- ✅ 參數全面校驗
- ✅ SQL 注入防護
- ✅ XSS 攻擊防護

---

## 專案特色

### 1. 分層架構設計

採用經典的 **三層架構**，實現清晰的代碼分層：

```
Controller (控制層) ← 調用 → Service (業務層) ← 調用 → Mapper (數據層)
```

### 2. 統一異常處理

使用 `@ControllerAdvice` 實現全局異常處理，統一錯誤響應格式：

```java
@ExceptionHandler(BusinessException.class)
public Result handleBusinessException(BusinessException ex) {
    log.error("業務異常：{}", ex.getMessage(), ex);
    return new Result(ex.getCode(), ex.getMessage(), null);
}
```

### 3. 智能防抖機制

基於註解的接口防抖功能，防止惡意請求和重複提交：

```java
@RequestDebounce(key = "sendCode", expire = 60, message = "操作過於頻繁")
public Result sendVerificationCode(String email) {
    // 業務邏輯
}
```

### 4. 分佈式緩存方案

基於 Redis 的分佈式緩存，支持多實例環境下的數據一致性：

```java
@Cacheable(value = "song", key = "#id")
public SongVO getSongById(Long id) {
    // 業務邏輯
}
```

### 5. 性能優化

- 數據庫查詢優化（索引、分頁）
- Redis 熱點數據緩存
- 連接池優化（Druid）
- 異步任務處理

---

## 技術棧

### 核心框架

| 技術 | 版本 | 說明 |
|------|------|------|
| Spring Boot | 3.2.0 | 後端框架 |
| Java | 17 | 編程語言 |
| Maven | 3.6+ | 項目構建工具 |

### 數據存儲

| 技術 | 版本 | 說明 |
|------|------|------|
| MySQL | 8.0+ | 關係型數據庫 |
| Redis | 7.0+ | 內存數據庫/緩存 |
| MinIO | 最新版 | 分佈式對象存儲 |

### 安全認證

| 技術 | 說明 |
|------|------|
| JWT | 無狀態身份認證 |
| Spring Security | 安全框架 |
| RSA | 非對稱加密 |

### 工具庫

| 依賴 | 說明 |
|------|------|
| MyBatis-Plus | 增強型 ORM 框架 |
| Lombok | 代碼簡化工具 |
| Hutool | Java 工具庫 |
| Druid | 高性能數據庫連接池 |
| Swagger | API 文檔生成 |
| Validation | 參數校驗 |

---

## 專案架構

### 整體架構

```
┌─────────────────────────────────────────────────────────┐
│                   Controller Layer                       │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  User    │  │  Song    │  │ Playlist │              │
│  │Controller│  │Controller│  │Controller│              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                        ↓ 調用
┌─────────────────────────────────────────────────────────┐
│                    Service Layer                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  User    │  │  Song    │  │ Playlist │              │
│  │ Service  │  │ Service  │  │ Service  │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                        ↓ 調用
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

### 三層架構

本項目採用經典的 **三層架構** 模式：

- **Controller（控制層）**：處理 HTTP 請求和響應
  - `controller/` - REST 控制器
  - 負責參數校驗、結果包裝

- **Service（業務層）**：處理核心業務邏輯
  - `service/` - 業務接口
  - `service/impl/` - 業務實現
  - 拋出 `BusinessException` 處理業務異常

- **Mapper（數據層）**：處理數據庫操作
  - `mapper/` - MyBatis Mapper 接口
  - `entity/` - 數據庫實體類

---

## 快速開始

### 前提條件

- JDK 17 或更高版本
- MySQL 8.0+
- Redis 7.0+
- MinIO 最新版本
- Maven 3.6+

### 安裝步驟

1. **克隆倉庫**

```bash
# GitHub
git clone https://github.com/AmisKwok/vibe-music-server.git

# 或 Gitee（國內用戶推薦）
git clone https://gitee.com/AmisKwok/vibe-music-server.git

cd vibe-music-server
```

2. **數據庫初始化**

```sql
CREATE DATABASE vibe_music CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. **配置環境變量**

```bash
# 複製配置文件模板
cp src/main/resources/application.yml.template src/main/resources/application.yml

# 編輯 application.yml 文件，配置數據庫、Redis、MinIO 等信息
```

`application.yml` 主要配置項：

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

4. **構建項目**

```bash
mvn clean package -DskipTests
```

5. **啟動服務**

```bash
java -jar target/vibe-music-server-*.jar
```

6. **訪問 API 文檔**

啟動成功後，訪問以下地址查看 API 文檔：

```
http://localhost:8080/swagger-ui.html
```

---

## 配置說明

### 應用配置

| 配置項 | 說明 | 默認值 |
|--------|------|--------|
| `server.port` | 服務端口 | 8080 |
| `spring.application.name` | 應用名稱 | vibe-music-server |

### 數據庫配置

| 配置項 | 說明 | 示例值 |
|--------|------|--------|
| `spring.datasource.url` | MySQL 連接地址 | `jdbc:mysql://localhost:3306/vibe_music` |
| `spring.datasource.username` | 數據庫用戶名 | `root` |
| `spring.datasource.password` | 數據庫密碼 | `password` |

### Redis 配置

| 配置項 | 說明 | 示例值 |
|--------|------|--------|
| `spring.data.redis.host` | Redis 主機地址 | `localhost` |
| `spring.data.redis.port` | Redis 端口 | `6379` |
| `spring.data.redis.password` | Redis 密碼 | `your_password` |

### MinIO 配置

| 配置項 | 說明 | 示例值 |
|--------|------|--------|
| `minio.endpoint` | MinIO 服務地址 | `http://localhost:9000` |
| `minio.accessKey` | 訪問密鑰 | `minioadmin` |
| `minio.secretKey` | 密鑰 | `minioadmin` |
| `minio.bucketName` | 存儲桶名稱 | `vibe-music` |

### JWT 配置

| 配置項 | 說明 | 示例值 |
|--------|------|--------|
| `jwt.secret` | JWT 密鑰 | `your-secret-key` |
| `jwt.expiration` | Token 有效期（毫秒） | `86400000` |

### RSA 配置

| 配置項 | 說明 | 示例值 |
|--------|------|--------|
| `rsa.public-key` | RSA 公鑰 | `-----BEGIN PUBLIC KEY-----...` |
| `rsa.private-key` | RSA 私鑰 | `-----BEGIN PRIVATE KEY-----...` |

---

## 構建和部署

### 本地開發

```bash
# 編譯項目
mvn clean compile

# 運行測試
mvn test

# 啟動應用（開發模式）
mvn spring-boot:run
```

### 生產構建

```bash
# 構建可執行 JAR
mvn clean package -DskipTests

# 構建產物位於 target/ 目錄
ls -lh target/vibe-music-server-*.jar
```

### Docker 部署

項目支持 Docker 容器化部署：

```bash
# 構建 Docker 映像
docker build -t vibe-music-server:latest .

# 運行容器
docker run -d \
  --name vibe-music-server \
  -p 8080:8080 \
  -v /path/to/application.yml:/app/application.yml \
  vibe-music-server:latest
```

### Docker Compose

使用 Docker Compose 一鍵部署所有服務：

```bash
# 啟動所有服務
docker-compose up -d

# 查看日誌
docker-compose logs -f

# 停止服務
docker-compose down
```

---

## 更新日誌

### v1.0.0 (2026-03-05)

**新增功能**
- ✨ 採用 CC BY-NC-SA 4.0 開源協議
- ✨ 優化項目架構（Service 層不再返回 Result）
- ✨ 添加全局異常處理器
- ✨ 實現 BusinessException 業務異常類
- ✨ 添加 StartupBanner 啟動橫幅
- ✨ 實現 RsaConsoleUtil 控制台加密工具
- ✨ 完善中文註釋

**問題修復**
- 🐛 修復 Service 層耦合 Result 的架構問題
- 🐛 修復 RSA 密鑰讀取問題
- 🐛 修復 MyBatis-Plus 實體類註解缺失問題

**優化改進**
- ♻️ 重構所有 Service 接口和實現類
- ♻️ 重構所有 Controller 控制器
- ♻️ 優化代碼結構和註釋
- ⚡ 性能優化和異常處理改進

### v0.1.0 (2026-01-23)

**首次發布**
- 🎉 基礎音樂管理功能
- 🎨 現代化 RESTful API 設計
- 💾 MySQL + Redis + MinIO 存儲支持
- 🔐 JWT 安全認證
- 👤 用戶認證系統

---

## 開發路線圖

### 近期計劃 (v1.1.0)

- [ ] 歌詞管理功能
- [ ] 音樂推薦算法
- [ ] 播放歷史記錄
- [ ] 用戶行為分析
- [ ] 歌單分享功能

---

## 貢獻指南

我們歡迎所有形式的貢獻！

### 如何貢獻

1. **Fork 倉庫**
```bash
git clone https://github.com/AmisKwok/vibe-music-server.git
```

2. **創建功能分支**
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

5. **創建 Pull Request**

### 提交規範

使用 [Conventional Commits](https://www.conventionalcommits.org/) 規範：

- `✨ Add:` 新功能
- `🐛 Fix:` 修復問題
- `♻️ Refactor:` 代碼重構
- `📝 Docs:` 文檔更新
- `🎨 Style:` 代碼格式調整
- `⚡ Perf:` 性能優化
- `✅ Test:` 測試相關

### 代碼審查

所有 PR 都需要經過代碼審查：
1. 代碼風格檢查
2. 功能測試
3. 性能評估
4. 文檔完整性

---

## 許可證

本項目採用 **Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License (CC BY-NC-SA 4.0)** 許可協議。

### 許可證特點

| 權限 | 條件 | 限制 |
|------|------|------|
| ✅ 共享 | 📝 署名 | ❌ 禁止商業使用 |
| ✅ 修改 | 🔄 相同方式共享 | ❌ 無附加限制 |

### 簡單來說

**你可以：**
- ✅ 複製、分發本作品
- ✅ 修改、轉換或基於本作品進行創作
- ✅ 用於學習、研究和個人使用

**你必須：**
- 📝 保留版權聲明
- 📝 提供許可協議鏈接
- 📝 說明是否進行了修改
- 🔄 使用相同協議分發衍生作品

**你不能：**
- ❌ 用於商業目的
- ❌ 銷售本軟件或其修改版本
- ❌ 通過本軟件獲取商業利益

完整協議文本請查看 [LICENSE](LICENSE) 文件。

---

## 聯繫方式

### 項目地址

- **GitHub**: https://github.com/AmisKwok/vibe-music-server
- **Gitee**: https://gitee.com/AmisKwok/vibe-music-server

### 問題反饋

- **GitHub Issues**: https://github.com/AmisKwok/vibe-music-server/issues
- **Gitee Issues**: https://gitee.com/AmisKwok/vibe-music-server/issues

### 作者

- **AmisKwok**

---

## 致謝

感謝所有為這個項目做出貢獻的人！

---

<p align="center">
  <strong>享受音樂，享受生活！🎧</strong>
</p>

<p align="center">
  如果這個項目對你有幫助，請給一個 ⭐️ Star 支持一下！
</p>
