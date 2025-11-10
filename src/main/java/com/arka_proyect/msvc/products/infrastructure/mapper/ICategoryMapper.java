package com.arka_proyect.msvc.products.infrastructure.mapper;

import com.arka_proyect.msvc.products.domain.model.Category;
import com.arka_proyect.msvc.products.infrastructure.persistence.entity.CategoryEntity;
import com.arka_proyect.msvc.products.infrastructure.web.dto.request.CategoryRequest;
import com.arka_proyect.msvc.products.infrastructure.web.dto.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICategoryMapper {

    // Request/Response
    @Mapping(target = "createAt", ignore = true)
    Category toDomain(CategoryRequest request);
    CategoryResponse toResponse(Category category);
    List<CategoryResponse> toResponseList(List<Category> categories);

    // Entity
    @Named("categoryToEntity")
    CategoryEntity toEntity(Category category);

    // Domain
    @Named("categoryToDomain")
    Category toDomain(CategoryEntity entity);
}