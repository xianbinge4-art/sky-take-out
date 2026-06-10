package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @ApiOperation("新增菜品")
    @PostMapping
     public Result save( @RequestBody DishDTO dishDTO){
         log.info("新增菜品的信息是:{}", dishDTO);
         dishService.saveWithFlavor(dishDTO);
         return Result.success();
     }
     @GetMapping("/page")
     @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
            log.info("菜品分页查询的参数是:{}", dishPageQueryDTO);
         PageResult pageResult=dishService.page(dishPageQueryDTO);
         return Result.success(pageResult);
     }
     @ApiOperation("菜品删除")
    @DeleteMapping
     public Result delete( @RequestParam("ids") List<Integer> ids){
         log.info("删除菜单的id是:{}", ids);
         dishService.delete(ids);
         return Result.success();
     }





}
