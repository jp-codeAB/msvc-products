package com.arka_proyect.msvc.products.infrastructure.web.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StockLogResponse {
    private Long id;
    private Long productId;
    private Integer quantityChange;
    private Integer finalStock;
    private String reason;
    private Long userId;
    private LocalDateTime createAt;
}