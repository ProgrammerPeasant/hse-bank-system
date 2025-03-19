package com.financetracker.facade;

import com.financetracker.model.*;
import java.time.LocalDate;
import java.util.List;

public interface FinanceTrackerFacadeInterface {

    BankAccount createBankAccount(String name, double initialBalance);
    BankAccount getBankAccountById(Long id);
    List<BankAccount> getAllBankAccounts();
    void updateBankAccount(BankAccount bankAccount);
    void deleteBankAccount(Long id);

    Category createCategory(CategoryType type, String name);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    List<Category> getCategoriesByType(CategoryType type);
    void updateCategory(Category category);
    void deleteCategory(Long id);

    Operation createIncomeOperation(Long bankAccountId, double amount, LocalDate date, String description, Long categoryId);
    Operation createExpenseOperation(Long bankAccountId, double amount, LocalDate date, String description, Long categoryId);
    Operation getOperationById(Long id);
    List<Operation> getAllOperations();
    List<Operation> getOperationsByBankAccountId(Long bankAccountId);
    void deleteOperation(Long id);
}