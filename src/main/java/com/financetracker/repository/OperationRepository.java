package com.financetracker.repository;

import com.financetracker.model.Operation;
import com.financetracker.model.OperationType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OperationRepository {
    private final Map<Long, Operation> operations = new HashMap<>();
    private Long idSequence = 1L;

    public Operation save(Operation operation) {
        if (operation.getId() == null) {
            operation.setId(idSequence++);
        }
        operations.put(operation.getId(), operation);
        return operation;
    }

    public Optional<Operation> findById(Long id) {
        return Optional.ofNullable(operations.get(id));
    }

    public List<Operation> findAll() {
        return new ArrayList<>(operations.values());
    }

    public List<Operation> findByBankAccountId(Long bankAccountId) {
        return operations.values().stream()
                .filter(operation -> operation.getBankAccountId().equals(bankAccountId))
                .collect(Collectors.toList());
    }

    public List<Operation> findByType(OperationType type) {
        return operations.values().stream()
                .filter(operation -> operation.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Operation> findByCategoryId(Long categoryId) {
        return operations.values().stream()
                .filter(operation -> operation.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public List<Operation> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return operations.values().stream()
                .filter(operation -> !operation.getDate().isBefore(startDate)
                        && !operation.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public void delete(Operation operation) {
        operations.remove(operation.getId());
    }

    public void deleteById(Long id) {
        operations.remove(id);
    }
}