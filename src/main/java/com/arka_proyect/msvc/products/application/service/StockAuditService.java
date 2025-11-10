package com.arka_proyect.msvc.products.application.service;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.domain.model.StockChangeReason;
import com.arka_proyect.msvc.products.domain.model.StockLog;
import com.arka_proyect.msvc.products.domain.ports.in.IStockAuditUseCase;
import com.arka_proyect.msvc.products.domain.ports.out.IStockLogRepositoryPort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class StockAuditService implements IStockAuditUseCase {
    private final IStockLogRepositoryPort stockLogRepositoryPort;
    public StockAuditService(IStockLogRepositoryPort stockLogRepositoryPort) {
        this.stockLogRepositoryPort = stockLogRepositoryPort;
    }

    @Override
    public void logStockChange(Product updatedProduct, Integer quantityChange, Long userId, StockChangeReason reason) {
        StockLog log = StockLog.builder()
                .product(updatedProduct)
                .quantityChange(quantityChange)
                .newStock(updatedProduct.getStock())
                .userId(userId)
                .changeAt(LocalDateTime.now())
                .operation(reason)
                .build();
        stockLogRepositoryPort.save(log);
    }
}