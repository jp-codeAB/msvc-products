package com.arka_proyect.msvc.products.domain.ports.in;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.domain.model.StockChangeReason;

public interface IStockAuditUseCase {
    void logStockChange(Product updatedProduct, Integer quantityChange, Long userId, StockChangeReason reason);
}