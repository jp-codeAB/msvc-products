package com.arka_proyect.msvc.products.domain.ports.in;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.infrastructure.web.dto.request.StockItemRequest;
import java.util.List;
import java.util.Optional;

public interface IProductStockManagementUseCase {
    Optional<Product> updateStock(Long productId, Integer quantityChange, Long userId);
    boolean reserveMultipleStock(List<StockItemRequest> items);
    void reverseMultipleStock(List<StockItemRequest> items);
    void convertReservationToSale(List<StockItemRequest> items);
}