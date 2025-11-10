package com.arka_proyect.msvc.products.application.service;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.domain.model.StockChangeReason;
import com.arka_proyect.msvc.products.domain.model.StockLog;
import com.arka_proyect.msvc.products.domain.ports.in.*;
import com.arka_proyect.msvc.products.domain.ports.out.IProductRepositoryPort;
import com.arka_proyect.msvc.products.domain.ports.out.IReportPort;
import com.arka_proyect.msvc.products.infrastructure.web.dto.request.StockItemRequest;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ProductService implements IProductUseCase, IProductStockManagementUseCase {

    private final IProductRepositoryPort productRepositoryPort;
    private final IStockAuditUseCase stockAuditUseCase;
    private final IStockLogUseCase stockLogUseCase;
    private final Integer currentPort;

    public ProductService(IProductRepositoryPort productRepositoryPort,
                          IStockAuditUseCase stockAuditUseCase,
                          Environment environment, IStockLogUseCase stockLogUseCase) {
        this.productRepositoryPort = productRepositoryPort;
        this.stockAuditUseCase = stockAuditUseCase;
        this.stockLogUseCase = stockLogUseCase;
        String port = environment.getProperty("local.server.port");
        this.currentPort = (port != null) ? Integer.parseInt(port) : 8084;
    }

    @Override
    public Product createProduct(Product product) {
        product.setStatus("CREATED");
        product.setCreateAt(LocalDate.now());
        return productRepositoryPort.save(product);
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        return productRepositoryPort.findById(id)
                .map(product -> {
                    product.setCurrentPort(this.currentPort);
                    return product;
                });
    }

    @Override
    public List<Product> listAllProduct() {
        return productRepositoryPort.findAll().stream()
                .peek(product -> product.setCurrentPort(this.currentPort))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> updateProduct (Product product) {
        return getProduct(product.getId()).map(productDB -> {
            Integer oldStock = productDB.getStock();
            Integer newStock = product.getStock();
            productDB.setName(product.getName());
            productDB.setDescription(product.getDescription());
            productDB.setCategory(product.getCategory());
            productDB.setPrice(product.getPrice());
            productDB.setStock(newStock);
            productDB.setUpdateAt(LocalDate.now());
            Product updatedProduct = productRepositoryPort.save(productDB);
            if (!oldStock.equals(newStock)) {
                Integer quantityChange = newStock - oldStock;
                StockChangeReason reason = (quantityChange > 0) ? StockChangeReason.ENTRY : StockChangeReason.OUT;
                stockAuditUseCase.logStockChange(updatedProduct, quantityChange, 0L, reason);
            }
            return updatedProduct;
        });
    }

    @Override
    public void delete(Long id) {
        stockLogUseCase.deleteLogsByProductId(id);
        productRepositoryPort.deleteById(id);
    }


    @Override
    public Optional<Product> updateStock(Long id, Integer quantity, Long userId) {
        return getProduct(id).map(productDB -> {
            Integer newStock = productDB.getStock() + quantity;
            if (newStock < 0) {
                throw new IllegalArgumentException("Cannot update stock to a negative value: "
                        + newStock);
            }
            productDB.setStock(newStock);
            productDB.setUpdateAt(LocalDate.now());
            Product updatedProduct = productRepositoryPort.save(productDB);
            StockChangeReason reason = quantity > 0 ? StockChangeReason.ENTRY : StockChangeReason.OUT;
            stockAuditUseCase.logStockChange(updatedProduct, quantity, userId, reason);
            return updatedProduct;
        });
    }

    @Override
    public boolean reserveMultipleStock(List<StockItemRequest> items) {
        List<Product> validatedProducts = new ArrayList<>();
        for (StockItemRequest item : items) {
            Optional<Product> productOpt = productRepositoryPort.findById(item.getProductId());
            if (productOpt.isEmpty() || productOpt.get().getStock() < item.getQuantity()) {
                return false;
            }
            Product product = productOpt.get();
            product.setStock(product.getStock() - item.getQuantity());
            validatedProducts.add(product);
        }
        for (int i = 0; i < validatedProducts.size(); i++) {
            Product product = validatedProducts.get(i);
            StockItemRequest item = items.get(i);
            product.setUpdateAt(LocalDate.now());
            Product updatedProduct = productRepositoryPort.save(product);
            stockAuditUseCase.logStockChange(updatedProduct, -item.getQuantity(), 0L, StockChangeReason.RESERVATION);
        }
        return true;
    }

    @Override
    public void reverseMultipleStock(List<StockItemRequest> items) {
        for (StockItemRequest item : items) {
            Optional<Product> productOpt = productRepositoryPort.findById(item.getProductId());
            if (productOpt.isEmpty()) continue;
            Product product = productOpt.get();
            Integer newStock = product.getStock() + item.getQuantity();
            product.setStock(newStock);
            product.setUpdateAt(LocalDate.now());
            Product updatedProduct = productRepositoryPort.save(product);
            stockAuditUseCase.logStockChange(updatedProduct, item.getQuantity(), 0L, StockChangeReason.ENTRY);
        }
    }


    @Override
    public Map<Long, Double> getProductPrices(List<Long> productIds) {
        return productRepositoryPort.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Product::getPrice));
    }

    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepositoryPort.findByCategoryId(categoryId).stream()
                .peek(product -> product.setCurrentPort(this.currentPort))
                .collect(Collectors.toList());
    }

    @Override
    public void convertReservationToSale(List<StockItemRequest> items) {
        for (StockItemRequest item : items) {
            Optional<Product> productOpt = productRepositoryPort.findById(item.getProductId());
            if (productOpt.isEmpty()) continue;
            Product product = productOpt.get();
            stockAuditUseCase.logStockChange(
                    product,
                    -item.getQuantity(),
                    0L,
                    StockChangeReason.SALE
            );
        }
    }
}