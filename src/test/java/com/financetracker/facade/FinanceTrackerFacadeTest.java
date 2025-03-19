package com.financetracker.facade;

import com.financetracker.factory.FinanceEntityFactory;
import com.financetracker.model.*;
import com.financetracker.repository.interfaces.BankAccountRepositoryInterface;
import com.financetracker.repository.interfaces.CategoryRepositoryInterface;
import com.financetracker.repository.interfaces.OperationRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceTrackerFacadeTest {

    @Mock
    private BankAccountRepositoryInterface bankAccountRepository;

    @Mock
    private CategoryRepositoryInterface categoryRepository;

    @Mock
    private OperationRepositoryInterface operationRepository;

    @Mock
    private FinanceEntityFactory entityFactory;

    @InjectMocks
    private FinanceTrackerFacade facade;

    private BankAccount testAccount;
    private Category testCategory;
    private Operation testOperation;

    @BeforeEach
    void setUp() {
        testAccount = BankAccount.builder()
                .id(1L)
                .name("Тестовый счет")
                .balance(1000.0)
                .build();

        testCategory = Category.builder()
                .id(1L)
                .name("Продукты")
                .type(CategoryType.EXPENSE)
                .build();

        testOperation = Operation.builder()
                .id(1L)
                .bankAccountId(1L)
                .categoryId(1L)
                .amount(100.0)
                .date(LocalDate.now())
                .description("Тестовая операция")
                .type(OperationType.EXPENSE)
                .build();
    }

    @Test
    void testCreateBankAccount() {
        String name = "Новый счет";
        double initialBalance = 5000.0;

        when(entityFactory.createBankAccount(name, initialBalance)).thenReturn(testAccount);
        when(bankAccountRepository.save(testAccount)).thenReturn(testAccount);

        BankAccount result = facade.createBankAccount(name, initialBalance);

        assertNotNull(result);
        assertEquals(testAccount, result);
        verify(entityFactory).createBankAccount(name, initialBalance);
        verify(bankAccountRepository).save(testAccount);
    }

    @Test
    void testGetBankAccountById() {
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(bankAccountRepository.findById(2L)).thenReturn(Optional.empty());

        BankAccount result = facade.getBankAccountById(1L);
        assertNotNull(result);
        assertEquals(testAccount, result);

        BankAccount nonExistingResult = facade.getBankAccountById(2L);
        assertNull(nonExistingResult);
    }

    @Test
    void testGetAllBankAccounts() {
        List<BankAccount> accounts = List.of(testAccount);
        when(bankAccountRepository.findAll()).thenReturn(accounts);

        List<BankAccount> result = facade.getAllBankAccounts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(accounts, result);
        verify(bankAccountRepository).findAll();
    }

    @Test
    void testUpdateBankAccount() {
        BankAccount accountToUpdate = testAccount;
        accountToUpdate.setName("Обновленный счет");

        facade.updateBankAccount(accountToUpdate);

        verify(bankAccountRepository).save(accountToUpdate);
    }

    @Test
    void testDeleteBankAccount() {
        facade.deleteBankAccount(1L);

        verify(bankAccountRepository).deleteById(1L);
    }

    @Test
    void testCreateCategory() {
        CategoryType type = CategoryType.INCOME;
        String name = "Зарплата";

        Category incomeCategory = Category.builder()
                .id(2L)
                .name(name)
                .type(type)
                .build();

        when(entityFactory.createCategory(type, name)).thenReturn(incomeCategory);
        when(categoryRepository.save(incomeCategory)).thenReturn(incomeCategory);

        Category result = facade.createCategory(type, name);

        assertNotNull(result);
        assertEquals(incomeCategory, result);
        verify(entityFactory).createCategory(type, name);
        verify(categoryRepository).save(incomeCategory);
    }

    @Test
    void testGetCategoryById() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(testCategory));
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        Category result = facade.getCategoryById(1L);
        assertNotNull(result);
        assertEquals(testCategory, result);

        Category nonExistingResult = facade.getCategoryById(2L);
        assertNull(nonExistingResult);
    }

    @Test
    void testGetAllCategories() {
        List<Category> categories = List.of(testCategory);
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = facade.getAllCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categories, result);
        verify(categoryRepository).findAll();
    }

    @Test
    void testGetCategoriesByType() {
        List<Category> expenseCategories = List.of(testCategory);
        when(categoryRepository.findByType(CategoryType.EXPENSE)).thenReturn(expenseCategories);
        when(categoryRepository.findByType(CategoryType.INCOME)).thenReturn(new ArrayList<>());

        List<Category> expenseResult = facade.getCategoriesByType(CategoryType.EXPENSE);

        assertNotNull(expenseResult);
        assertEquals(1, expenseResult.size());

        List<Category> incomeResult = facade.getCategoriesByType(CategoryType.INCOME);

        assertNotNull(incomeResult);
        assertTrue(incomeResult.isEmpty());
    }

    @Test
    void testUpdateCategory() {
        Category categoryToUpdate = testCategory;
        categoryToUpdate.setName("Обновленная категория");

        facade.updateCategory(categoryToUpdate);

        verify(categoryRepository).save(categoryToUpdate);
    }

    @Test
    void testDeleteCategory() {
        facade.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void testCreateIncomeOperation() {
        Long bankAccountId = 1L;
        double amount = 500.0;
        LocalDate date = LocalDate.now();
        String description = "Зарплата";
        Long categoryId = 2L;

        Operation incomeOperation = Operation.builder()
                .id(2L)
                .bankAccountId(bankAccountId)
                .amount(amount)
                .date(date)
                .description(description)
                .categoryId(categoryId)
                .type(OperationType.INCOME)
                .build();

        when(entityFactory.createIncomeOperation(bankAccountId, amount, date, description, categoryId))
                .thenReturn(incomeOperation);
        when(operationRepository.save(incomeOperation)).thenReturn(incomeOperation);

        Operation result = facade.createIncomeOperation(bankAccountId, amount, date, description, categoryId);

        assertNotNull(result);
        assertEquals(incomeOperation, result);
        verify(entityFactory).createIncomeOperation(bankAccountId, amount, date, description, categoryId);
        verify(operationRepository).save(incomeOperation);
    }

    @Test
    void testCreateExpenseOperation() {
        Long bankAccountId = 1L;
        double amount = 200.0;
        LocalDate date = LocalDate.now();
        String description = "Продукты";
        Long categoryId = 1L;

        when(entityFactory.createExpenseOperation(bankAccountId, amount, date, description, categoryId))
                .thenReturn(testOperation);
        when(operationRepository.save(testOperation)).thenReturn(testOperation);

        Operation result = facade.createExpenseOperation(bankAccountId, amount, date, description, categoryId);

        assertNotNull(result);
        assertEquals(testOperation, result);
        verify(entityFactory).createExpenseOperation(bankAccountId, amount, date, description, categoryId);
        verify(operationRepository).save(testOperation);
    }

    @Test
    void testGetOperationById() {
        when(operationRepository.findById(1L)).thenReturn(Optional.of(testOperation));
        when(operationRepository.findById(2L)).thenReturn(Optional.empty());

        Operation result = facade.getOperationById(1L);
        assertNotNull(result);
        assertEquals(testOperation, result);

        Operation nonExistingResult = facade.getOperationById(2L);
        assertNull(nonExistingResult);
    }

    @Test
    void testGetAllOperations() {
        List<Operation> operations = List.of(testOperation);
        when(operationRepository.findAll()).thenReturn(operations);

        List<Operation> result = facade.getAllOperations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(operations, result);
        verify(operationRepository).findAll();
    }

    @Test
    void testGetOperationsByBankAccountId() {
        List<Operation> operations = List.of(testOperation);
        when(operationRepository.findByBankAccountId(1L)).thenReturn(operations);
        when(operationRepository.findByBankAccountId(2L)).thenReturn(new ArrayList<>());

        List<Operation> result = facade.getOperationsByBankAccountId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        List<Operation> emptyResult = facade.getOperationsByBankAccountId(2L);

        assertNotNull(emptyResult);
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    void testGetOperationsByType() {
        List<Operation> expenseOperations = List.of(testOperation);
        when(operationRepository.findByType(OperationType.EXPENSE)).thenReturn(expenseOperations);
        when(operationRepository.findByType(OperationType.INCOME)).thenReturn(new ArrayList<>());

        List<Operation> expenseResult = facade.getOperationsByType(OperationType.EXPENSE);

        assertNotNull(expenseResult);
        assertEquals(1, expenseResult.size());

        List<Operation> incomeResult = facade.getOperationsByType(OperationType.INCOME);

        assertNotNull(incomeResult);
        assertTrue(incomeResult.isEmpty());
    }
}