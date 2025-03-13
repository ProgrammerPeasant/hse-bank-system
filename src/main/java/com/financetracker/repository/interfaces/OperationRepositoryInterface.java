package com.financetracker.repository.interfaces;

import com.financetracker.model.Operation;
import com.financetracker.model.OperationType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OperationRepositoryInterface {
    Operation save(Operation operation);
    Optional<Operation> findById(Long id);
    List<Operation> findAll();
    List<Operation> findByBankAccountId(Long bankAccountId);
    List<Operation> findByType(OperationType type);
    List<Operation> findByCategoryId(Long categoryId);
    List<Operation> findByDateRange(LocalDate startDate, LocalDate endDate);
    void delete(Operation operation);
    void deleteById(Long id);
}