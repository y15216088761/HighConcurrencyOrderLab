package com.example.order.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.order.order.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Update("UPDATE orders SET status = 'PAID', updated_at = CURRENT_TIMESTAMP " +
            "WHERE id = #{orderId} AND status = 'PENDING_PAYMENT'")
    int markPaid(@Param("orderId") Long orderId);

    @Update("UPDATE orders SET status = 'CANCELLED', updated_at = CURRENT_TIMESTAMP " +
            "WHERE id = #{orderId} AND status = 'PENDING_PAYMENT'")
    int markCancelled(@Param("orderId") Long orderId);
}
