# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 构建命令

```bash
# 全量编译（跳过测试）
mvn clean install -DskipTests

# 单个模块编译 + 自动安装依赖模块
mvn install -pl common/common-cache -am -DskipTests

# 启动单个服务（在模块目录下执行）
cd service-goods && mvn spring-boot:run

# 启动 Gateway
cd gateway && mvn spring-boot:run
```

项目没有 Maven Wrapper，需要本地安装 Maven 3.8+ 和 JDK 21。

## 技术架构

Java 21 微服务脚手架，基于 Spring Boot 3.2.2 + Spring Cloud Alibaba 2023.0.1.2 + Dubbo 3.2.10。

核心基础设施是 **Nacos**（服务发现 + 配置中心）。命名空间 `cn.hollis.nft.turbo` 继承自原 NFTurbo 项目。

### 运行时模块与端口

```
gateway          ← 8081, Spring Cloud Gateway + Sa-Token 鉴权
service-auth     ← 8082, 登录/注册 Web 控制器，通过 Dubbo 调用 service-user
service-user     ← 8083, 用户领域服务，有 MySQL 数据库
service-goods    ← 8084, 商品领域服务，有 MySQL 数据库
service-template ← 新服务复制模板（不可运行，需要改名）
```

Gateway 路由规则在 `gateway/src/main/resources/application.yml` 中定义，路径 `/auth/**` → `service-auth`、`/user/**` → `service-user`、`/goods/**` → `service-goods`。

### 模块依赖链

```
common-base               ← 零内部依赖：异常（ErrorCode→BizException/SystemException）、BaseResponse/SingleResponse/MultiResponse/PageResponse、
                             BaseRequest/PageRequest、BeanValidator、HttpUtils、SpringContextHolder、StateMachine 接口、线程池工具
  ├─ common-api            ← API 契约：按业务域分包（api/user/、api/goods/），
  │                          每域含 request/response/service/constant 子包
  ├─ common-rpc            ← Dubbo + @Facade AOP（参数校验→执行→异常转BaseResponse→计时日志）
  ├─ common-web            ← Spring Web + GlobalWebExceptionHandler + Result/MultiResult 响应封装
  ├─ common-cache          ← Redisson + JetCache + Caffeine（独立叶子节点）
  ├─ common-datasource     ← MyBatis-Plus + Druid + ShardingSphere + BaseEntity
  │                          实体基类（id/deleted=@TableLogic/lockVersion=@Version/gmtCreate/gmtModified）
  ├─ common-lock           ← @DistributeLock 注解（依赖 Redisson，通过 common-cache 引入）
  └─ common-sa-token        ← Sa-Token 认证（仅有 pom.xml 依赖声明，无 Java 源码）

common-config              ← Nacos 服务发现 + 配置中心（无 Java 源码，仅 config.yml + pom）
                            每个可运行模块都依赖它
```

### 核心设计模式

**双层异常处理**：
- **Dubbo 服务端**：`@Facade` 注解标记 Facade 方法，由 `FacadeAspect` 切面（`@Order(Integer.MIN_VALUE)`）拦截。对参数调用 `BeanValidator.validateObject()` 校验，捕获 `BizException`/`SystemException` 后自动转换为 `BaseResponse`（success=false，带 code + message），并记录方法耗时日志。
- **Web 控制器端**：`GlobalWebExceptionHandler`（`@ControllerAdvice`）捕获：
  - `MethodArgumentNotValidException` → 400 + 字段错误 Map
  - `BizException` → 200 + Result（success=false, code+message）
  - `SystemException` → 200 + Result（success=false, code+message）
  - `Throwable` → 200 + Result（兜底消息）

**响应信封**：
- RPC 层返回值统一为 `BaseResponse` 子类：`SingleResponse<T>`、`MultiResponse<T>`、`PageResponse<T>`（均含 success/responseCode/responseMessage）
- Web 层使用 `Result`/`MultiResult`（含 success/code/message）。`MultiResultConvertor` 负责两者之间的转换。

**实体基类**：所有数据库实体继承 `BaseEntity`（位于 `common-datasource`），内置 `id`（`@TableId` 自增）、`deleted`（`@TableLogic` 软删除填充值 0）、`lockVersion`（`@Version` 乐观锁填充值 0）、`gmtCreate`、`gmtModified`。`DataObjectHandler` 自动填充 gmtCreate/gmtModified/deleted/lockVersion。

**状态机**：`common-base` 提供 `StateMachine<STATE, EVENT>` 泛型接口和 `BaseStateMachine` 抽象类，定义了 `transition(state, event) → state` 契约，供各业务领域实现自己的状态流转逻辑。

