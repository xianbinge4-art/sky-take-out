package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public TurnoverReportVO turnoverReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnoverSum = orderMapper.sumByMap(map);
            if (turnoverSum == null) {
                turnoverSum = 0.0;
            }
            turnoverList.add(turnoverSum);

        }
        return TurnoverReportVO.builder()
                .dateList(dateList.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .turnoverList(turnoverList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .build();
    }

    @Override
    public UserReportVO userReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("end", endTime);
            Integer totalUserCount = userMapper.getByMap(map);
            if (totalUserCount == null) {
                totalUserCount = 0;
            }
            totalUserList.add(totalUserCount);
            map.put("begin", beginTime);
            Integer newUserCount = userMapper.getByMap(map);
            if (newUserCount == null) {
                newUserCount = 0;
            }
            newUserList.add(newUserCount);
        }

        return UserReportVO.builder()
                .dateList(dateList.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .newUserList(newUserList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .totalUserList(totalUserList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .build();

    }

    @Override
    public OrderReportVO orderReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        Integer totalOrderCount = 0;
        Integer totalValidOrderCount = 0;
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap<>();
            map.put("begin", beginTime);
            map.put("end", endTime);
            Integer orderCount = orderMapper.getByMap(map);
            if (orderCount == null) {
                orderCount = 0;
            }
            totalOrderCount += orderCount;
            orderCountList.add(orderCount);
            map.put("status", Orders.COMPLETED);
            Integer validOrderCount = orderMapper.getByMap(map);
            if (validOrderCount == null) {
                validOrderCount = 0;
            }
            totalValidOrderCount += validOrderCount;
            validOrderCountList.add(validOrderCount);

        }
        return OrderReportVO.builder()
                .dateList(dateList.stream().map(LocalDate::toString).collect(Collectors.joining(",")))
                .orderCountList(orderCountList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .validOrderCountList(validOrderCountList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate((double) totalValidOrderCount / totalOrderCount)
                .build();
    }

    @Override
    public SalesTop10ReportVO salesTop10Report(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        Map map = new HashMap<>();
        map.put("begin", beginTime);
        map.put("end", endTime);
        map.put("status", Orders.COMPLETED);
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getGoodsSalesDTOList(map);
        if (goodsSalesDTOList == null) {
            goodsSalesDTOList = new ArrayList<>();
        }
        String nameList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.joining(","));
        String numberList = goodsSalesDTOList.stream()
                .map(item -> String.valueOf(item.getNumber()))
                .collect(Collectors.joining(","));

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }


}