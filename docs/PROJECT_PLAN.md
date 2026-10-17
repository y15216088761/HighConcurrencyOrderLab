# 高并发订单系统学习项目方案

> 项目类型：技术实验型项目
>
> 项目目标：以一个极简订单业务为载体，系统学习 MyBatis、事务、并发控制、Redis、消息队列、多实例部署、压测与监控。
>
> 项目原则：业务做小，技术做深；每个阶段都保留可运行版本、压测数据和问题复盘。

---

## 1. 项目定位

项目暂定名称：`High-Concurrency Order Lab`

核心业务链路：

```text
用户 → 浏览商品 → 创建订单 → 扣减库存 → 模拟支付 → 查询订单
```

本项目不追求完整电商能力，而是作为高并发与分布式技术实验场。

### 第一版包含

- 用户注册、登录
- 商品查询
- 商品库存管理
- 创建订单
- 查询订单详情
- 查询订单列表
- 模拟支付
- 取消订单
- 取消订单后的库存回滚

### 暂不包含

- 购物车
- 优惠券
- 真实支付
- 复杂权限体系
- 营销活动
- 推荐系统
- 前端商城
- 微服务全面拆分
- 第三方登录

---

## 2. 技术栈

### 基础技术

- Java 21
- Spring Boot 3.x
- Maven
- MySQL
- MyBatis-Plus
- MyBatis XML
- Flyway

### 中间件与部署

- Redis
- RabbitMQ
- Nginx
- Docker Compose

### 测试与观测

- JUnit 5
- Mockito
- MockMvc
- JMeter 或 Gatling
- Spring Boot Actuator
- Micrometer
- Prometheus + Grafana（后续阶段）

### 技术使用原则

- 普通 CRUD 使用 MyBatis-Plus
- 核心业务 SQL 使用 MyBatis XML 手写
- 库存扣减、订单状态变更必须理解并验证 SQL 和事务边界
- 不一开始引入 Redis、MQ 和微服务，按阶段递进

---

## 3. 初版业务模型

### 用户

- 注册
- 登录
- 查询个人信息

### 商品

- 查询商品详情
- 查询商品列表
- 创建商品
- 修改商品价格和库存
- 上架/下架

### 订单

- 创建订单
- 查询订单详情
- 分页查询个人订单
- 取消未支付订单
- 更新订单状态

### 支付

- 模拟支付成功
- 防止重复支付
- 支付成功后更新订单状态

---

## 4. 初版数据库设计

### `user`

```text
id              BIGINT PRIMARY KEY
username        VARCHAR(64) UNIQUE NOT NULL
password        VARCHAR(255) NOT NULL
status          TINYINT NOT NULL
created_at      DATETIME NOT NULL
updated_at      DATETIME NOT NULL
```

### `product`

```text
id              BIGINT PRIMARY KEY
name            VARCHAR(128) NOT NULL
price           DECIMAL( BigDecimal ) NOT NULL
stock           INT NOT NULL
status          TINYINT NOT NULL
version         BIGINT NOT NULL DEFAULT 0
created_at      DATETIME NOT NULL
updated_at      DATETIME NOT NULL
```

> 实际建表时价格字段使用 `DECIMAL(10,2)`，Java 使用 `BigDecimal`。

### `orders`

```text
id              BIGINT PRIMARY KEY
order_no        VARCHAR(64) UNIQUE NOT NULL
user_id         BIGINT NOT NULL
total_amount    DECIMAL(10,2) NOT NULL
status          VARCHAR(32) NOT NULL
request_token   VARCHAR(64) NOT NULL
created_at      DATETIME NOT NULL
updated_at      DATETIME NOT NULL
```

建议建立唯一索引：

```text
(user_id, request_token)
```

用于防止同一用户重复提交相同请求。

### `order_item`

```text
id              BIGINT PRIMARY KEY
order_id        BIGINT NOT NULL
product_id      BIGINT NOT NULL
product_name    VARCHAR(128) NOT NULL
price           DECIMAL(10,2) NOT NULL
quantity        INT NOT NULL
amount          DECIMAL(10,2) NOT NULL
```

