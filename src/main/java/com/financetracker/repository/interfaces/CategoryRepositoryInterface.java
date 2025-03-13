package com.financetracker.repository.interfaces;

import com.financetracker.model.Category;
import com.financetracker.model.CategoryType;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryInterface {
    Category save(Category category);
    Optional<Category> findById(Long id);
    List<Category> findAll();
    List<Category> findByType(CategoryType type);
    void delete(Category category);
    void deleteById(Long id);
}