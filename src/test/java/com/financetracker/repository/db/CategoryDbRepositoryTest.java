package com.financetracker.repository.db;

import com.financetracker.model.Category;
import com.financetracker.model.CategoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDbRepositoryTest {

    private CategoryDbRepository repository;

    @BeforeEach
    void setUp() {
        repository = new CategoryDbRepository();
    }

    @Test
    void save_shouldPersistCategory() {
        Category category = Category.builder()
                .name("Food")
                .type(CategoryType.EXPENSE)
                .build();

        Category savedCategory = repository.save(category);

        assertNotNull(savedCategory.getId());
        assertEquals("Food", savedCategory.getName());
        assertEquals(CategoryType.EXPENSE, savedCategory.getType());

        Optional<Category> retrieved = repository.findById(savedCategory.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(savedCategory.getId(), retrieved.get().getId());
    }

    @Test
    void findById_shouldReturnCategory_whenPresent() {
        Category category = Category.builder()
                .name("Travel")
                .type(CategoryType.EXPENSE)
                .build();
        Category savedCategory = repository.save(category);

        Optional<Category> retrievedCategory = repository.findById(savedCategory.getId());

        assertTrue(retrievedCategory.isPresent());
        assertEquals(category.getName(), retrievedCategory.get().getName());
        assertEquals(category.getType(), retrievedCategory.get().getType());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotPresent() {
        Optional<Category> retrievedCategory = repository.findById(999L);

        assertTrue(retrievedCategory.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllCategories() {
        repository.save(Category.builder().name("Groceries").type(CategoryType.EXPENSE).build());
        repository.save(Category.builder().name("Salary").type(CategoryType.INCOME).build());

        List<Category> categories = repository.findAll();

        assertEquals(2, categories.size());
    }

    @Test
    void findByType_shouldReturnCategoriesWithGivenType() {
        repository.save(Category.builder().name("Groceries").type(CategoryType.EXPENSE).build());
        repository.save(Category.builder().name("Salary").type(CategoryType.INCOME).build());
        repository.save(Category.builder().name("Travel").type(CategoryType.EXPENSE).build());

        List<Category> expenseCategories = repository.findByType(CategoryType.EXPENSE);

        assertEquals(2, expenseCategories.size());
        assertTrue(expenseCategories.stream().allMatch(c -> c.getType() == CategoryType.EXPENSE));
    }

    @Test
    void delete_shouldRemoveCategory() {
        Category category = repository.save(Category.builder()
                .name("Entertainment")
                .type(CategoryType.EXPENSE)
                .build());

        repository.delete(category);

        assertTrue(repository.findById(category.getId()).isEmpty());
    }

    @Test
    void deleteById_shouldRemoveCategoryById() {
        Category category = repository.save(Category.builder()
                .name("Shopping")
                .type(CategoryType.EXPENSE)
                .build());

        repository.deleteById(category.getId());

        assertTrue(repository.findById(category.getId()).isEmpty());
    }
}