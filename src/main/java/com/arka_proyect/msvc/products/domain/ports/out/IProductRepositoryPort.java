package com.arka_proyect.msvc.products.domain.ports.out;

import com.arka_proyect.msvc.products.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface IProductRepositoryPort {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
    List<Product> findByStockLessThan(Integer threshold);
    List<Product> findAllById(List<Long> ids);
    List<Product> findByCategoryId(Long categoryId);

}