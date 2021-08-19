package com.example.catalog.service;

import com.example.catalog.entity.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();

    CatalogEntity getCatalog(String productId);
}
