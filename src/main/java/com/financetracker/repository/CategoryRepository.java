package com.financetracker.repository;

import com.financetracker.model.Category;
import com.financetracker.model.CategoryType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CategoryRepository {
    private final Map<Long, Category> categories = new HashMap<>();
    private Long idSequence = 1L;

    public Category save(Category category) {
        if (category.getId() == null) {
            category.setId(idSequence++);
        }
        categories.put(category.getId(), category);
        return category;
    }

    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(categories.get(id));
    }

    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

    public List<Category> findByType(CategoryType type) {
        return categories.values().stream()
                .filter(category -> category.getType() == type)
                .collect(Collectors.toList());
    }

    public void delete(Category category) {
        categories.remove(category.getId());
    }

    public void deleteById(Long id) {
        categories.remove(id);
    }
}