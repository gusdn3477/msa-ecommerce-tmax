package com.example.orderservice.client;

import com.example.orderservice.vo.ResponseCatalog;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name="catalog-service")
public interface CatalogServiceClient {

    @GetMapping("/catalogs/{productId}")
    ResponseCatalog getCatalog(@PathVariable String productId);
}
