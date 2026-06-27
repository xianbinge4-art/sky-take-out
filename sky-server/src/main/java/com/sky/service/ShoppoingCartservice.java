package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppoingCartservice {
    void add( ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> showCart();;

    void clean();

    void sub(ShoppingCartDTO shoppingCartDTO);
}
