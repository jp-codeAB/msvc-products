package com.arka_proyect.msvc.products.infrastructure.web.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "The name can't be empty")
    private String name;
    private String description;

    @NotNull(message = "The price can't be null")
    @Min(value = 0, message = "The price can't be negative")
    private Double price;

    @NotNull(message = "The stock can't be null")
    @Min(value = 0, message = "The stock can't be negative")
    private Integer stock;

    private String status;

    @NotNull(message = "The Category ID can't be null")
    private Long categoryId;
}