package com.financetracker.service;

import com.financetracker.model.BankAccount;
import com.financetracker.model.Operation;
import com.financetracker.repository.BankAccountRepository;
import com.financetracker.repository.OperationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpenseOperationProcessor extends OperationProcessingTemplate {

    private final OperationRepository operationRepository;

    public ExpenseOperationProcessor(BankAccountRepository bankAccountRepository,
                                     OperationRepository operationRepository) {
        super(bankAccountRepository);
        this.operationRepository = operationRepository;
    }

    @Override
    protected Operation executeOperation(Operation operation) {
        return operationRepository.save(operation);
    }

    @Override
    protected void validateOperation(Operation operation) {
        super.validateOperation(operation);

        Optional<BankAccount> bankAccountOpt =
                bankAccountRepository.findById(operation.getBankAccountId());

        if (bankAccountOpt.isPresent() &&
                bankAccountOpt.get().getBalance() < operation.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds for this expense");
        }
    }

    @Override
    protected double calculateNewBalance(double currentBalance, Operation operation) {
        return currentBalance - operation.getAmount();
    }
}