package com.financetracker.repository.proxy;

import com.financetracker.model.Category;
import com.financetracker.model.CategoryType;
import com.financetracker.repository.interfaces.CategoryRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class CategoryRepositoryProxy implements CategoryRepositoryInterface {
    private final Map<Long, Category> cache = new ConcurrentHashMap<>();

    private final CategoryRepositoryInterface dbRepository;

    @Autowired
    public CategoryRepositoryProxy(@Qualifier("categoryDbRepository")
                                   CategoryRepositoryInterface dbRepository) {
        this.dbRepository = dbRepository;
        initializeCache();
    }

    private void initializeCache() {
        // все данные в кеш при инициализации БД
        List<Category> allCategories = dbRepository.findAll();
        for (Category category : allCategories) {
            cache.put(category.getId(), category);
        }
    }

    @Override
    public Category save(Category category) {
        // сохраняю в БД
        Category savedCategory = dbRepository.save(category);
        // обновляю кеш
        cache.put(savedCategory.getId(), savedCategory);
        return savedCategory;
    }

    @Override
    public Optional<Category> findById(Long id) {
        // проверяю кеш
        Category cachedCategory = cache.get(id);
        if (cachedCategory != null) {
            return Optional.of(cachedCategory);
        }

        // если нет, то обращаюсь к БД
        Optional<Category> categoryFromDb = dbRepository.findById(id);

        // сохраняю в кеш
        categoryFromDb.ifPresent(category -> cache.put(id, category));

        return categoryFromDb;
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public List<Category> findByType(CategoryType type) {
        return cache.values().stream()
                .filter(category -> category.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Category category) {
        dbRepository.delete(category);
        cache.remove(category.getId());
    }

    @Override
    public void deleteById(Long id) {
        dbRepository.deleteById(id);
        cache.remove(id);
    }
}