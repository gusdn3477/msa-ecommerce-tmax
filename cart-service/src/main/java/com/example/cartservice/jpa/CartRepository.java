package com.example.cartservice.jpa;

import com.example.cartservice.entity.CartEntity;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<CartEntity, Long>{
    //CartEntity findByuserId(String userId);
    //Iterable<CartEntity> findByUserId(String userId);
}
