package com.arka_proyect.msvc.products.infrastructure.persistence.repository;

import com.arka_proyect.msvc.products.infrastructure.persistence.entity.StockLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IStockLogRepository extends JpaRepository<StockLogEntity, Long> {
    List<StockLogEntity> findByProductId(Long productId);
    @Modifying
    @Query("DELETE FROM StockLogEntity sl WHERE sl.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);
}