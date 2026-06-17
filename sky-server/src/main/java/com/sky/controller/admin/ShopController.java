package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;


    @PutMapping("/{status}")
    @ApiOperation(value = "设置店铺营业状态接口")
    public Result setStatus(@PathVariable("status") Integer status) {
        log.info("修改店铺营业状态:{}", status==1?"营业中":"打烊中");
        redisTemplate.opsForValue().set("SHOP_STATUS", status);
        return Result.success();
    }

    @ApiOperation(value = "查询店铺营业状态接口")
    @GetMapping("/status")
    public Result<Integer> getShop() {
        Integer status = (Integer) redisTemplate.opsForValue().get("SHOP_STATUS");
        log.info("查询店铺营业状态:{}", status==1?"营业中":"打烊中");
        return Result.success(status);
    }
}




