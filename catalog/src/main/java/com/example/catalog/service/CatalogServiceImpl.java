package com.example.catalog.service;

import com.example.catalog.entity.CatalogEntity;
import com.example.catalog.jpa.CatalogRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Data
@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService{
    CatalogRepository repository;

    Environment env;

    @Autowired
    public CatalogServiceImpl(CatalogRepository repository, Environment env){
        this.repository = repository;
        this.env = env;
    }

    @Override
    public Iterable<CatalogEntity> getAllCatalogs(){
        return repository.findAll();
    }

    @Override
    public CatalogEntity getCatalog(String productId){
        return repository.findByproductId(productId);
    }

}
