package com.arka_proyect.msvc.products.infrastructure.web.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {

    private Long id;
    @NotBlank(message = "The name can't be empty")
    private String name;
    private String description;
}