package com.arka_proyect.msvc.products.application.service;

import com.arka_proyect.msvc.products.domain.model.Category;
import com.arka_proyect.msvc.products.domain.ports.in.ICategoryUseCase;
import com.arka_proyect.msvc.products.domain.ports.out.ICategoryRepositoryPort;
import com.arka_proyect.msvc.products.shared.exception.ResourceAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryUseCase {

    private final ICategoryRepositoryPort repositoryPort;

    public CategoryService(ICategoryRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Optional<Category> getCategory(Long id) {
        return repositoryPort.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        category.setCreateAt(LocalDate.now());
        try {
            return repositoryPort.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException(
                    "The category name '" + category.getName() + "' already exists. Names must be unique."
            );
        }
    }

    @Override
    public Optional<Category> updateCategory(Category category) {
        return getCategory(category.getId()).map(categoryDB -> {
            categoryDB.setName(category.getName());
            categoryDB.setDescription(category.getDescription());
            return repositoryPort.save(categoryDB);
        });
    }

    @Override
    public void delete(Long id) {
        repositoryPort.deleteById(id);
    }

    @Override
    public List<Category> listAllCategory() {
        return repositoryPort.findAll();
    }

}