package com.arka_proyect.msvc.products.domain.ports.out;

import com.arka_proyect.msvc.products.domain.model.StockLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IStockLogRepositoryPort {
    StockLog save(StockLog stockLog);
    List<StockLog> findByProductId(Long productId);
    Page<StockLog> findAll(Pageable pageable);
    void deleteByProductId(Long productId);
}