**分布式锁**：`@DistributeLock` 注解（`common-lock`），由 `DistributeLockAspect`（`@Order(Integer.MIN_VALUE+1)`，在 `FacadeAspect` 之后执行）处理。支持 `scene`（业务场景）、`key`（固定 key）或 `keyExpression`（SpEL 表达式，如 `#id`、`#insertResult.id`）、`expireTime`（超时毫秒，默认-1 即看门狗自动续期）、`waitTime`（等待毫秒，默认-1 即一直阻塞直到获取锁）。底层基于 Redisson RLock。

**Gateway 鉴权（三层）**：`SaTokenConfig` 定义全局 `SaReactorFilter`，按 `登录校验 → 角色校验 → 权限校验` 三层逐级过滤：
1. 所有 `/**` 需登录（`StpUtil.checkLogin()`），`/auth/**` 和 `/favicon.ico` 放行
2. 角色校验按路径匹配（`StpUtil.checkRole("ADMIN")`），当前为注释状态
3. 权限校验按路径匹配（`StpUtil.checkPermission("goods:write")`），当前为注释状态
4. `StpInterfaceImpl` 提供权限/角色数据源（当前为空实现，标注 TODO 待业务填充）

### DDD 分层约定（每个 service-xxx 模块）

```
cn.hollis.nft.turbo.{service}/
├── XxxApplication.java         ← @SpringBootApplication(scanBasePackages = "cn.hollis.nft.turbo.{service}")
│                                  + @EnableDubbo，只扫描自身包，不全局扫描
├── facade/                     ← @DubboService(version="1.0.0") 实现 common-api 中定义的 Facade 接口
├── domain/entity/              ← 实体 extends BaseEntity
├── domain/entity/convertor/    ← MapStruct INSTANCE 单例转换器
├── domain/service/             ← 领域服务逻辑
├── domain/listener/            ← 事件监听
├── infrastructure/mapper/      ← MyBatis Mapper
├── infrastructure/exception/   ← 模块级错误码（实现 ErrorCode 接口）
└── infrastructure/config/      ← 模块级 Spring 配置
```

**服务间调用**：调用方通过 `@DubboReference(version = "1.0.0")` 注入 `common-api` 中定义的 Facade 接口。例如 `service-auth` 的 `AuthController` 通过 `@DubboReference` 调用 `UserFacadeService`。

### common-api 业务域分包约定

```
cn.hollis.nft.turbo.api.{domain}/
├── constant/       ← 枚举（GoodsType、GoodsState、UserRole、UserStateEnum 等）
├── request/        ← 请求 DTO + 子包 condition/（查询条件）
├── response/       ← 响应 DTO + 子包 data/（返回的数据对象）
├── model/          ← VO 对象（如 GoodsVO）
└── service/        ← Facade 接口（Dubbo 服务契约）
```

### 配置管理

**配置入口**：所有中间件地址集中在 `common/common-base/src/main/resources/base.yml`，以 `nft.turbo.*` 为前缀。

修改本地环境配置时，编辑该文件中的 Nacos / MySQL / Redis 等连接信息即可。

**配置引用链**：每个模块的 `bootstrap.yml` 通过 `spring.config.import` 导入多个 classpath YAML：
```
base.yml (中间件地址) → config.yml (Nacos 配置) → cache.yml / datasource.yml / rpc.yml
```

各技术模块的 YAML（`datasource.yml`、`rpc.yml`、`cache.yml`）通过 `${nft.turbo.mysql.url}` 占位符引用 base.yml 中的值。Nacos 配置中心用于运行时覆盖。

### 新增服务步骤

1. 复制 `service-template` → `service-xxx`
2. 修改子模块 `pom.xml` 的 `artifactId` 和 `application.name`（如 `scaffold-template` → `scaffold-xxx`）
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
| fastjson2 | 2.0.42 |
| Guava | 32.1.3-jre |
| Hutool | 5.8.22 |
| commons-lang3 | 3.14.0 |

### 错误码体系

`common-base` 的 `ErrorCode` 接口定义 `getCode()` + `getMessage()` 两个方法。预置实现：
- `BizErrorCode`（业务错误码枚举）
- `BlockErrorCode`（阻断错误码枚举）
- `RepoErrorCode`（仓储错误码，含 `DUPLICATE_KEY`）
- `ResponseCode`（响应状态码：SUCCESS / BIZ_ERROR / SYSTEM_ERROR）

每个服务模块可在自己的 `infrastructure/exception/` 下定义本模块错误码枚举，实现 `ErrorCode` 接口，并配套 XxxException 类。
