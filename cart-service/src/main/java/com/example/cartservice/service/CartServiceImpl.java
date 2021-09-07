package com.example.cartservice.service;

import com.example.cartservice.entity.CartEntity;
import com.example.cartservice.jpa.CartRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
public class CartServiceImpl implements CartService{
    CartRepository repository;
    Environment env;

    @Autowired
    public CartServiceImpl(CartRepository repository, Environment env){
        this.repository = repository;
        this.env = env;
    }

    @Override
    public Iterable<CartEntity> getAllCarts(){
        return repository.findAll();
    }

//    @Override
//    public Iterable<CartEntity> getCartByUserId(String userId){
//        return repository.findByUserId(userId);
//    }
}
