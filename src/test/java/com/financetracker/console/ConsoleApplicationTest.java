package com.financetracker.console;

import com.financetracker.facade.FinanceTrackerFacade;
import com.financetracker.model.BankAccount;
import com.financetracker.model.Category;
import com.financetracker.model.CategoryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsoleApplicationTest {
    @Mock
    private FinanceTrackerFacade facadeMock;

    private ConsoleApplication consoleApplication;

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        consoleApplication = new ConsoleApplication(facadeMock);
    }

    @Test
    void testInitializeSampleData_WhenNoAccounts() {
        when(facadeMock.getAllBankAccounts()).thenReturn(new ArrayList<>());

        BankAccount mainAccount = BankAccount.builder()
                .id(1L)
                .name("Основной счет")
                .balance(100000.0)
                .build();

        BankAccount savingsAccount = BankAccount.builder()
                .id(2L)
                .name("Сберегательный счет")
                .balance(50000.0)
                .build();

        Category salary = Category.builder()
                .id(1L)
                .name("Зарплата")
                .type(CategoryType.INCOME)
                .build();

        Category interest = Category.builder()
                .id(2L)
                .name("Проценты по вкладу")
                .type(CategoryType.INCOME)
                .build();

        Category gifts = Category.builder()
                .id(3L)
                .name("Подарки")
                .type(CategoryType.INCOME)
                .build();

        Category food = Category.builder()
                .id(4L)
                .name("Продукты")
                .type(CategoryType.EXPENSE)
                .build();

        Category entertainment = Category.builder()
                .id(5L)
                .name("Развлечения")
                .type(CategoryType.EXPENSE)
                .build();

        Category transport = Category.builder()
                .id(6L)
                .name("Транспорт")
                .type(CategoryType.EXPENSE)
                .build();

        Category utilities = Category.builder()
                .id(7L)
                .name("Коммунальные услуги")
                .type(CategoryType.EXPENSE)
                .build();

        when(facadeMock.createBankAccount("Основной счет", 100000.0)).thenReturn(mainAccount);
        when(facadeMock.createBankAccount("Сберегательный счет", 50000.0)).thenReturn(savingsAccount);

        when(facadeMock.createCategory(CategoryType.INCOME, "Зарплата")).thenReturn(salary);
        when(facadeMock.createCategory(CategoryType.INCOME, "Проценты по вкладу")).thenReturn(interest);
        when(facadeMock.createCategory(CategoryType.INCOME, "Подарки")).thenReturn(gifts);

        when(facadeMock.createCategory(CategoryType.EXPENSE, "Продукты")).thenReturn(food);
        when(facadeMock.createCategory(CategoryType.EXPENSE, "Развлечения")).thenReturn(entertainment);
        when(facadeMock.createCategory(CategoryType.EXPENSE, "Транспорт")).thenReturn(transport);
        when(facadeMock.createCategory(CategoryType.EXPENSE, "Коммунальные услуги")).thenReturn(utilities);

        consoleApplication.initializeSampleData();

        verify(facadeMock).createBankAccount("Основной счет", 100000.0);
        verify(facadeMock).createBankAccount("Сберегательный счет", 50000.0);

        verify(facadeMock).createCategory(CategoryType.INCOME, "Зарплата");
        verify(facadeMock).createCategory(CategoryType.INCOME, "Проценты по вкладу");
        verify(facadeMock).createCategory(CategoryType.INCOME, "Подарки");

        verify(facadeMock).createCategory(CategoryType.EXPENSE, "Продукты");
        verify(facadeMock).createCategory(CategoryType.EXPENSE, "Развлечения");
        verify(facadeMock).createCategory(CategoryType.EXPENSE, "Транспорт");
        verify(facadeMock).createCategory(CategoryType.EXPENSE, "Коммунальные услуги");

        verify(facadeMock, times(3)).createIncomeOperation(anyLong(), anyDouble(), any(LocalDate.class), anyString(), anyLong());
        verify(facadeMock, times(4)).createExpenseOperation(anyLong(), anyDouble(), any(LocalDate.class), anyString(), anyLong());

        assertTrue(outputStream.toString().contains("Инициализация демонстрационных данных"));
    }

    @Test
    void testInitializeSampleData_WhenAccountsExist() {
        List<BankAccount> existingAccounts = new ArrayList<>();
        existingAccounts.add(BankAccount.builder().id(1L).name("Существующий счет").balance(100.0).build());
        when(facadeMock.getAllBankAccounts()).thenReturn(existingAccounts);

        consoleApplication.initializeSampleData();

        verify(facadeMock, never()).createBankAccount(anyString(), anyDouble());
        verify(facadeMock, never()).createCategory(any(CategoryType.class), anyString());
        verify(facadeMock, never()).createIncomeOperation(anyLong(), anyDouble(), any(LocalDate.class), anyString(), anyLong());
        verify(facadeMock, never()).createExpenseOperation(anyLong(), anyDouble(), any(LocalDate.class), anyString(), anyLong());
    }

    @Test
    void testCreateBankAccount() throws Exception {
        String simulatedInput = "Test Account\n5000\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Field scannerField = ConsoleApplication.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(consoleApplication, new Scanner(System.in));

        BankAccount createdAccount = BankAccount.builder()
                .id(100L)
                .name("Test Account")
                .balance(5000)
                .build();

        when(facadeMock.createBankAccount("Test Account", 5000.0)).thenReturn(createdAccount);

        java.lang.reflect.Method method = ConsoleApplication.class.getDeclaredMethod("createBankAccount");
        method.setAccessible(true);
        method.invoke(consoleApplication);

        verify(facadeMock).createBankAccount("Test Account", 5000.0);
        assertTrue(outputStream.toString().contains("Счет успешно создан с ID: 100"));
    }

    @Test
    void testViewAllBankAccounts_WithAccounts() throws Exception {
        List<BankAccount> accounts = Arrays.asList(
                BankAccount.builder().id(1L).name("Счет 1").balance(1000.0).build(),
                BankAccount.builder().id(2L).name("Счет 2").balance(2000.0).build()
        );

        when(facadeMock.getAllBankAccounts()).thenReturn(accounts);

        java.lang.reflect.Method method = ConsoleApplication.class.getDeclaredMethod("viewAllBankAccounts");
        method.setAccessible(true);
        method.invoke(consoleApplication);

        String output = outputStream.toString();
        assertTrue(output.contains("СПИСОК ВСЕХ СЧЕТОВ"));
        assertTrue(output.contains("Счет 1"));
        assertTrue(output.contains("Счет 2"));
        assertTrue(output.contains("1000"));
        assertTrue(output.contains("2000"));
    }

    @Test
    void testViewAllBankAccounts_WithNoAccounts() throws Exception {
        when(facadeMock.getAllBankAccounts()).thenReturn(new ArrayList<>());

        java.lang.reflect.Method method = ConsoleApplication.class.getDeclaredMethod("viewAllBankAccounts");
        method.setAccessible(true);
        method.invoke(consoleApplication);

        assertTrue(outputStream.toString().contains("Счета отсутствуют"));
    }

    @Test
    void testEditBankAccount_Success() throws Exception {
        List<BankAccount> accounts = List.of(
                BankAccount.builder().id(1L).name("Старое название").balance(1000.0).build()
        );

        when(facadeMock.getAllBankAccounts()).thenReturn(accounts);

        BankAccount account = BankAccount.builder()
                .id(1L)
                .name("Старое название")
                .balance(1000.0)
                .build();

        when(facadeMock.getBankAccountById(1L)).thenReturn(account);

        String simulatedInput = "1\nНовое название\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Field scannerField = ConsoleApplication.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(consoleApplication, new Scanner(System.in));

        java.lang.reflect.Method method = ConsoleApplication.class.getDeclaredMethod("editBankAccount");
        method.setAccessible(true);
        method.invoke(consoleApplication);

        ArgumentCaptor<BankAccount> accountCaptor = ArgumentCaptor.forClass(BankAccount.class);
        verify(facadeMock).updateBankAccount(accountCaptor.capture());

        BankAccount updatedAccount = accountCaptor.getValue();
        assertEquals("Новое название", updatedAccount.getName());
        assertEquals(1000.0, updatedAccount.getBalance());
        assertTrue(outputStream.toString().contains("Счет успешно обновлен"));
    }

    @Test
    void testCreateCategory() throws Exception {
        String simulatedInput = "1\nНовая категория дохода\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Field scannerField = ConsoleApplication.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(consoleApplication, new Scanner(System.in));

        Category createdCategory = Category.builder()
                .id(100L)
                .name("Новая категория дохода")
                .type(CategoryType.INCOME)
                .build();

        when(facadeMock.createCategory(CategoryType.INCOME, "Новая категория дохода")).thenReturn(createdCategory);

        java.lang.reflect.Method method = ConsoleApplication.class.getDeclaredMethod("createCategory");
        method.setAccessible(true);
        method.invoke(consoleApplication);

        verify(facadeMock).createCategory(CategoryType.INCOME, "Новая категория дохода");
        assertTrue(outputStream.toString().contains("Категория успешно создана с ID: 100"));
    }


}