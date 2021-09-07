package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.OrderEntity;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDetails);
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
