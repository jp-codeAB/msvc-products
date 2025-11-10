package com.arka_proyect.msvc.products.infrastructure.web.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String status;
    private String categoryName;
    private Long categoryId;
    private LocalDate createAt;
    private LocalDate updateAt;
}