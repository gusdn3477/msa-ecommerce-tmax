package com.example.catalog.jpa;

import com.example.catalog.entity.CatalogEntity;
import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends CrudRepository<CatalogEntity, Long>{
    CatalogEntity findByproductId(String productId);
}
