package com.arka_proyect.msvc.products.infrastructure.persistence.adapter;

import com.arka_proyect.msvc.products.domain.model.Category;
import com.arka_proyect.msvc.products.domain.ports.out.ICategoryRepositoryPort;
import com.arka_proyect.msvc.products.infrastructure.mapper.ICategoryMapper;
import com.arka_proyect.msvc.products.infrastructure.persistence.entity.CategoryEntity;
import com.arka_proyect.msvc.products.infrastructure.persistence.repository.ICategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CategoryRepositoryAdapter implements ICategoryRepositoryPort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryMapper categoryMapper;

    public CategoryRepositoryAdapter(ICategoryRepository categoryRepository, ICategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = categoryMapper.toEntity(category);
        return categoryMapper.toDomain(categoryRepository.save(entity));
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

}