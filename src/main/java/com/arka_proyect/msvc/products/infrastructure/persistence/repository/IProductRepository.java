package com.arka_proyect.msvc.products.infrastructure.persistence.repository;

import com.arka_proyect.msvc.products.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByStockLessThan(Integer threshold);
    List<ProductEntity> findByCategoryId(Long categoryId);
}