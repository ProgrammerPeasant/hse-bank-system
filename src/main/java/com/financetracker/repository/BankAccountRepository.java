package com.financetracker.repository;

import com.financetracker.model.BankAccount;
import com.financetracker.repository.interfaces.BankAccountRepositoryInterface;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class BankAccountRepository implements BankAccountRepositoryInterface {
    private final Map<Long, BankAccount> accounts = new HashMap<>();
    private Long idSequence = 1L;

    @Override
    public BankAccount save(BankAccount bankAccount) {
        if (bankAccount.getId() == null) {
            bankAccount.setId(idSequence++);
        }
        accounts.put(bankAccount.getId(), bankAccount);
        return bankAccount;
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public void delete(BankAccount bankAccount) {
        accounts.remove(bankAccount.getId());
    }

    @Override
    public void deleteById(Long id) {
        accounts.remove(id);
    }
}