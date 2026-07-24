# 微服务脚手架 (Microservice Scaffold)

基于 **Spring Cloud Alibaba** 的微服务最小脚手架，从 NFTurbo 项目中提取核心框架。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 21 | JDK 版本 |
| Spring Boot | 3.2.2 | 基础框架 |
| Spring Cloud | 2023.0.0 | 微服务框架 |
| Spring Cloud Alibaba | 2023.0.1.2 | 阿里巴巴微服务组件 |
| Dubbo | 3.2.10 | RPC 远程调用 |
| Nacos | 3.x | 服务发现 + 配置中心 |

## 项目结构

```
microservice-scaffold/
├── pom.xml                          # 根 POM（统一版本管理）
├── common/                          # 公共模块
│   ├── pom.xml                      # 公共模块父 POM
│   ├── common-base/                 # 基础组件：工具类、异常、响应封装
│   ├── common-config/               # Nacos 服务发现 + 配置中心
│   ├── common-api/                  # API 接口定义（Request/Response/Service）
│   ├── common-rpc/                  # Dubbo RPC + Facade 切面
│   └── common-web/                  # Web 层：统一异常处理 + 响应封装
├── service-template/                # 微服务模板（复制此模块创建新服务）
│   ├── pom.xml
│   └── src/main/
│       ├── java/.../TemplateApplication.java
│       └── resources/bootstrap.yml
└── README.md
```

## 核心模块依赖关系

```
common-base (零内部依赖)
    ↑
    ├── common-api       → 只需 common-base
    ├── common-rpc       → 只需 common-base + Dubbo
    └── common-web       → 只需 common-base + Spring Web

common-config (零内部依赖，独立引入)
```

## 快速开始

### 1. 环境准备

- JDK 21+
- Maven 3.8+
- Nacos 3.x（本地开发需关闭鉴权或配置用户名密码）

### 2. 修改配置

编辑 `common/common-base/src/main/resources/base.yml`，将中间件地址改成你自己的：

```yaml
nft:
  turbo:
    nacos:
      server:
        url: localhost:8848      # Nacos 地址
    mysql:
      url: jdbc:mysql://localhost:3306/your_db
      username: root
      password: your_password
    redis:
      url: localhost
      port: 6379
      password: ''
```

### 3. 编译项目

```bash
cd microservice-scaffold
mvn clean install -DskipTests
```

### 4. 创建新服务

```bash
# 复制模板
cp -r service-template service-xxx

# 修改以下内容：
# 1. pom.xml → application.name
# 2. 包名 cn.hollis.nft.turbo.template → cn.hollis.nft.turbo.xxx
# 3. Application 类名和 scanBasePackages
# 4. 根 pom.xml 中添加 <module>service-xxx</module>
```

### 5. 启动服务

```bash
cd service-xxx
mvn spring-boot:run
```

## 服务开发规范

### DDD 分层架构

```
service-xxx/src/main/java/cn/hollis/nft/turbo/xxx/
├── XxxApplication.java         # 启动类
├── facade/                     # 门面层（Dubbo 服务暴露）
│   └── XxxFacadeServiceImpl.java
├── domain/                     # 领域层
│   ├── entity/                 # 实体
│   ├── service/                # 领域服务
│   └── listener/               # 事件监听
└── infrastructure/             # 基础设施层
    ├── mapper/                 # 数据库 Mapper
    └── config/                 # 配置类
```

### 启动类模板

```java
@SpringBootApplication(scanBasePackages = {"cn.hollis.nft.turbo.xxx"})
@EnableDubbo
public class XxxApplication {
    public static void main(String[] args) {
        SpringApplication.run(XxxApplication.class, args);
    }
}
```

## 按需引入扩展模块

脚手架仅包含最小核心模块。以下模块可按需从原项目引入：

| 模块 | 用途 | 依赖 |
|------|------|------|
| `common-cache` | Redis + 本地缓存（Redisson/JetCache/Caffeine） | common-base |
| `common-datasource` | 数据库 + MyBatis-Plus + ShardingSphere | common-base |
| `common-sa-token` | Sa-Token 认证授权 | common-base |
| `common-mq` | RocketMQ 消息队列 | common-base |
| `common-lock` | 分布式锁 | common-cache |
| `common-seata` | 分布式事务（Seata） | common-base |
| `common-es` | Elasticsearch 搜索 | common-base |
| `common-file` | 文件存储 | common-base |
| `common-sms` | 短信服务 | common-base |
| `common-job` | XXL-JOB 定时任务 | common-base |
| `common-limiter` | 限流（Sentinel） | common-base |
| `common-skywalking` | SkyWalking 链路追踪 | common-base |
| `common-prometheus` | Prometheus 监控指标 | common-base |

引入方式：将原项目对应模块的 pom.xml 和源码复制到 `common/` 目录下，然后在 `common/pom.xml` 中添加 `<module>` 即可。

## 启动顺序

1. **Nacos** — 服务发现与配置中心（必须先启动）
2. **你的服务** — 按依赖关系依次启动

## 常见问题

### Nacos 连接失败

检查 `base.yml` 中的 `nft.turbo.nacos.server.url` 是否正确。

Nacos 3.x 需要在 `application.properties` 中配置：
```properties
nacos.core.auth.enabled=false
nacos.core.api.compatibility.client.enabled=true
nacos.core.api.compatibility.console.enabled=true
```

### 编译报错

确保 JDK 21 已安装并配置正确：
```bash
java -version  # 应显示 21.x
```

### 启动报错 "No Token Found"

这说明请求没有携带 Authorization header，或者 common-web 中的 TokenFilter 已启用。
最小脚手架版本默认不包含 TokenFilter，如需要请引入 common-sa-token 模块。
