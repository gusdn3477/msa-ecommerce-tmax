package com.example.cartservice.controller;

import com.example.cartservice.entity.CartEntity;
import com.example.cartservice.service.CartService;
import com.example.cartservice.vo.ResponseCart;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class CartController {
    private final Environment env;
    private final CartService cartService;

    @Autowired
    public CartController(Environment env, CartService cartService){
        this.env = env;
        this.cartService = cartService;
    }

    @GetMapping("/health_check")
    public String status(HttpServletRequest request){
        return String.format("잘 작동됩니다.");
    }

    @GetMapping("/carts")
    public ResponseEntity<List<ResponseCart>> getCarts(){
        Iterable<CartEntity> cartList = cartService.getAllCarts();
        List<ResponseCart> result = new ArrayList<>();
        cartList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseCart.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/carts")
    public ResponseEntity<List<ResponseCart>> addCarts(){
        Iterable<CartEntity> cartList = cartService.getAllCarts();
        List<ResponseCart> result = new ArrayList<>();
        cartList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseCart.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    @GetMapping("/{userId}/carts")
//    public ResponseEntity<List<ResponseCart>> getCart(@PathVariable("userId") String userId){
//        Iterable<CartEntity> cartList = cartService.getCartByUserId(userId);
//        List<ResponseCart> result = new ArrayList<>();
//        cartList.forEach(v -> {
//            result.add(new ModelMapper().map(v, ResponseCart.class));
//        });
//
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }

}
