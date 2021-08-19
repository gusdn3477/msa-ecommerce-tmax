package com.example.orderservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseCatalog {

    private String productId;
    private String productName;
    private Integer stock;
    private Integer unitPrice;
    private Date createdAt;
}
