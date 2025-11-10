package com.arka_proyect.msvc.products.domain.ports.in;

import com.arka_proyect.msvc.products.domain.model.StockLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStockLogUseCase {
    Page<StockLog> listAllStockHistory(Pageable pageable);
    List<StockLog> getHistoryByProductId(Long productId);
    void deleteLogsByProductId(Long productId);
}