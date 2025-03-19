package com.financetracker.facade;

import com.financetracker.factory.FinanceEntityFactory;
import com.financetracker.model.*;
import com.financetracker.repository.interfaces.BankAccountRepositoryInterface;
import com.financetracker.repository.interfaces.CategoryRepositoryInterface;
import com.financetracker.repository.interfaces.OperationRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Фасад для финансового трекера, который упрощает доступ к основной функциональности
 * и скрывает сложность внутренней реализации.
 * Паттерн Фасад используется для предоставления унифицированного интерфейса
 * к набору интерфейсов в системе.
 */
@Service
public class FinanceTrackerFacade implements FinanceTrackerFacadeInterface {

    private final BankAccountRepositoryInterface bankAccountRepository;
    private final CategoryRepositoryInterface categoryRepository;
    private final OperationRepositoryInterface operationRepository;
    private final FinanceEntityFactory entityFactory;

    @Autowired
    public FinanceTrackerFacade(
            @Qualifier("bankAccountRepositoryProxy") BankAccountRepositoryInterface bankAccountRepository,
            @Qualifier("categoryRepositoryProxy") CategoryRepositoryInterface categoryRepository,
            @Qualifier("operationRepositoryProxy") OperationRepositoryInterface operationRepository,
            FinanceEntityFactory entityFactory) {
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
        this.entityFactory = entityFactory;
    }

    // --------------- Банковские счета ---------------

    public BankAccount createBankAccount(String name, double initialBalance) {
        BankAccount account = entityFactory.createBankAccount(name, initialBalance);
        return bankAccountRepository.save(account);
    }

    public BankAccount getBankAccountById(Long id) {
        Optional<BankAccount> accountOpt = bankAccountRepository.findById(id);
        return accountOpt.orElse(null);
    }

    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public void updateBankAccount(BankAccount account) {
        bankAccountRepository.save(account);
    }

    public void deleteBankAccount(Long id) {
        bankAccountRepository.deleteById(id);
    }

    // --------------- Категории ---------------

    public Category createCategory(CategoryType type, String name) {
        Category category = entityFactory.createCategory(type, name);
        return categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        return categoryOpt.orElse(null);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByType(CategoryType type) {
        return categoryRepository.findByType(type);
    }

    public void updateCategory(Category category) {
        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // --------------- Операции ---------------

    public Operation createIncomeOperation(Long bankAccountId, double amount, LocalDate date,
                                           String description, Long categoryId) {
        Operation operation = entityFactory.createIncomeOperation(
                bankAccountId, amount, date, description, categoryId);
        return operationRepository.save(operation);
    }

    public Operation createExpenseOperation(Long bankAccountId, double amount, LocalDate date,
                                            String description, Long categoryId) {
        Operation operation = entityFactory.createExpenseOperation(
                bankAccountId, amount, date, description, categoryId);
        return operationRepository.save(operation);
    }

    public Operation getOperationById(Long id) {
        Optional<Operation> operationOpt = operationRepository.findById(id);
        return operationOpt.orElse(null);
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    public List<Operation> getOperationsByBankAccountId(Long bankAccountId) {
        return operationRepository.findByBankAccountId(bankAccountId);
    }

    public List<Operation> getOperationsByType(OperationType type) {
        return operationRepository.findByType(type);
    }

    public List<Operation> getOperationsByCategoryId(Long categoryId) {
        return operationRepository.findByCategoryId(categoryId);
    }

    public List<Operation> getOperationsByDateRange(LocalDate startDate, LocalDate endDate) {
        return operationRepository.findByDateRange(startDate, endDate);
    }

    public void deleteOperation(Long id) {
        operationRepository.deleteById(id);
    }


    // --------------- Пересчет баланса ---------------

    public void recalculateAllBalances() {
        // TODO реализовать пересчет баланса для всех счетов
    }
}