# 实验 01：库存超卖

## 实验目标

验证当前订单创建流程在并发请求下是否会出现库存超卖，并理解条件更新、事务和并发控制的作用。

## 当前实现

商品库存通过以下 SQL 扣减：

```sql
UPDATE product
SET stock = stock - #{quantity},
    version = version + 1
WHERE id = #{productId}
  AND status = 1
  AND stock >= #{quantity}
```

## 实验准备

- 创建一个库存为 100 的商品
- 创建一个可用用户
- 准备相同商品的并发下单请求
- 每个请求购买 1 件
- 使用同一个或不同的 `requestToken`，分别观察结果

## 并发测试

建议先使用 JUnit 多线程测试，之后再使用 JMeter 或 Gatling。

测试变量：

```text
库存数量：10
并发请求数：50
每次购买数量：1
并发线程数：50
```

## 需要记录的数据

```text
成功订单数：10
失败请求数：40
最终库存：0
数据库中的订单数：10
平均响应时间：待补充
P95：待补充
P99：待补充
```

## 实验结果

已通过 JUnit 并发集成测试：50 个并发请求竞争 10 件库存，最终成功 10 个，库存为 0，没有超卖。

测试文件：`order-service/src/test/java/com/example/order/order/StockOversellingIntegrationTest.java`

## 原理分析

待填写：

- 是否发生超卖？
- 条件 UPDATE 是否起到了作用？
- 事务边界是什么？
- `affected rows` 为 0 时发生了什么？
- 当前实现是否已经使用了乐观锁？

## 改进方案

待填写。

## 改造后数据

待填写。

## 方案代价与边界

待填写。

## 八股总结

### 一句话回答

待填写。

### 项目回答

待填写。

### 深入回答

待填写。
