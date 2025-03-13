package com.financetracker.service;

import com.financetracker.model.BankAccount;
import com.financetracker.model.Operation;
import com.financetracker.repository.BankAccountRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public abstract class OperationProcessingTemplate {

    protected final BankAccountRepository bankAccountRepository;

    protected OperationProcessingTemplate(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public final Operation processOperation(Operation operation) {
        LocalDateTime startTime = LocalDateTime.now();

        validateOperation(operation);

        Operation processedOperation = executeOperation(operation);

        updateBankAccountBalance(processedOperation);

        recordExecutionTime(processedOperation, startTime);

        return processedOperation;
    }

    protected abstract Operation executeOperation(Operation operation);

    protected void validateOperation(Operation operation) {
        if (operation.getAmount() <= 0) {
            throw new IllegalArgumentException("Operation amount must be positive");
        }

        if (operation.getBankAccountId() == null) {
            throw new IllegalArgumentException("Bank account ID cannot be null");
        }

        Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(operation.getBankAccountId());
        if (bankAccountOpt.isEmpty()) {
            throw new IllegalArgumentException("Bank account not found");
        }
    }

    protected void updateBankAccountBalance(Operation operation) {
        Optional<BankAccount> bankAccountOpt = bankAccountRepository.findById(operation.getBankAccountId());
        if (bankAccountOpt.isPresent()) {
            BankAccount bankAccount = bankAccountOpt.get();
            double newBalance = calculateNewBalance(bankAccount.getBalance(), operation);
            bankAccount.setBalance(newBalance);
            bankAccountRepository.save(bankAccount);
        }
    }

    protected abstract double calculateNewBalance(double currentBalance, Operation operation);

    private void recordExecutionTime(Operation operation, LocalDateTime startTime) {
        LocalDateTime endTime = LocalDateTime.now();
    }
}