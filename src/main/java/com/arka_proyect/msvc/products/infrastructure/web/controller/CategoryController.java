package com.arka_proyect.msvc.products.infrastructure.web.controller;

import com.arka_proyect.msvc.products.domain.model.Category;
import com.arka_proyect.msvc.products.domain.ports.in.ICategoryUseCase;
import com.arka_proyect.msvc.products.infrastructure.mapper.ICategoryMapper;
import com.arka_proyect.msvc.products.infrastructure.web.dto.request.CategoryRequest;
import com.arka_proyect.msvc.products.infrastructure.web.dto.response.CategoryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final ICategoryUseCase categoryUseCase;
    private final ICategoryMapper categoryMapper;

    public CategoryController(ICategoryUseCase categoryUseCase, ICategoryMapper categoryMapper) {
        this.categoryUseCase = categoryUseCase;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> listAll() {
        List<CategoryResponse> response = categoryUseCase.listAllCategory().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        return categoryUseCase.getCategory(id)
                .map(categoryMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> create(
            @Valid @RequestBody CategoryRequest request){

        Category category = categoryMapper.toDomain(request);
        CategoryResponse response = categoryMapper.toResponse(categoryUseCase.createCategory(category));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Categoría '" + response.getName() + "' registrada exitosamente.",
                        "category", response
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        Category category = categoryMapper.toDomain(request);
        category.setId(id);
        return categoryUseCase.updateCategory(category)
                .map(categoryMapper::toResponse)
                .map(response -> ResponseEntity.ok(
                        Map.of(
                                "message", "Categoría '" + response.getName() + "' actualizada exitosamente.",
                                "category", response
                        )
                ))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        categoryUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

}