package com.arka_proyect.msvc.products.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    private Long id;
    private String name;
    private String description;
    private LocalDate createAt;
}