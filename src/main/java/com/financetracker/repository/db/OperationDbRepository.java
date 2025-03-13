package com.financetracker.repository.db;

import com.financetracker.model.Operation;
import com.financetracker.model.OperationType;
import com.financetracker.repository.interfaces.OperationRepositoryInterface;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository("operationDbRepository")
public class OperationDbRepository implements OperationRepositoryInterface {

    private final Map<Long, Operation> dbStorage = new HashMap<>();
    private Long idSequence = 1L;

    @Override
    public Operation save(Operation operation) {
        if (operation.getId() == null) {
            operation.setId(idSequence++);
        }
        // Создаем глубокую копию для имитации сохранения в БД
        Operation savedOperation = Operation.builder()
                .id(operation.getId())
                .type(operation.getType())
                .bankAccountId(operation.getBankAccountId())
                .amount(operation.getAmount())
                .date(operation.getDate())
                .description(operation.getDescription())
                .categoryId(operation.getCategoryId())
                .build();

        dbStorage.put(savedOperation.getId(), savedOperation);
        return savedOperation;
    }

    @Override
    public Optional<Operation> findById(Long id) {
        Operation operation = dbStorage.get(id);
        if (operation == null) {
            return Optional.empty();
        }

        return Optional.of(Operation.builder()
                .id(operation.getId())
                .type(operation.getType())
                .bankAccountId(operation.getBankAccountId())
                .amount(operation.getAmount())
                .date(operation.getDate())
                .description(operation.getDescription())
                .categoryId(operation.getCategoryId())
                .build());
    }

    @Override
    public List<Operation> findAll() {
        List<Operation> result = new ArrayList<>();
        for (Operation operation : dbStorage.values()) {
            result.add(Operation.builder()
                    .id(operation.getId())
                    .type(operation.getType())
                    .bankAccountId(operation.getBankAccountId())
                    .amount(operation.getAmount())
                    .date(operation.getDate())
                    .description(operation.getDescription())
                    .categoryId(operation.getCategoryId())
                    .build());
        }
        return result;
    }

    @Override
    public List<Operation> findByBankAccountId(Long bankAccountId) {
        return dbStorage.values().stream()
                .filter(operation -> operation.getBankAccountId().equals(bankAccountId))
                .map(operation -> Operation.builder()
                        .id(operation.getId())
                        .type(operation.getType())
                        .bankAccountId(operation.getBankAccountId())
                        .amount(operation.getAmount())
                        .date(operation.getDate())
                        .description(operation.getDescription())
                        .categoryId(operation.getCategoryId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByType(OperationType type) {
        return dbStorage.values().stream()
                .filter(operation -> operation.getType() == type)
                .map(operation -> Operation.builder()
                        .id(operation.getId())
                        .type(operation.getType())
                        .bankAccountId(operation.getBankAccountId())
                        .amount(operation.getAmount())
                        .date(operation.getDate())
                        .description(operation.getDescription())
                        .categoryId(operation.getCategoryId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByCategoryId(Long categoryId) {
        return dbStorage.values().stream()
                .filter(operation -> operation.getCategoryId().equals(categoryId))
                .map(operation -> Operation.builder()
                        .id(operation.getId())
                        .type(operation.getType())
                        .bankAccountId(operation.getBankAccountId())
                        .amount(operation.getAmount())
                        .date(operation.getDate())
                        .description(operation.getDescription())
                        .categoryId(operation.getCategoryId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return dbStorage.values().stream()
                .filter(operation -> !operation.getDate().isBefore(startDate)
                        && !operation.getDate().isAfter(endDate))
                .map(operation -> Operation.builder()
                        .id(operation.getId())
                        .type(operation.getType())
                        .bankAccountId(operation.getBankAccountId())
                        .amount(operation.getAmount())
                        .date(operation.getDate())
                        .description(operation.getDescription())
                        .categoryId(operation.getCategoryId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Operation operation) {
        dbStorage.remove(operation.getId());
    }

    @Override
    public void deleteById(Long id) {
        dbStorage.remove(id);
    }
}