订单明细保存商品名称和价格快照，避免商品后续修改影响历史订单。

### `payment`

```text
id              BIGINT PRIMARY KEY
order_id        BIGINT UNIQUE NOT NULL
payment_no      VARCHAR(64) UNIQUE NOT NULL
amount          DECIMAL(10,2) NOT NULL
status          VARCHAR(32) NOT NULL
paid_at         DATETIME NULL
created_at      DATETIME NOT NULL
updated_at      DATETIME NOT NULL
```

### 初版状态

```text
订单：PENDING_PAYMENT → PAID → COMPLETED
订单：PENDING_PAYMENT → CANCELLED
支付：UNPAID → PAID
```

状态变更必须通过明确的状态机或条件更新，不能任意覆盖状态。

---

## 5. 模块结构

```text
order-lab/
├── src/main/java/
│   └── com.example.order/
│       ├── common/
│       ├── config/
│       ├── user/
│       ├── product/
│       ├── order/
│       ├── payment/
│       └── infrastructure/
├── src/main/resources/
│   ├── mapper/
│   ├── db/migration/
│   └── application.yml
├── src/test/
├── docker/
├── docs/
│   └── experiments/
├── scripts/
└── README.md
```

推荐按业务模块组织代码，而不是只按 Controller、Service、Mapper 组织全部代码。

---

## 6. 分阶段学习路线

## 阶段一：普通单体版本

目标：完成一个可运行的订单闭环。

学习内容：

- Spring Boot 项目结构
- Controller、Service、Mapper 分层
- DTO、VO 和实体对象
- MyBatis-Plus 基础 CRUD
- MyBatis XML 自定义 SQL
- 参数校验
- 全局异常处理
- 统一响应结构
- MySQL 表设计、索引和外键取舍
- Spring 声明式事务
- Flyway 数据库迁移

完成标准：

- 可以创建商品和库存
- 可以创建订单并生成订单明细
- 下单成功后库存正确减少
- 支付和取消订单流程可用
- 关键接口具备单元测试和集成测试

## 阶段二：并发扣库存

目标：复现并解决库存超卖。

实验顺序：

```text
无锁扣库存
→ 复现超卖
→ SELECT FOR UPDATE
→ 乐观锁 version
→ 条件更新
→ 对比不同方案
```

重点 SQL：

```sql
UPDATE product
SET stock = stock - #{quantity},
    version = version + 1
WHERE id = #{productId}
  AND stock >= #{quantity}
  AND version = #{version};
```

学习内容：

- 多线程并发测试
- 事务隔离级别
- 脏读、不可重复读、幻读
- 悲观锁与乐观锁
- 行锁与索引
- affected rows 判断
- 锁竞争和事务耗时

完成标准：

```text
库存为 100，1000 个请求并发购买：
- 成功数量不超过 100
- 库存不会小于 0
- 失败请求返回明确业务错误
```

## 阶段三：请求幂等

目标：处理重复提交、超时重试和客户端重试。

学习内容：

- 幂等号设计
- 唯一索引保证幂等
- Redis SetNX
- 重复支付处理
- 数据库约束兜底
- 接口超时后的重试语义

建议设计：

```text
(user_id, request_token) 唯一
```

同一个用户使用同一个 `request_token` 重复提交时，只创建一个订单。

## 阶段四：Redis 缓存

目标：降低商品查询压力，并理解缓存一致性问题。

学习内容：

- String、Hash、Set、List
- Cache Aside Pattern
- 缓存过期时间
- 缓存穿透
- 缓存击穿
- 缓存雪崩
- 热点 Key
- 缓存与数据库一致性
- Redis 分布式锁

初始策略：

```text
查询商品：Redis → MySQL
更新商品：MySQL → 删除缓存
```

先实现简单方案，再通过实验分析并发更新下的一致性问题。

## 阶段五：多实例部署

目标：让单体服务支持横向扩展。

部署结构：

