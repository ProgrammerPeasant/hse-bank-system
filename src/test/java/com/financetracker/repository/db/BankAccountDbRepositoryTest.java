package com.financetracker.repository.db;

import com.financetracker.model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BankAccountDbRepositoryTest {

    private BankAccountDbRepository repository;

    @BeforeEach
    void setUp() {
        repository = new BankAccountDbRepository();
    }

    @Test
    @DisplayName("Should save a new bank account with deep copy and generate an ID")
    void testSaveNewAccount() {
        BankAccount account = new BankAccount();
        account.setName("Test Account");
        account.setBalance(1000.0);

        BankAccount savedAccount = repository.save(account);

        assertNotNull(savedAccount.getId());
        assertEquals(account.getName(), savedAccount.getName());
        assertEquals(account.getBalance(), savedAccount.getBalance());

        // проверка на глубокую копию
        account.setName("Modified Name");
        Optional<BankAccount> retrievedAccount = repository.findById(savedAccount.getId());
        assertTrue(retrievedAccount.isPresent());
        assertEquals("Test Account", retrievedAccount.get().getName());
    }

    @Test
    @DisplayName("Should update an existing bank account with deep copy")
    void testUpdateExistingAccount() {
        BankAccount account = new BankAccount();
        account.setName("Initial Name");
        account.setBalance(1000.0);

        BankAccount savedAccount = repository.save(account);
        Long id = savedAccount.getId();

        savedAccount.setName("Updated Name");
        savedAccount.setBalance(2000.0);

        BankAccount updatedAccount = repository.save(savedAccount);

        assertEquals(id, updatedAccount.getId());
        assertEquals("Updated Name", updatedAccount.getName());
        assertEquals(2000.0, updatedAccount.getBalance());

        updatedAccount.setName("Modified Again");
        Optional<BankAccount> retrievedAccount = repository.findById(id);
        assertTrue(retrievedAccount.isPresent());
        assertEquals("Modified Again", retrievedAccount.get().getName());
    }

    @Test
    @DisplayName("Should find an account by ID with deep copy when it exists")
    void testFindByIdWhenExists() {
        // Arrange
        BankAccount account = new BankAccount();
        account.setName("Test Account");
        account.setBalance(1000.0);
        BankAccount savedAccount = repository.save(account);

        Optional<BankAccount> foundAccountOptional = repository.findById(savedAccount.getId());

        assertTrue(foundAccountOptional.isPresent());
        BankAccount foundAccount = foundAccountOptional.get();
        assertEquals(savedAccount.getId(), foundAccount.getId());
        assertEquals(savedAccount.getName(), foundAccount.getName());
        assertEquals(savedAccount.getBalance(), foundAccount.getBalance());

        foundAccount.setName("Modified Name");
        Optional<BankAccount> retrievedAccountAgain = repository.findById(savedAccount.getId());
        assertTrue(retrievedAccountAgain.isPresent());
        assertEquals("Test Account", retrievedAccountAgain.get().getName());
    }

    @Test
    @DisplayName("Should return an empty Optional when finding by non-existent ID")
    void testFindByIdWhenNotExists() {
        Optional<BankAccount> foundAccount = repository.findById(999L);

        assertFalse(foundAccount.isPresent());
    }

    @Test
    @DisplayName("Should return deep copies of all saved accounts")
    void testFindAll() {
        BankAccount account1 = new BankAccount();
        account1.setName("Account 1");
        account1.setBalance(1000.0);

        BankAccount account2 = new BankAccount();
        account2.setName("Account 2");
        account2.setBalance(2000.0);

        repository.save(account1);
        repository.save(account2);

        List<BankAccount> accounts = repository.findAll();

        assertEquals(2, accounts.size());

        accounts.get(0).setName("Modified Account");

        List<BankAccount> accountsAgain = repository.findAll();
        boolean hasModifiedName = false;
        for(BankAccount account : accountsAgain) {
            if("Modified Account".equals(account.getName())) {
                hasModifiedName = true;
                break;
            }
        }
        assertFalse(hasModifiedName);
    }

    @Test
    @DisplayName("Should delete an account by reference")
    void testDelete() {
        BankAccount account = new BankAccount();
        account.setName("Test Account");
        account.setBalance(1000.0);
        BankAccount savedAccount = repository.save(account);

        repository.delete(savedAccount);

        Optional<BankAccount> foundAccount = repository.findById(savedAccount.getId());
        assertFalse(foundAccount.isPresent());
    }

    @Test
    @DisplayName("Should delete an account by ID")
    void testDeleteById() {
        BankAccount account = new BankAccount();
        account.setName("Test Account");
        account.setBalance(1000.0);
        BankAccount savedAccount = repository.save(account);

        repository.deleteById(savedAccount.getId());

        Optional<BankAccount> foundAccount = repository.findById(savedAccount.getId());
        assertFalse(foundAccount.isPresent());
    }
}