# 项目驱动学习路线

## 学习原则

以项目问题为主线，把八股知识放到实际代码和实验中理解：

```text
制造问题 → 压测复现 → 学习原理 → 修改代码 → 对比数据 → 总结复述
```

每个技术点都要形成三种理解：

1. 一句话定义
2. 项目中的具体应用
3. 原理、适用场景和代价

## 学习时间分配

```text
项目编码：50%
原理学习：30%
总结复述：20%
```

## 学习阶段

### 1. MySQL 与事务

项目实验：库存超卖、订单事务回滚。

重点知识：

- 索引和 B+ 树
- 聚簇索引
- MVCC
- 事务隔离级别
- 行锁
- 悲观锁和乐观锁
- EXPLAIN

### 2. Spring 事务

项目实验：订单创建失败时库存回滚。

重点知识：

- `@Transactional`
- AOP 代理
- 事务传播行为
- 隔离级别
- rollbackFor
- 自调用导致事务失效

### 3. 接口幂等

项目实验：重复提交同一个订单请求。

重点知识：

- 唯一索引
- 幂等号
- Redis SETNX
- 重试语义
- 分布式锁

### 4. Redis

项目实验：商品查询缓存和缓存失效。

重点知识：

- 常用数据结构
- RDB/AOF
- 过期策略
- 缓存一致性
- 缓存穿透、击穿、雪崩
- 热点 Key

### 5. JVM 与并发

项目实验：压测下观察线程、连接池和响应时间。

重点知识：

- 线程池
- synchronized
- CAS
- volatile
- JVM 内存区域
- GC
- 数据库连接池

### 6. 多实例部署

项目实验：两个应用实例通过 Nginx 接收请求。

重点知识：

- 无状态服务
- 负载均衡
- Session 共享
- 分布式锁
- 多实例定时任务
- 连接池配置

### 7. 消息队列

项目实验：异步下单、重试和重复消费。

重点知识：

- 消息确认
- 消息可靠性
- 重复消费
- 消费幂等
- 重试机制
- 死信队列
- 最终一致性

## 每个实验的固定模板

```markdown
# 实验名称

## 实验目标

## 初始实现

## 问题复现

## 压测数据

## 原理分析

## 解决方案

## 改造后数据

## 方案代价与边界

## 面试表达
```

## 推荐实验顺序

```text
01-stock-overselling
02-transaction-rollback
03-request-idempotency
04-redis-cache
05-redis-cache-breakdown
06-multi-instance
07-message-retry
08-rate-limit
09-monitoring
```

## Git 规则

每个实验使用独立分支：

```bash
git switch -c experiment/stock-overselling
```

完成后：

```bash
git add .
git commit -m "experiment: analyze stock overselling"
git push -u origin experiment/stock-overselling
```

验证完成后再合并到 `main`。
