package com.arka_proyect.msvc.products.infrastructure.persistence.adapter;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.domain.ports.out.IProductRepositoryPort;
import com.arka_proyect.msvc.products.infrastructure.mapper.IProductMapper;
import com.arka_proyect.msvc.products.infrastructure.persistence.entity.ProductEntity;
import com.arka_proyect.msvc.products.infrastructure.persistence.repository.IProductRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryAdapter implements IProductRepositoryPort {

    private final IProductRepository productRepository;
    private final IProductMapper mapper;

    public ProductRepositoryAdapter(IProductRepository productRepository, IProductMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = productRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id).map(mapper::toDomain);
    }
    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
    @Override
    public List<Product> findByStockLessThan(Integer threshold) {
        return productRepository.findByStockLessThan(threshold).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<Product> findAllById(List<Long> ids) {
        return productRepository.findAllById(ids).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

}