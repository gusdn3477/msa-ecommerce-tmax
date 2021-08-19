package com.example.orderservice.dto;

import com.example.orderservice.vo.ResponseCatalog;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDto implements Serializable {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;

    private List<ResponseCatalog> catalogs;
}
