package com.arka_proyect.msvc.products.infrastructure.persistence.repository;

import com.arka_proyect.msvc.products.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
}