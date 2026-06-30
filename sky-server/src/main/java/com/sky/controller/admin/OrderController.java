package com.sky.controller.admin;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "管理端-订单管理")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 根据条件分页查询订单列表
     * @param ordersPageQueryDTO  查询条件（状态、订单号、手机号、下单时间范围、分页信息）
     * @return                    分页后的订单列表
     */
    @GetMapping("/conditionSearch")
    @ApiOperation(value = "根据条件分页查询订单")
    public Result<PageResult> conditionSearch( OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("根据条件分页查询订单: {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据订单 id 查询订单详情（含菜品明细）
     * @param id  订单 id
     * @return    订单详情（订单主信息 + 订单项列表）
     */
    @GetMapping("/details/{id}")
    @ApiOperation(value = "根据订单id查询订单详情")
    public Result<OrderVO> details(@PathVariable Long id) {
        log.info("根据订单id查询订单详情: {}", id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * 查询订单各状态的统计数量
     * @return  待接单、待派送、派送中的订单数量
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "查询订单统计信息")
    public Result<OrderStatisticsVO> statistics() {
        log.info("查询订单统计信息");
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 商家确认接单（订单状态 待接单 → 已接单）
     * @param ordersConfirmDTO  订单 id
     */
    @PutMapping("/confirm")
    @ApiOperation(value = "确认订单")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("确认订单: {}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * 商家拒单（订单状态 待接单 → 已取消，记录拒单原因和时间）
     * @param ordersRejectionDTO  订单 id + 拒单原因
     */
    @PutMapping("/rejection")
    @ApiOperation(value = "拒绝订单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) {
        log.info("拒绝订单: {}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }


    @PutMapping("/cancel")
    @ApiOperation(value = "取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) {
        log.info("取消订单: {}", ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }
    @PutMapping("/delivery/{id}")
    @ApiOperation(value = "派送订单")
    public Result delivery(@PathVariable Long id) {
        log.info("派送订单: {}", id);
        orderService.delivery(id);
        return Result.success();
    }


    @PutMapping("/complete/{id}")
    @ApiOperation(value = "完成订单")

    public Result complete(@PathVariable Long id) {
        log.info("完成订单: {}", id);
        orderService.complete(id);
        return Result.success();
    }
}