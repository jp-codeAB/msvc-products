package com.arka_proyect.msvc.products.domain.ports.in;

import com.arka_proyect.msvc.products.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface ICategoryUseCase {
    Optional<Category> getCategory(Long id);
    Category createCategory(Category category);
    Optional<Category> updateCategory(Category category);
    void delete(Long id);
    List<Category> listAllCategory();
}