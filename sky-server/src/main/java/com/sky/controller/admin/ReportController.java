package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Api(tags = "订单报表")
public class ReportController {
    @Autowired
    private ReportService reportService;


    @GetMapping("turnoverStatistics")
    @ApiOperation(value = "营业额统计")
    public Result<TurnoverReportVO>  turnoverReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {
        TurnoverReportVO turnoverReportVO = reportService.turnoverReport(begin, end);

       return Result.success(turnoverReportVO);
    }



    @GetMapping("userStatistics")
    @ApiOperation(value = "用户统计")
    public Result<UserReportVO> userReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {
        UserReportVO userReportVO = reportService.userReport(begin, end);

       return Result.success(userReportVO);
    }

    @GetMapping("ordersStatistics")
    @ApiOperation(value = "订单统计")
    public Result<OrderReportVO> orderReport(  @DateTimeFormat(pattern = "yyyy-MM-dd")
                                               LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd")
                                               LocalDate end){
        OrderReportVO orderReportVO = reportService.orderReport(begin, end);
        return Result.success(orderReportVO);
    }



    @GetMapping("top10")
    @ApiOperation(value = "销售Top10商品统计")
    public Result<SalesTop10ReportVO> salesTop10Report(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {
        SalesTop10ReportVO salesTop10ReportVO = reportService.salesTop10Report(begin, end);
        return Result.success(salesTop10ReportVO);
    }


}