```text
Nginx
 ├── order-app-1
 ├── order-app-2
 └── order-app-3
      ├── MySQL
      └── Redis
```

学习内容：

- Dockerfile
- Docker Compose
- Nginx 负载均衡
- 无状态应用
- Session 与登录状态共享
- 本地文件依赖问题
- 多实例定时任务重复执行
- 分布式锁
- 数据库连接池
- 多实例日志和实例标识

完成标准：

- 任意一个应用实例停止后，请求仍能访问
- 应用不依赖本地内存保存关键状态
- 多实例不会重复执行同一项定时任务
- 所有实例连接同一个 Redis 和 MySQL

## 阶段六：消息队列

目标：学习异步处理、削峰和最终一致性。

学习内容：

- RabbitMQ Exchange、Queue、Routing Key
- 生产者确认
- 消费者确认
- 重试机制
- 死信队列
- 消费幂等
- 消息丢失和重复消费
- 最终一致性

演进流程：

```text
同步创建订单
→ 写入待处理订单
→ 发送下单消息
→ 消费者处理库存
→ 更新订单状态
```

注意：引入 MQ 后必须重新设计异常补偿，不能只把同步代码简单搬到消费者中。

## 阶段七：限流、降级与监控

学习内容：

- 固定窗口、滑动窗口
- 令牌桶和漏桶
- 接口限流
- 服务降级
- 熔断思路
- 健康检查
- 结构化日志
- Trace ID
- Actuator 和 Micrometer
- Prometheus 指标
- Grafana 看板

重点指标：

```text
QPS
平均响应时间
P95 / P99 延迟
错误率
数据库连接数
Redis 命中率
MQ 消费延迟
库存扣减失败数
订单创建成功数
```

---

## 7. 每个实验的固定记录格式

每个实验必须留下以下内容：

```text
1. 实验目标
2. 初始实现
3. 问题复现方式
4. 压测脚本
5. 原始数据
6. 改进方案
7. 改进后数据
8. 原理分析
9. 方案缺点
10. 下一步优化方向
```

实验文档放在：

```text
docs/experiments/
```

建议文件：

```text
01-basic-order.md
02-stock-overselling.md
03-idempotency.md
04-redis-cache.md
05-multi-instance.md
06-message-queue.md
07-rate-limit.md
08-monitoring.md
```

---

## 8. 第一版完成标准

V1 暂不追求高并发，完成以下目标即可：

- 项目可以通过 Docker 或本地环境启动
- 使用 MyBatis-Plus 完成基础 CRUD
- 使用 MyBatis XML 编写核心库存 SQL
- 完成商品、库存、订单、支付表
- 完成普通下单流程
- 事务边界清晰
- 有数据库初始化和迁移脚本
- 有统一异常处理和参数校验
- 有基础接口测试
- 能写并发测试
- 能复现并解决库存超卖
- README 包含启动和测试说明

---

## 9. 推荐开发顺序

```text
1. 初始化 Spring Boot + Maven 项目
2. 配置 MySQL、MyBatis-Plus、Flyway
3. 创建用户、商品、订单相关表
4. 实现商品 CRUD
5. 实现普通下单流程
6. 实现支付和取消订单
7. 补充事务与异常处理
8. 编写基础测试
9. 故意制造库存超卖
10. 使用乐观锁解决超卖
11. 记录第一份实验报告
```

第一阶段不要提前加入 Redis、RabbitMQ、Docker 多实例和微服务。先确保数据库事务、库存 SQL 和订单状态流转真正理解，再逐步增加复杂度。

---

## 10. 项目最终目标

最终形成一个可以逐步演进的技术实验项目：

```text
基础单体
  ↓
事务与并发控制
  ↓
Redis 缓存
  ↓
幂等与分布式锁
  ↓
多实例部署
  ↓
MQ 异步削峰
  ↓
限流、降级、监控
  ↓
局部服务拆分
```

项目的核心成果不是业务数量，而是能够说明：

- 问题如何产生
- 如何通过压测复现
- 技术方案为什么有效
- 方案的代价和边界是什么
- 改造前后的数据有什么变化
