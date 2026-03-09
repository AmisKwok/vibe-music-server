# Config Template 使用指南

## 概述
此文件夹包含所有环境配置的模板文件，用于快速创建新的配置环境。

## 文件结构
```
config-template/
├── dev/                 # 开发环境模板
│   ├── mysql.yml.template
│   ├── oss.yml.template  
│   ├── redis.yml.template
│   ├── mail.yml.template
│   └── page-helper.yml.template
├── local/               # 本地环境模板
├── preview/             # 预发布环境模板
└── prod/                # 生产环境模板
```

## 使用方法

### 1. 创建新环境配置
```bash
# 复制模板到实际配置文件夹
cp config-template/dev/*.template config/your-new-env/

# 重命名文件（移除.template后缀）
cd config/your-new-env
rename '.template' '' *.template
```

### 2. 配置说明
- **mysql.yml**: 数据库连接配置
- **oss.yml**: 对象存储配置 (支持MINIO/Aliyun/Tencent/Qiniu)
- **redis.yml**: Redis缓存配置
- **mail.yml**: 邮件服务配置
- **page-helper.yml**: MyBatis-Plus分页配置

### 3. 安全提示
- 不要将包含真实凭据的配置文件提交到Git
- 使用环境变量或密钥管理系统存储敏感信息
- 实际配置文件夹已从Git跟踪中排除

## 环境说明
- **dev**: 开发环境 - 用于日常开发测试
- **local**: 本地环境 - 个人开发机配置
- **preview**: 预发布环境 - 接近生产环境的测试
- **prod**: 生产环境 - 线上正式环境配置