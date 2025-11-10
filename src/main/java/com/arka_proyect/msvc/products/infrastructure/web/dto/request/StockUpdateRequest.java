package com.arka_proyect.msvc.products.infrastructure.web.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class StockUpdateRequest {

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be a positive number for stock replenishment .")
    private Integer quantity;
}