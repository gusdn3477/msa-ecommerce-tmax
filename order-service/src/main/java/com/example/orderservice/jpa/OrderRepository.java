package com.example.orderservice.jpa;

import com.example.orderservice.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    Iterable<OrderEntity> findByUserId(String userId);
}
