package com.financetracker.service;

import com.financetracker.model.Operation;
import com.financetracker.repository.BankAccountRepository;
import com.financetracker.repository.OperationRepository;
import org.springframework.stereotype.Service;

@Service
public class IncomeOperationProcessor extends OperationProcessingTemplate {

    private final OperationRepository operationRepository;

    public IncomeOperationProcessor(BankAccountRepository bankAccountRepository,
                                    OperationRepository operationRepository) {
        super(bankAccountRepository);
        this.operationRepository = operationRepository;
    }

    @Override
    protected Operation executeOperation(Operation operation) {
        return operationRepository.save(operation);
    }

    @Override
    protected double calculateNewBalance(double currentBalance, Operation operation) {
        return currentBalance + operation.getAmount();
    }
}