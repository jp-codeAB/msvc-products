package com.arka_proyect.msvc.products.domain.ports.in;

import com.arka_proyect.msvc.products.domain.model.Product;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProductUseCase {
    Product createProduct(Product product);
    Optional<Product> getProduct(Long id);
    List<Product> listAllProduct();
    Optional<Product> updateProduct(Product product);
    void delete(Long id);
    List<Product> getProductsByCategory(Long categoryId);
    Map<Long, Double> getProductPrices(List<Long> productIds);
}