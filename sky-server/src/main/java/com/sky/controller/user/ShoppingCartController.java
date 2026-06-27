package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppoingCartservice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Api(tags = "c端-购物车管理接口")
@Slf4j
public class ShoppingCartController {


    @Autowired
    private ShoppoingCartservice shoppoingCartservice;


    @PostMapping("/add")
    @ApiOperation(value = "添加购物车")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车,物品的信息为: {}", shoppingCartDTO);
        shoppoingCartservice.add(shoppingCartDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询购物车")
    public Result<List<ShoppingCart>> list() {
        log.info("查询购物车");
        List<ShoppingCart> shoppingCarts = shoppoingCartservice.showCart();

        return Result.success(shoppingCarts);
    }


    @DeleteMapping("/clean")
    @ApiOperation(value = "清空购物车")
    public Result clean() {
        log.info("清空购物车");
        shoppoingCartservice.clean();
        return Result.success();
    }


    @PostMapping("/sub")
    @ApiOperation(value = "减少购物车")
    public Result sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("减少购物车,物品的信息为: {}", shoppingCartDTO);
        shoppoingCartservice.sub(shoppingCartDTO);
        return Result.success();
    }





}
