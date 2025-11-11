package com.arka_proyect.msvc.products.infrastructure.mapper;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.infrastructure.persistence.entity.ProductEntity;
import com.arka_proyect.msvc.products.infrastructure.web.dto.request.ProductRequest;
import com.arka_proyect.msvc.products.infrastructure.web.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ICategoryMapper.class})
public interface IProductMapper {

    // Mapeo de Request a Domain
    @Mappings({
            @Mapping(target = "category.id", source = "categoryId"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "updateAt", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "currentPort", ignore = true)
    })
    Product toDomain(ProductRequest productRequest);

    // Mapeo de Domain a Response
    @Mappings({
            @Mapping(target = "categoryName", source = "category.name"),
            @Mapping(target = "categoryId", source = "category.id")
    })
    ProductResponse toResponse(Product product);

    // Mapeo de Domain → Entity
    @Named("productToEntity")
    @Mappings({
            @Mapping(target = "stockLogs", ignore = true),
            @Mapping(target = "category", source = "category", qualifiedByName = "categoryToEntity")
    })
    ProductEntity toEntity(Product product);

    // Mapeo de Entity → Domain
    @Named("productToDomain")
    @Mappings({
            @Mapping(target = "category", source = "category", qualifiedByName = "categoryToDomain"),
            @Mapping(target = "status", expression = "java(productEntity.getStatus() != null ? productEntity.getStatus() : \"AVAILABLE\")")
    })
    Product toDomain(ProductEntity productEntity);
}