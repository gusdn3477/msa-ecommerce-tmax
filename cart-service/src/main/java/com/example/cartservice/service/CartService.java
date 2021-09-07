package com.example.cartservice.service;

import com.example.cartservice.entity.CartEntity;

public interface CartService {
    Iterable<CartEntity> getAllCarts();
    //Iterable<CartEntity> getCartByUserId(String userId);
}
