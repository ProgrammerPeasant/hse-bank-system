package com.financetracker.factory;

import com.financetracker.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FinanceEntityFactory {

    public BankAccount createBankAccount(String name, double initialBalance) {
        return BankAccount.builder()
                .name(name)
                .balance(initialBalance)
                .build();
    }

    public Category createCategory(CategoryType type, String name) {
        return Category.builder()
                .type(type)
                .name(name)
                .build();
    }

    public Operation createIncomeOperation(Long bankAccountId, double amount, LocalDate date,
                                           String description, Long categoryId) {
        return Operation.builder()
                .type(OperationType.INCOME)
                .bankAccountId(bankAccountId)
                .amount(amount)
                .date(date)
                .description(description)
                .categoryId(categoryId)
                .build();
    }

    public Operation createExpenseOperation(Long bankAccountId, double amount, LocalDate date,
                                            String description, Long categoryId) {
        return Operation.builder()
                .type(OperationType.EXPENSE)
                .bankAccountId(bankAccountId)
                .amount(amount)
                .date(date)
                .description(description)
                .categoryId(categoryId)
                .build();
    }
}