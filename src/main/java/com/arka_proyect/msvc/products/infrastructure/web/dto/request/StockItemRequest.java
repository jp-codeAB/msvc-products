package com.arka_proyect.msvc.products.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class StockItemRequest {

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}