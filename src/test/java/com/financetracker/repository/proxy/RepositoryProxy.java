package com.financetracker.repository.proxy;

import com.financetracker.model.BankAccount;
import com.financetracker.model.Category;
import com.financetracker.model.Operation;
import com.financetracker.repository.interfaces.BankAccountRepositoryInterface;
import com.financetracker.repository.interfaces.CategoryRepositoryInterface;
import com.financetracker.repository.interfaces.OperationRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankAccountRepositoryProxyTest {

    @InjectMocks
    private BankAccountRepositoryProxy proxy;

    @Mock
    private BankAccountRepositoryInterface dbRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldSaveInDbAndCache() {
        BankAccount account = BankAccount.builder().id(1L).name("TestAccount").build();
        when(dbRepository.save(account)).thenReturn(account);

        BankAccount savedAccount = proxy.save(account);

        verify(dbRepository, times(1)).save(account);
        assertEquals(account, savedAccount);
        assertTrue(proxy.findAll().contains(savedAccount));
    }

    @Test
    void findById_shouldReturnFromCacheIfPresent() {
        BankAccount account = BankAccount.builder().id(2L).name("CachedAccount").build();
        when(dbRepository.save(account)).thenReturn(account);
        proxy.save(account);

        Optional<BankAccount> retrievedAccount = proxy.findById(2L);

        verify(dbRepository, never()).findById(anyLong());
        assertTrue(retrievedAccount.isPresent());
        assertEquals(account, retrievedAccount.get());
    }

    @Test
    void findById_shouldGetFromDbIfNotCached() {
        BankAccount account = BankAccount.builder().id(3L).name("DbAccount").build();
        when(dbRepository.findById(3L)).thenReturn(Optional.of(account));

        Optional<BankAccount> retrievedAccount = proxy.findById(3L);

        verify(dbRepository, times(1)).findById(3L);
        assertTrue(retrievedAccount.isPresent());
        assertEquals(account, retrievedAccount.get());
        assertTrue(proxy.findAll().contains(account));
    }

    @Test
    void findById_shouldReturnEmptyIfNotExists() {
        when(dbRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<BankAccount> retrievedAccount = proxy.findById(999L);

        verify(dbRepository, times(1)).findById(999L);
        assertTrue(retrievedAccount.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllFromCache() {
        BankAccount account1 = BankAccount.builder().id(1L).name("Account1").build();
        BankAccount account2 = BankAccount.builder().id(2L).name("Account2").build();
        when(dbRepository.save(account1)).thenReturn(account1);
        when(dbRepository.save(account2)).thenReturn(account2);
        proxy.save(account1);
        proxy.save(account2);

        List<BankAccount> accounts = proxy.findAll();

        assertEquals(2, accounts.size());
        assertTrue(accounts.contains(account1));
        assertTrue(accounts.contains(account2));
    }

    @Test
    void delete_shouldRemoveFromCacheAndDb() {
        BankAccount account = BankAccount.builder().id(1L).build();
        when(dbRepository.save(account)).thenReturn(account);
        proxy.save(account);

        proxy.delete(account);

        verify(dbRepository, times(1)).delete(account);
        assertFalse(proxy.findById(1L).isPresent());
    }

    @Test
    void deleteById_shouldRemoveFromCacheAndDb() {
        BankAccount account = BankAccount.builder().id(2L).build();
        when(dbRepository.save(account)).thenReturn(account);
        proxy.save(account);

        proxy.deleteById(2L);

        verify(dbRepository, times(1)).deleteById(2L);
        assertFalse(proxy.findById(2L).isPresent());
    }
}

class CategoryRepositoryProxyTest {

    @InjectMocks
    private CategoryRepositoryProxy proxy;

    @Mock
    private CategoryRepositoryInterface dbRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldSaveInDbAndCache() {
        Category category = Category.builder().id(1L).name("TestCategory").build();
        when(dbRepository.save(category)).thenReturn(category);

        Category savedCategory = proxy.save(category);

        verify(dbRepository, times(1)).save(category);
        assertEquals(category, savedCategory);
        assertTrue(proxy.findAll().contains(savedCategory));
    }

    @Test
    void findById_shouldReturnFromCacheIfPresent() {
        Category category = Category.builder().id(2L).name("CachedCategory").build();
        when(dbRepository.save(category)).thenReturn(category);
        proxy.save(category);

        Optional<Category> retrievedCategory = proxy.findById(2L);

        verify(dbRepository, never()).findById(anyLong());
        assertTrue(retrievedCategory.isPresent());
        assertEquals(category, retrievedCategory.get());
    }

    @Test
    void findById_shouldGetFromDbIfNotCached() {
        Category category = Category.builder().id(3L).name("DbCategory").build();
        when(dbRepository.findById(3L)).thenReturn(Optional.of(category));

        Optional<Category> retrievedCategory = proxy.findById(3L);

        verify(dbRepository, times(1)).findById(3L);
        assertTrue(retrievedCategory.isPresent());
        assertEquals(category, retrievedCategory.get());
        assertTrue(proxy.findAll().contains(category));
    }

    @Test
    void findById_shouldReturnEmptyIfNotExists() {
        when(dbRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Category> retrievedCategory = proxy.findById(999L);

        verify(dbRepository, times(1)).findById(999L);
        assertTrue(retrievedCategory.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllFromCache() {
        Category category1 = Category.builder().id(1L).name("Category1").build();
        Category category2 = Category.builder().id(2L).name("Category2").build();
        when(dbRepository.save(category1)).thenReturn(category1);
        when(dbRepository.save(category2)).thenReturn(category2);
        proxy.save(category1);
        proxy.save(category2);

        List<Category> categories = proxy.findAll();

        assertEquals(2, categories.size());
        assertTrue(categories.contains(category1));
    }
}

class OperationRepositoryProxyTest {
    @InjectMocks
    private OperationRepositoryProxy proxy;

    @Mock
    private OperationRepositoryInterface dbRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_shouldSaveInDbAndCache() {
        Operation operation = Operation.builder().id(1L).build();
        when(dbRepository.save(operation)).thenReturn(operation);

        Operation savedOperation = proxy.save(operation);

        verify(dbRepository, times(1)).save(operation);
        assertEquals(operation, savedOperation);
        assertTrue(proxy.findAll().contains(savedOperation));
    }

    @Test
    void findById_shouldReturnFromCacheIfPresent() {
        Operation operation = Operation.builder().id(2L).build();
        when(dbRepository.save(operation)).thenReturn(operation);
        proxy.save(operation);

        Optional<Operation> retrievedOperation = proxy.findById(2L);

        verify(dbRepository, never()).findById(anyLong());
        assertTrue(retrievedOperation.isPresent());
        assertEquals(operation, retrievedOperation.get());
    }

    @Test
    void findById_shouldGetFromDbIfNotCached() {
        Operation operation = Operation.builder().id(3L).build();
        when(dbRepository.findById(3L)).thenReturn(Optional.of(operation));

        Optional<Operation> retrievedOperation = proxy.findById(3L);

        verify(dbRepository, times(1)).findById(3L);
        assertTrue(retrievedOperation.isPresent());
        assertEquals(operation, retrievedOperation.get());
        assertTrue(proxy.findAll().contains(operation));
    }

    @Test
    void findById_shouldReturnEmptyIfNotExists() {
        when(dbRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Operation> retrievedOperation = proxy.findById(999L);

        verify(dbRepository, times(1)).findById(999L);
        assertTrue(retrievedOperation.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllFromCache() {
        Operation operation1 = Operation.builder().id(1L).build();
        Operation operation2 = Operation.builder().id(2L).build();
        when(dbRepository.save(operation1)).thenReturn(operation1);
        when(dbRepository.save(operation2)).thenReturn(operation2);
        proxy.save(operation1);
        proxy.save(operation2);

        List<Operation> operations = proxy.findAll();

        assertEquals(2, operations.size());
        assertTrue(operations.contains(operation1));
        assertTrue(operations.contains(operation2));
    }
}