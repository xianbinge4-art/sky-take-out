package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppoingCartservice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ShoppoingCartserviceImpl implements ShoppoingCartservice {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper  dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入到购物车的商品是否已经存在
        ShoppingCart  shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);

        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        //如果已经存在了
        if(list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateById(cart);
    }
        else {
            //如果不存在,就加入购物车
            Long dishId=shoppingCartDTO.getDishId();
            if(dishId!=null){
                //说明是菜品,先查询得到信息,再插入
              Dish dish=dishMapper.getById(dishId.intValue());
                shoppingCart.setName(dish.getName());
                    shoppingCart.setImage(dish.getImage());
                    shoppingCart.setAmount(dish.getPrice());
            }else {
                Long setmealId=shoppingCartDTO.getSetmealId();
             Setmeal setmeal=setmealMapper.findById(setmealId.intValue());
                shoppingCart.setName(setmeal.getName());
                    shoppingCart.setImage(setmeal.getImage());
                    shoppingCart.setAmount(setmeal.getPrice());

            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> showCart() {
       Long shoppingCartId=BaseContext.getCurrentId();
          ShoppingCart shoppingCart=ShoppingCart.builder()
                  .userId(shoppingCartId)
                  .build();
        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void clean() {
        Long userId=BaseContext.getCurrentId();

        shoppingCartMapper.deleteByUserId(userId);
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if(list != null && list.size() > 0){
            ShoppingCart cart = list.get(0);
            if(cart.getNumber() > 1){
                cart.setNumber(cart.getNumber() - 1);
                shoppingCartMapper.updateById(cart);
            }else {
                //如果数量为1,就删除
                shoppingCartMapper.deleteByUserId(cart.getUserId());
            }
        }
    }


}
