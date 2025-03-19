package com.financetracker.factory;

import com.financetracker.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FinanceEntityFactoryTest {

    private FinanceEntityFactory factory;
    private final LocalDate testDate = LocalDate.of(2025, 3, 19);

    @BeforeEach
    void setUp() {
        factory = new FinanceEntityFactory();
    }

    @Test
    @DisplayName("Should create a bank account with the given name and initial balance")
    void testCreateBankAccount() {
        // Arrange
        String accountName = "Test Account";
        double initialBalance = 1000.0;

        // Act
        BankAccount account = factory.createBankAccount(accountName, initialBalance);

        // Assert
        assertNotNull(account);
        assertEquals(accountName, account.getName());
        assertEquals(initialBalance, account.getBalance());
    }

    @Test
    @DisplayName("Should create an income category with the given name")
    void testCreateIncomeCategory() {
        // Arrange
        String categoryName = "Salary";

        // Act
        Category category = factory.createCategory(CategoryType.INCOME, categoryName);

        // Assert
        assertNotNull(category);
        assertEquals(categoryName, category.getName());
        assertEquals(CategoryType.INCOME, category.getType());
    }

    @Test
    @DisplayName("Should create an expense category with the given name")
    void testCreateExpenseCategory() {
        // Arrange
        String categoryName = "Groceries";

        // Act
        Category category = factory.createCategory(CategoryType.EXPENSE, categoryName);

        // Assert
        assertNotNull(category);
        assertEquals(categoryName, category.getName());
        assertEquals(CategoryType.EXPENSE, category.getType());
    }

    @Test
    @DisplayName("Should create an income operation with the given parameters")
    void testCreateIncomeOperation() {
        // Arrange
        Long bankAccountId = 1L;
        double amount = 5000.0;
        String description = "Monthly salary";
        Long categoryId = 2L;

        // Act
        Operation operation = factory.createIncomeOperation(
                bankAccountId, amount, testDate, description, categoryId);

        // Assert
        assertNotNull(operation);
        assertEquals(OperationType.INCOME, operation.getType());
        assertEquals(bankAccountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(testDate, operation.getDate());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    @DisplayName("Should create an expense operation with the given parameters")
    void testCreateExpenseOperation() {
        // Arrange
        Long bankAccountId = 1L;
        double amount = 100.0;
        String description = "Grocery shopping";
        Long categoryId = 3L;

        // Act
        Operation operation = factory.createExpenseOperation(
                bankAccountId, amount, testDate, description, categoryId);

        // Assert
        assertNotNull(operation);
        assertEquals(OperationType.EXPENSE, operation.getType());
        assertEquals(bankAccountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(testDate, operation.getDate());
        assertEquals(description, operation.getDescription());
        assertEquals(categoryId, operation.getCategoryId());
    }
}