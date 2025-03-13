package com.financetracker.repository.proxy;

import com.financetracker.model.BankAccount;
import com.financetracker.repository.interfaces.BankAccountRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BankAccountRepositoryProxy implements BankAccountRepositoryInterface {
    private final Map<Long, BankAccount> cache = new ConcurrentHashMap<>();

    private final BankAccountRepositoryInterface dbRepository;

    @Autowired
    public BankAccountRepositoryProxy(@Qualifier("bankAccountDbRepository")
                                      BankAccountRepositoryInterface dbRepository) {
        this.dbRepository = dbRepository;
        initializeCache();
    }

    private void initializeCache() {
        List<BankAccount> allAccounts = dbRepository.findAll();
        for (BankAccount account : allAccounts) {
            cache.put(account.getId(), account);
        }
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        BankAccount savedAccount = dbRepository.save(bankAccount);
        cache.put(savedAccount.getId(), savedAccount);
        return savedAccount;
    }

    @Override
    public Optional<BankAccount> findById(Long id) {
        BankAccount cachedAccount = cache.get(id);
        if (cachedAccount != null) {
            return Optional.of(cachedAccount);
        }

        Optional<BankAccount> accountFromDb = dbRepository.findById(id);

        accountFromDb.ifPresent(account -> cache.put(id, account));

        return accountFromDb;
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public void delete(BankAccount bankAccount) {
        dbRepository.delete(bankAccount);
        cache.remove(bankAccount.getId());
    }

    @Override
    public void deleteById(Long id) {
        dbRepository.deleteById(id);
        cache.remove(id);
    }
}