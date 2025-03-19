package com.financetracker.repository.db;

import com.financetracker.model.Operation;
import com.financetracker.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OperationDbRepositoryTest {

    private OperationDbRepository repository;

    @BeforeEach
    void setUp() {
        repository = new OperationDbRepository();
    }

    @Test
    void save_shouldPersistOperation() {
        Operation operation = Operation.builder()
                .type(OperationType.EXPENSE)
                .bankAccountId(1L)
                .amount(100)
                .date(LocalDate.now())
                .description("Test")
                .categoryId(1L)
                .build();

        Operation savedOperation = repository.save(operation);

        assertNotNull(savedOperation.getId());
        assertEquals(operation.getType(), savedOperation.getType());
        assertEquals(operation.getBankAccountId(), savedOperation.getBankAccountId());

        Optional<Operation> retrieved = repository.findById(savedOperation.getId());
        assertTrue(retrieved.isPresent());
    }

    @Test
    void findById_shouldReturnOperation_whenPresent() {
        Operation operation = Operation.builder()
                .type(OperationType.INCOME)
                .bankAccountId(1L)
                .amount(200)
                .date(LocalDate.now())
                .description("Income Test")
                .categoryId(2L)
                .build();

        Operation savedOperation = repository.save(operation);

        Optional<Operation> retrievedOperation = repository.findById(savedOperation.getId());

        assertTrue(retrievedOperation.isPresent());
        assertEquals(savedOperation.getId(), retrievedOperation.get().getId());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotPresent() {
        Optional<Operation> retrievedOperation = repository.findById(999L);
        assertTrue(retrievedOperation.isEmpty());
    }

    @Test
    void findAll_shouldReturnAllOperations() {
        repository.save(Operation.builder().type(OperationType.EXPENSE).build());
        repository.save(Operation.builder().type(OperationType.INCOME).build());

        List<Operation> operations = repository.findAll();
        assertEquals(2, operations.size());
    }

    @Test
    void findByBankAccountId_shouldReturnOperationsWithGivenBankAccountId() {
        repository.save(Operation.builder().bankAccountId(1L).build());
        repository.save(Operation.builder().bankAccountId(2L).build());
        repository.save(Operation.builder().bankAccountId(1L).build());

        List<Operation> operations = repository.findByBankAccountId(1L);
        assertEquals(2, operations.size());
    }

    @Test
    void findByType_shouldReturnOperationsWithGivenType() {
        repository.save(Operation.builder().type(OperationType.EXPENSE).build());
        repository.save(Operation.builder().type(OperationType.INCOME).build());
        repository.save(Operation.builder().type(OperationType.EXPENSE).build());

        List<Operation> operations = repository.findByType(OperationType.EXPENSE);
        assertEquals(2, operations.size());
    }

    @Test
    void findByCategoryId_shouldReturnOperationsWithGivenCategoryId() {
        repository.save(Operation.builder().categoryId(1L).build());
        repository.save(Operation.builder().categoryId(2L).build());
        repository.save(Operation.builder().categoryId(1L).build());

        List<Operation> operations = repository.findByCategoryId(1L);
        assertEquals(2, operations.size());
    }

    @Test
    void findByDateRange_shouldReturnOperationsWithinDateRange() {
        repository.save(Operation.builder().date(LocalDate.of(2023, 1, 1)).build());
        repository.save(Operation.builder().date(LocalDate.of(2023, 2, 1)).build());
        repository.save(Operation.builder().date(LocalDate.of(2023, 3, 1)).build());

        List<Operation> operations = repository.findByDateRange(LocalDate.of(2023, 2, 1), LocalDate.of(2023, 3, 1));
        assertEquals(2, operations.size());
    }

    @Test
    void delete_shouldRemoveOperation() {
        Operation operation = repository.save(Operation.builder().type(OperationType.EXPENSE).build());
        repository.delete(operation);

        Optional<Operation> retrieved = repository.findById(operation.getId());
        assertTrue(retrieved.isEmpty());
    }

    @Test
    void deleteById_shouldRemoveOperationById() {
        Operation operation = repository.save(Operation.builder().type(OperationType.EXPENSE).build());
        repository.deleteById(operation.getId());

        Optional<Operation> retrieved = repository.findById(operation.getId());
        assertTrue(retrieved.isEmpty());
    }
}