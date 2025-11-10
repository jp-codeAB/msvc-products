package com.arka_proyect.msvc.products.application.service;

import com.arka_proyect.msvc.products.domain.model.StockLog;
import com.arka_proyect.msvc.products.domain.ports.in.IStockLogUseCase;
import com.arka_proyect.msvc.products.domain.ports.out.IStockLogRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StockLogService implements IStockLogUseCase {

    private final IStockLogRepositoryPort stockLogRepositoryPort;

    public StockLogService(IStockLogRepositoryPort stockLogRepositoryPort) {
        this.stockLogRepositoryPort = stockLogRepositoryPort;
    }

    @Override
    public List<StockLog> getHistoryByProductId(Long productId) {
        return stockLogRepositoryPort.findByProductId(productId);
    }

    @Override
    public Page<StockLog> listAllStockHistory(Pageable pageable) {
        return stockLogRepositoryPort.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteLogsByProductId(Long productId) {
        stockLogRepositoryPort.deleteByProductId(productId);
    }
}