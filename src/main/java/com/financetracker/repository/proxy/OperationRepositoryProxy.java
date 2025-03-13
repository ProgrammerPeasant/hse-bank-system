package com.financetracker.repository.proxy;

import com.financetracker.model.Operation;
import com.financetracker.model.OperationType;
import com.financetracker.repository.interfaces.OperationRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class OperationRepositoryProxy implements OperationRepositoryInterface {
    private final Map<Long, Operation> cache = new ConcurrentHashMap<>();

    private final OperationRepositoryInterface dbRepository;

    @Autowired
    public OperationRepositoryProxy(@Qualifier("operationDbRepository")
                                    OperationRepositoryInterface dbRepository) {
        this.dbRepository = dbRepository;
        initializeCache();
    }

    private void initializeCache() {
        List<Operation> allOperations = dbRepository.findAll();
        for (Operation operation : allOperations) {
            cache.put(operation.getId(), operation);
        }
    }

    @Override
    public Operation save(Operation operation) {
        Operation savedOperation = dbRepository.save(operation);
        cache.put(savedOperation.getId(), savedOperation);
        return savedOperation;
    }

    @Override
    public Optional<Operation> findById(Long id) {
        Operation cachedOperation = cache.get(id);
        if (cachedOperation != null) {
            return Optional.of(cachedOperation);
        }

        Optional<Operation> operationFromDb = dbRepository.findById(id);

        operationFromDb.ifPresent(operation -> cache.put(id, operation));

        return operationFromDb;
    }

    @Override
    public List<Operation> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public List<Operation> findByBankAccountId(Long bankAccountId) {
        return cache.values().stream()
                .filter(operation -> operation.getBankAccountId().equals(bankAccountId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByType(OperationType type) {
        return cache.values().stream()
                .filter(operation -> operation.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByCategoryId(Long categoryId) {
        return cache.values().stream()
                .filter(operation -> operation.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return cache.values().stream()
                .filter(operation -> !operation.getDate().isBefore(startDate)
                        && !operation.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Operation operation) {
        dbRepository.delete(operation);
        cache.remove(operation.getId());
    }

    @Override
    public void deleteById(Long id) {
        dbRepository.deleteById(id);
        cache.remove(id);
    }
}