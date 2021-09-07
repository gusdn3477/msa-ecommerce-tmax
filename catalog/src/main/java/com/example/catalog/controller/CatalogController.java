package com.example.catalog.controller;

import com.example.catalog.entity.CatalogEntity;
import com.example.catalog.service.CatalogService;
import com.example.catalog.vo.ResponseCatalog;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class CatalogController {
    private Environment env;
    CatalogService catalogService;

    @Autowired
    public CatalogController(Environment env, CatalogService catalogService){
        this.env = env;
        this.catalogService = catalogService;
    }

    @GetMapping("/health_check")
    public String status(HttpServletRequest request){
        return String.format("It's Working in Catalog Service on Port %s", request.getServerPort());
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();
        catalogList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseCatalog.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/catalogs/{productId}")
    public ResponseEntity<ResponseCatalog> getCatalog(@PathVariable String productId) {
        log.info("Before retrieve catalog data");
        CatalogEntity catalogEntity = catalogService.getCatalog(productId);

        log.info("After retrieve catalogs data");
        if (catalogEntity != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ModelMapper().map(catalogEntity, ResponseCatalog.class));
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
