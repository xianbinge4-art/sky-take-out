package com.sky.tsak;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;


    //每分钟执行一次
    //查询待付款订单
    //超时未付款订单
    //更新订单状态为已取消
    //设置取消原因为超时未付款
    @Scheduled(cron = "0 * * * * *")
    public void processTimeOutOrder() {
        log.info("定时处理超时订单,{}", LocalDateTime.now());
        //查询待付款订单
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orders = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, time);
        if (orders != null && !orders.isEmpty()) {
            //更新订单状态为已取消
            orders.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("超时未付款");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            });


        }


    }

    //每天凌晨一点执行一次
    //查询派送中的订单
    //超时未派送订单
    //更新订单状态为已完成
    //设置派送时间当前时间
    @Scheduled(cron = "0 1 1 * * ?")
    public void processTimeoutDelivery() {
        log.info("定时处理超时派送订单,{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, time);
        if (orders != null && !orders.isEmpty()) {
            //更新订单状态为已完成
            orders.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(order);
            });


        }
    }
}
