package com.example.userservice.client;

import com.example.userservice.error.FeignErrorDecoder;
import com.example.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name = "order-service", configuration = FeignErrorDecoder.class)
public interface OrderServiceClient {

    @GetMapping("/{userId}/orders")
    List<ResponseOrder> getOrder(@PathVariable("userId") String userId);

    @GetMapping("/{userId}/orders.wrong")
    List<ResponseOrder> getOrdersWrong(@PathVariable String userId);
}
