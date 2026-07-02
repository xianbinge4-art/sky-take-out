package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "c端-订单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @PostMapping("/submit")
    @ApiOperation(value = "用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单: {}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);

    }


    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }


    /*查询历史订单
     * */

    @GetMapping("/historyOrders")
    @ApiOperation(value = "历史订单查询")
    public Result<PageResult> historyOrders(OrdersPageQueryDTO ordersPageQueryDTO) {

        PageResult pageResult = orderService.historyOrders(ordersPageQueryDTO);
        return Result.success(pageResult);
    }


    @GetMapping("/orderDetail/{id}")
    @ApiOperation(value = "订单详情查询")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        OrderVO orderVO = orderService.orderDetail(id);
        return Result.success(orderVO);
    }


    @PutMapping("/cancel/{id}")
    @ApiOperation(value = "取消订单")
    public Result cancel(@PathVariable Long id) {
        log.info("取消订单: {}", id);
        orderService.cancelById(id);
        return Result.success();
    }


    @PostMapping("/repetition/{id}")
    @ApiOperation(value = "重复下单")
    public Result repetition(@PathVariable Long id) {
        log.info("重复下单: {}", id);
        orderService.repetition(id);
        return Result.success();
    }


    @GetMapping("/reminder/{id}")
    @ApiOperation(value = "客户催单")
    public Result reminder(@PathVariable Long id) {
        log.info("客户催单: {}", id);
        orderService.reminder(id);
        return Result.success();

    }
}