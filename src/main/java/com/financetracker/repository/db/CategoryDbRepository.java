package com.financetracker.repository.db;

import com.financetracker.model.Category;
import com.financetracker.model.CategoryType;
import com.financetracker.repository.interfaces.CategoryRepositoryInterface;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository("categoryDbRepository")
public class CategoryDbRepository implements CategoryRepositoryInterface {

    private final Map<Long, Category> dbStorage = new HashMap<>();
    private Long idSequence = 1L;

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(idSequence++);
        }
        Category savedCategory = Category.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .build();

        dbStorage.put(savedCategory.getId(), savedCategory);
        return savedCategory;
    }

    @Override
    public Optional<Category> findById(Long id) {
        Category category = dbStorage.get(id);
        if (category == null) {
            return Optional.empty();
        }

        return Optional.of(Category.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .build());
    }

    @Override
    public List<Category> findAll() {
        List<Category> result = new ArrayList<>();
        for (Category category : dbStorage.values()) {
            result.add(Category.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .type(category.getType())
                    .build());
        }
        return result;
    }

    @Override
    public List<Category> findByType(CategoryType type) {
        return dbStorage.values().stream()
                .filter(category -> category.getType() == type)
                .map(category -> Category.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .type(category.getType())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Category category) {
        dbStorage.remove(category.getId());
    }

    @Override
    public void deleteById(Long id) {
        dbStorage.remove(id);
    }
}