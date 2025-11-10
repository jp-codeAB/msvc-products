package com.arka_proyect.msvc.products.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockLog {
    private Long id;
    private Product product;
    private Integer quantityChange;
    private Integer newStock;
    private Long userId;
    private LocalDateTime changeAt;
    private StockChangeReason operation;
}