package com.financetracker.repository.db;

import com.financetracker.model.BankAccount;
import com.financetracker.repository.interfaces.BankAccountRepositoryInterface;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class BankAccountDbRepository implements BankAccountRepositoryInterface {
    // вместо реально бд заглушка с глубоким копированием для имитации сохрания и
    // возвращение копии как получение данных из бд

    private final Map<Long, BankAccount> dbStorage = new HashMap<>();
    private Long idSequence = 1L;

    @Override
    public BankAccount save(BankAccount bankAccount) {
        if (bankAccount.getId() == null) {
            bankAccount.setId(idSequence++);
        }
        BankAccount savedAccount = BankAccount.builder()
                .id(bankAccount.getId())
                .name(bankAccount.getName())
                .balance(bankAccount.getBalance())
                .build();

        dbStorage.put(savedAccount.getId(), savedAccount);
        return savedAccount;
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        BankAccount account = dbStorage.get(id);
        if (account == null) {
            return Optional.empty();
        }

        return Optional.of(BankAccount.builder()
                .id(account.getId())
                .name(account.getName())
                .balance(account.getBalance())
                .build());
    }

    @Override
    public List<BankAccount> findAll() {
        List<BankAccount> result = new ArrayList<>();
        for (BankAccount account : dbStorage.values()) {
            result.add(BankAccount.builder()
                    .id(account.getId())
                    .name(account.getName())
                    .balance(account.getBalance())
                    .build());
        }
        return result;
    }

    @Override
    public void delete(BankAccount bankAccount) {
        dbStorage.remove(bankAccount.getId());
    }

    @Override
    public void deleteById(Long id) {
        dbStorage.remove(id);
    }
}