package com.arka_proyect.msvc.products.infrastructure.persistence.adapter;

import com.arka_proyect.msvc.products.domain.model.StockLog;
import com.arka_proyect.msvc.products.domain.ports.out.IStockLogRepositoryPort;
import com.arka_proyect.msvc.products.infrastructure.mapper.IStockLogMapper;
import com.arka_proyect.msvc.products.infrastructure.persistence.entity.StockLogEntity;
import com.arka_proyect.msvc.products.infrastructure.persistence.repository.IStockLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class StockLogRepositoryAdapter implements IStockLogRepositoryPort {

    private final IStockLogRepository repository;
    private final IStockLogMapper mapper;

    public StockLogRepositoryAdapter(IStockLogRepository repository, IStockLogMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public StockLog save(StockLog stockLog) {
        StockLogEntity entity = mapper.toEntity(stockLog);
        StockLogEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<StockLog> findByProductId(Long productId) {
        List<StockLogEntity> entities = repository.findByProductId(productId);
        return mapper.toDomainList(entities);
    }

    @Override
    public Page<StockLog> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByProductId(Long productId) {
        repository.deleteByProductId(productId);
    }
}