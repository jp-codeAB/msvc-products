package com.arka_proyect.msvc.products.infrastructure.web.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate createAt;
}