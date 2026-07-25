# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 构建命令

```bash
# 全量编译（跳过测试）
mvn clean install -DskipTests

# 启动单个服务（在模块目录下执行）
cd service-xxx && mvn spring-boot:run

# 编译单个模块（带依赖）
mvn install -pl common/common-xxx -am -DskipTests
```

项目没有 Maven Wrapper，需要本地安装 Maven 3.8+ 和 JDK 21。

## 技术架构

Java 21 微服务脚手架，基于 Spring Boot 3.2.2 + Spring Cloud Alibaba 2023.0.1.2 + Dubbo 3.2.10。核心基础设施是 Nacos（服务发现 + 配置中心）。命名空间 `cn.hollis.nft.turbo` 继承自原 NFTurbo 项目。

### 运行时的模块依赖链

```
common-base          ← 零内部依赖：异常、响应信封、工具类、状态机
  ├─ common-api      ← API 契约：Request/Response DTO、Facade 接口（按业务域分包）
  ├─ common-rpc      ← Dubbo + @Facade AOP（参数校验、异常转响应、计时日志）
  ├─ common-web      ← Spring Web + GlobalWebExceptionHandler + Result 响应封装
  ├─ common-cache    ← Redisson + JetCache + Caffeine（独立叶子节点）
  ├─ common-datasource ← MyBatis-Plus + Druid + ShardingSphere（独立叶子节点）
  ├─ common-lock     ← @DistributeLock 注解（依赖 common-cache）
  └─ common-sa-token ← Sa-Token 认证（尚无 Java 源码，仅依赖声明）

common-config        ← Nacos 服务发现 + 配置中心（独立，每个服务都会引入）

gateway              ← Spring Cloud Gateway（端口 8081，依赖 common-config + common-base）
service-auth         ← 认证服务（端口 8082，Dubbo 调用 service-user）
service-user         ← 用户服务（端口 8083，有 MySQL 数据库）
service-goods        ← 商品服务（端口 8084，有 MySQL 数据库）
service-template     ← 新服务复制模板
```

### 核心设计模式

**双层异常处理**：
- Dubbo 服务端：`@Facade` 注解标记 Facade 方法，由 `FacadeAspect` 切面拦截。对参数调用 `BeanValidator.validateObject()` 校验，捕获 `BizException`/`SystemException` 后自动转换为 `BaseResponse`（success=false，带 code + message），并记录方法耗时日志。
- Web 控制器端：`GlobalWebExceptionHandler`（`@ControllerAdvice`）捕获 `MethodArgumentNotValidException` → 400、`BizException`/`SystemException` → 200 + Result（success=false）、`Throwable` → 200 + 兜底消息。

**响应信封**：
- RPC 层返回值统一为 `BaseResponse` 子类：`SingleResponse<T>`、`MultiResponse<T>`、`PageResponse<T>`（均含 success/responseCode/responseMessage）。
- Web 层使用 `Result`/`MultiResult`（含 success/code/message）。

**实体基类**：所有数据库实体继承 `BaseEntity`，内置 `id`、`deleted`（`@TableLogic` 软删除）、`lockVersion`（`@Version` 乐观锁）、`gmtCreate`、`gmtModified`。`DataObjectHandler` 自动填充 gmtCreate/gmtModified/deleted/lockVersion。

**DDD 分层约定**（每个 service-xxx 模块）：
```
cn.hollis.nft.turbo.{service}/
├── XxxApplication.java         ← @SpringBootApplication + @EnableDubbo
├── facade/                     ← @DubboService(version="1.0.0") 实现 common-api 中的接口
├── domain/entity/              ← 实体 extends BaseEntity
├── domain/entity/convertor/    ← MapStruct INSTANCE 单例转换器
├── domain/service/             ← 领域服务逻辑
├── domain/listener/            ← 事件监听
├── infrastructure/mapper/      ← MyBatis Mapper
├── infrastructure/exception/   ← 模块级错误码
└── infrastructure/config/      ← 模块级 Spring 配置
```

**服务间调用**：通过 `@DubboReference(version = "1.0.0")` 注入 `common-api` 中定义的 Facade 接口。

### 配置管理

所有中间件地址集中在 `common/common-base/src/main/resources/base.yml`，以 `nft.turbo.*` 为前缀。各模块的 YAML（如 `datasource.yml`、`rpc.yml`、`cache.yml`）通过 `${nft.turbo.mysql.url}` 占位符引用。Nacos 配置中心用于运行时覆盖。

**修改环境配置的入口**：编辑 `base.yml` 中的 Nacos / MySQL / Redis 连接信息。

### 新增服务步骤

1. 复制 `service-template` → `service-xxx`
2. 修改子模块 `pom.xml` 的 `artifactId` 和 `application.name`
3. 重命名包 `cn.hollis.nft.turbo.template` → `cn.hollis.nft.turbo.xxx`
4. 修改 `XxxApplication` 中 `scanBasePackages` 为 `"cn.hollis.nft.turbo.xxx"`（只扫描自身包，不全局扫描）
5. 在根 `pom.xml` 的 `<modules>` 中添加 `service-xxx`

### 关键依赖版本速查

| 技术 | 版本 |
|------|------|
| Lombok | 1.18.30 |
| MapStruct | 1.6.0.Beta1（注解处理器 1.5.5.Final） |
| MyBatis-Plus | 3.5.5 |
| Druid | 1.2.20 |
| Redisson | 3.24.3 |
| JetCache | 2.7.5 |
| Sa-Token | 1.37.0 |
