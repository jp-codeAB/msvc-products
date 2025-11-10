package com.arka_proyect.msvc.products.domain.ports.out;

import com.arka_proyect.msvc.products.domain.model.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepositoryPort {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    void deleteById(Long id);
}