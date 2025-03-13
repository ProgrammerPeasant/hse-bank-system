package com.financetracker.repository.interfaces;

import com.financetracker.model.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepositoryInterface {
    BankAccount save(BankAccount bankAccount);
    Optional<BankAccount> findById(Long id);
    List<BankAccount> findAll();
    void delete(BankAccount bankAccount);
    void deleteById(Long id);
}