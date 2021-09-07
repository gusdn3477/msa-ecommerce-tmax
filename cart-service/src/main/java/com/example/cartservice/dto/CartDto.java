package com.example.cartservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CartDto implements Serializable {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private String image;
    private String orderId;
    private String userId;
}
