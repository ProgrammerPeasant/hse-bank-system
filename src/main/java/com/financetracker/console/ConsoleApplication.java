package com.financetracker.console;

import com.financetracker.facade.FinanceTrackerFacade;
import com.financetracker.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;



@Component
public class ConsoleApplication implements CommandLineRunner {
    public static final String WRONG_OPTION = "Неверная опция. Пожалуйста, попробуйте снова.";
    public static final String CHOOSE_OPTION = "Выберите опцию: ";
    public static final String NOT_FOUND = "не найдено";

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите целое число.");
            }
        }
    }

    private String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private double readDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное число.");
            }
        }
    }

    private long readLongInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine().trim();
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите корректное целое число.");
            }
        }
    }

    private final FinanceTrackerFacade facade;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    public ConsoleApplication(FinanceTrackerFacade facade) {
        this.facade = facade;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run(String... args) {
        printHeader();
        initializeSampleData();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readIntInput(CHOOSE_OPTION);

            switch (choice) {
                case 1:
                    manageBankAccounts();
                    break;
                case 2:
                    manageCategories();
                    break;
                case 3:
                    manageOperations();
                    break;
                case 4:
                    analyzeData();
                    break;
                case 5:
                    // importExportData(); TODO - implement this method
                    break;
                case 0:
                    System.out.println("Выход из программы...");
                    running = false;
                    break;
                default:
                    System.out.println(WRONG_OPTION);
            }
        }
    }

    private void printHeader() {
        System.out.println("      СИСТЕМА УЧЕТА ФИНАНСОВ - ВЕРСИЯ 1.0        ");
        System.out.println("=================================================");
        System.out.println(" Текущая дата: 2025-03-13 18:34:24               ");
        System.out.println(" Пользователь: ProgrammerPeasant                 ");
        System.out.println("=================================================");
    }

    private void initializeSampleData() {
        // Создаем демонстрационные данные для примера
        if (facade.getAllBankAccounts().isEmpty()) {
            System.out.println("Инициализация демонстрационных данных...");

            // Создаем счета
            BankAccount mainAccount = facade.createBankAccount("Основной счет", 100000.0);
            BankAccount savingsAccount = facade.createBankAccount("Сберегательный счет", 50000.0);

            // Создаем категории доходов
            Category salary = facade.createCategory(CategoryType.INCOME, "Зарплата");
            Category interest = facade.createCategory(CategoryType.INCOME, "Проценты по вкладу");
            Category gifts = facade.createCategory(CategoryType.INCOME, "Подарки");

            // Создаем категории расходов
            Category food = facade.createCategory(CategoryType.EXPENSE, "Продукты");
            Category entertainment = facade.createCategory(CategoryType.EXPENSE, "Развлечения");
            Category transport = facade.createCategory(CategoryType.EXPENSE, "Транспорт");
            Category utilities = facade.createCategory(CategoryType.EXPENSE, "Коммунальные услуги");

            // Создаем операции
            facade.createIncomeOperation(mainAccount.getId(), 80000.0, LocalDate.of(2025, 2, 10),
                    "Зарплата за февраль", salary.getId());
            facade.createIncomeOperation(savingsAccount.getId(), 1500.0, LocalDate.of(2025, 2, 15),
                    "Проценты по вкладу", interest.getId());
            facade.createIncomeOperation(mainAccount.getId(), 5000.0, LocalDate.of(2025, 3, 1),
                    "Подарок на день рождения", gifts.getId());

            facade.createExpenseOperation(mainAccount.getId(), 12000.0, LocalDate.of(2025, 2, 12),
                    "Еженедельный поход в супермаркет", food.getId());
            facade.createExpenseOperation(mainAccount.getId(), 5000.0, LocalDate.of(2025, 2, 20),
                    "Поход в кино и ресторан", entertainment.getId());
            facade.createExpenseOperation(mainAccount.getId(), 2500.0, LocalDate.of(2025, 2, 25),
                    "Проездной билет", transport.getId());
            facade.createExpenseOperation(mainAccount.getId(), 8000.0, LocalDate.of(2025, 3, 5),
                    "Оплата счетов за коммунальные услуги", utilities.getId());

            System.out.println("Демонстрационные данные созданы успешно!");
        }
    }

    private void printMainMenu() {
        System.out.println("\nГЛАВНОЕ МЕНЮ:");
        System.out.println("1. Управление банковскими счетами");
        System.out.println("2. Управление категориями");
        System.out.println("3. Управление операциями");
        System.out.println("4. Аналитика");
        System.out.println("5. Импорт/Экспорт данных");
        System.out.println("0. Выход");
    }

    // ============== БАНКОВСКИЕ СЧЕТА ==============

    private void manageBankAccounts() {
        boolean running = true;
        while (running) {
            System.out.println("\nУПРАВЛЕНИЕ БАНКОВСКИМИ СЧЕТАМИ:");
            System.out.println("1. Создать новый счёт");
            System.out.println("2. Просмотреть все счета");
            System.out.println("3. Редактировать счёт");
            System.out.println("4. Удалить счёт");
            System.out.println("0. Вернуться в главное меню");

            int choice = readIntInput(CHOOSE_OPTION);

            switch (choice) {
                case 1:
                    createBankAccount();
                    break;
                case 2:
                    viewAllBankAccounts();
                    break;
                case 3:
                    editBankAccount();
                    break;
                case 4:
                    deleteBankAccount();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println(WRONG_OPTION);
            }
        }
    }

    private void createBankAccount() {
        System.out.println("\n--- СОЗДАНИЕ НОВОГО СЧЕТА ---");
        String name = readStringInput("Введите название счета: ");
        double initialBalance = readDoubleInput("Введите начальный баланс: ");

        BankAccount account = facade.createBankAccount(name, initialBalance);
        System.out.println("Счет успешно создан с ID: " + account.getId());
    }

    private void viewAllBankAccounts() {
        System.out.println("\n--- СПИСОК ВСЕХ СЧЕТОВ ---");
        List<BankAccount> accounts = facade.getAllBankAccounts();

        if (accounts.isEmpty()) {
            System.out.println("Счета отсутствуют.");
            return;
        }

        System.out.printf("%-5s | %-30s | %-15s\n", "ID", "Название", "Баланс");
        System.out.println("------------------------------------------------------");

        for (BankAccount account : accounts) {
            System.out.printf("%-5d | %-30s | %-15.2f\n",
                    account.getId(), account.getName(), account.getBalance());
        }
    }

    private void editBankAccount() {
        viewAllBankAccounts();

        System.out.println("\n--- РЕДАКТИРОВАНИЕ СЧЕТА ---");
        long id = readLongInput("Введите ID счета для редактирования (0 для отмены): ");

        if (id == 0) {
            return;
        }

        BankAccount account = facade.getBankAccountById(id);
        if (account == null) {
            System.out.println("Счет с ID " + id + NOT_FOUND);
            return;
        }

        System.out.println("Текущее название: " + account.getName());
        String name = readStringInput("Введите новое название (оставьте пустым, чтобы не менять): ");

        if (!name.isEmpty()) {
            account.setName(name);
        }

        facade.updateBankAccount(account);
        System.out.println("Счет успешно обновлен.");
    }

    private void deleteBankAccount() {
        viewAllBankAccounts();

        System.out.println("\n--- УДАЛЕНИЕ СЧЕТА ---");
        long id = readLongInput("Введите ID счета для удаления (0 для отмены): ");

        if (id == 0) {
            return;
        }

        BankAccount account = facade.getBankAccountById(id);
        if (account == null) {
            System.out.println("Счет с ID " + id + NOT_FOUND);
            return;
        }

        System.out.println("Вы уверены, что хотите удалить счет \"" + account.getName() + "\"? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            facade.deleteBankAccount(id);
            System.out.println("Счет успешно удален.");
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    // ============== КАТЕГОРИИ ==============

    // ============== КАТЕГОРИИ ==============

    private void manageCategories() {
        boolean running = true;
        while (running) {
            System.out.println("\nУПРАВЛЕНИЕ КАТЕГОРИЯМИ:");
            System.out.println("1. Создать новую категорию");
            System.out.println("2. Просмотреть все категории");
            System.out.println("3. Редактировать категорию");
            System.out.println("4. Удалить категорию");
            System.out.println("0. Вернуться в главное меню");

            int choice = readIntInput(CHOOSE_OPTION);

            switch (choice) {
                case 1:
                    createCategory();
                    break;
                case 2:
                    viewAllCategories();
                    break;
                case 3:
                    editCategory();
                    break;
                case 4:
                    deleteCategory();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println(WRONG_OPTION);
            }
        }
    }

    private void createCategory() {
        System.out.println("\n--- СОЗДАНИЕ НОВОЙ КАТЕГОРИИ ---");
        System.out.println("Выберите тип категории:");
        System.out.println("1. Доход");
        System.out.println("2. Расход");

        int typeChoice = readIntInput("Ваш выбор: ");
        CategoryType type = (typeChoice == 1) ? CategoryType.INCOME : CategoryType.EXPENSE;

        String name = readStringInput("Введите название категории: ");

        Category category = facade.createCategory(type, name);
        System.out.println("Категория успешно создана с ID: " + category.getId());
    }

    private void viewAllCategories() {
        System.out.println("\n--- СПИСОК ВСЕХ КАТЕГОРИЙ ---");
        List<Category> categories = facade.getAllCategories();

        if (categories.isEmpty()) {
            System.out.println("Категории отсутствуют.");
            return;
        }

        System.out.printf("%-5s | %-10s | %-30s\n", "ID", "Тип", "Название");
        System.out.println("------------------------------------------------------");

        for (Category category : categories) {
            String typeStr = (category.getType() == CategoryType.INCOME) ? "Доход" : "Расход";
            System.out.printf("%-5d | %-10s | %-30s\n",
                    category.getId(), typeStr, category.getName());
        }
    }

    private void editCategory() {
        viewAllCategories();

        System.out.println("\n--- РЕДАКТИРОВАНИЕ КАТЕГОРИИ ---");
        long id = readLongInput("Введите ID категории для редактирования (0 для отмены): ");

        if (id == 0) {
            return;
        }

        Category category = facade.getCategoryById(id);
        if (category == null) {
            System.out.println("Категория с ID " + id + NOT_FOUND);
            return;
        }

        System.out.println("Текущее название: " + category.getName());
        String name = readStringInput("Введите новое название (оставьте пустым, чтобы не менять): ");

        if (!name.isEmpty()) {
            category.setName(name);
        }

        facade.updateCategory(category);
        System.out.println("Категория успешно обновлена.");
    }

    private void deleteCategory() {
        viewAllCategories();

        System.out.println("\n--- УДАЛЕНИЕ КАТЕГОРИИ ---");
        long id = readLongInput("Введите ID категории для удаления (0 для отмены): ");

        if (id == 0) {
            return;
        }

        Category category = facade.getCategoryById(id);
        if (category == null) {
            System.out.println("Категория с ID " + id + NOT_FOUND);
            return;
        }

        System.out.println("Вы уверены, что хотите удалить категорию \"" + category.getName() + "\"? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            facade.deleteCategory(id);
            System.out.println("Категория успешно удалена.");
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    // ============== ОПЕРАЦИИ ==============

    private void manageOperations() {
        boolean running = true;
        while (running) {
            System.out.println("\nУПРАВЛЕНИЕ ОПЕРАЦИЯМИ:");
            System.out.println("1. Создать новую операцию дохода");
            System.out.println("2. Создать новую операцию расхода");
            System.out.println("3. Просмотреть все операции");
            System.out.println("4. Просмотреть операции по счету");
            System.out.println("5. Удалить операцию");
            System.out.println("0. Вернуться в главное меню");

            int choice = readIntInput(CHOOSE_OPTION);

            switch (choice) {
                case 1:
                    createOperation(OperationType.INCOME);
                    break;
                case 2:
                    createOperation(OperationType.EXPENSE);
                    break;
                case 3:
                    viewAllOperations();
                    break;
                case 4:
                    viewOperationsByAccount();
                    break;
                case 5:
                    deleteOperation();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println(WRONG_OPTION);
            }
        }
    }

    private void createOperation(OperationType type) {
        String operationTypeStr = (type == OperationType.INCOME) ? "ДОХОДА" : "РАСХОДА";
        System.out.println("\n--- СОЗДАНИЕ НОВОЙ ОПЕРАЦИИ " + operationTypeStr + " ---");

        // Выбор счета
        viewAllBankAccounts();
        long accountId = readLongInput("Введите ID счета: ");
        BankAccount account = facade.getBankAccountById(accountId);
        if (account == null) {
            System.out.println("Счет с ID " + accountId + NOT_FOUND);
            return;
        }

        // Выбор категории
        System.out.println("Доступные категории " + operationTypeStr.toLowerCase() + ":");
        List<Category> categories = facade.getCategoriesByType(
                (type == OperationType.INCOME) ? CategoryType.INCOME : CategoryType.EXPENSE
        );

        if (categories.isEmpty()) {
            System.out.println("Нет доступных категорий для этого типа операций.");
            System.out.println("Пожалуйста, создайте категорию сначала.");
            return;
        }

        System.out.printf("%-5s | %-30s\n", "ID", "Название");
        System.out.println("--------------------------------------");
        for (Category category : categories) {
            System.out.printf("%-5d | %-30s\n", category.getId(), category.getName());
        }

        long categoryId = readLongInput("Введите ID категории: ");
        Category selectedCategory = facade.getCategoryById(categoryId);
        if (selectedCategory == null || selectedCategory.getType() !=
                ((type == OperationType.INCOME) ? CategoryType.INCOME : CategoryType.EXPENSE)) {
            System.out.println("Неверная категория для данного типа операции.");
            return;
        }

        // Ввод суммы
        double amount = readDoubleInput("Введите сумму: ");
        if (amount <= 0) {
            System.out.println("Сумма должна быть положительной.");
            return;
        }

        // Ввод даты
        String dateStr = readStringInput("Введите дату (формат: yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, dateFormatter);
        } catch (DateTimeParseException e) {
            System.out.println("Неверный формат даты. Используйте формат yyyy-MM-dd.");
            return;
        }

        // Описание
        String description = readStringInput("Введите описание операции: ");

        // Создание операции
        Operation operation;
        if (type == OperationType.INCOME) {
            operation = facade.createIncomeOperation(accountId, amount, date, description, categoryId);
        } else {
            operation = facade.createExpenseOperation(accountId, amount, date, description, categoryId);
        }

        System.out.println("Операция успешно создана с ID: " + operation.getId());
    }

    private void viewAllOperations() {
        System.out.println("\n--- СПИСОК ВСЕХ ОПЕРАЦИЙ ---");
        List<Operation> operations = facade.getAllOperations();

        if (operations.isEmpty()) {
            System.out.println("Операции отсутствуют.");
            return;
        }

        displayOperations(operations);
    }

    private void viewOperationsByAccount() {
        viewAllBankAccounts();

        System.out.println("\n--- ПРОСМОТР ОПЕРАЦИЙ ПО СЧЕТУ ---");
        long accountId = readLongInput("Введите ID счета (0 для отмены): ");

        if (accountId == 0) {
            return;
        }

        BankAccount account = facade.getBankAccountById(accountId);
        if (account == null) {
            System.out.println("Счет с ID " + accountId + NOT_FOUND);
            return;
        }

        List<Operation> operations = facade.getOperationsByBankAccountId(accountId);

        if (operations.isEmpty()) {
            System.out.println("Операции для счета \"" + account.getName() + "\" отсутствуют.");
            return;
        }

        System.out.println("\nОперации для счета \"" + account.getName() + "\":");
        displayOperations(operations);
    }

    private void deleteOperation() {
        viewAllOperations();

        System.out.println("\n--- УДАЛЕНИЕ ОПЕРАЦИИ ---");
        long id = readLongInput("Введите ID операции для удаления (0 для отмены): ");

        if (id == 0) {
            return;
        }

        Operation operation = facade.getOperationById(id);
        if (operation == null) {
            System.out.println("Операция с ID " + id + NOT_FOUND);
            return;
        }

        String typeStr = (operation.getType() == OperationType.INCOME) ? "доход" : "расход";
        System.out.println("Вы уверены, что хотите удалить " + typeStr + " на сумму " +
                operation.getAmount() + "? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y")) {
            facade.deleteOperation(id);
            System.out.println("Операция успешно удалена.");
        } else {
            System.out.println("Удаление отменено.");
        }
    }

    private void displayOperations(List<Operation> operations) {
        System.out.printf("%-5s | %-10s | %-12s | %-15s | %-30s | %-20s\n",
                "ID", "Тип", "Дата", "Сумма", "Описание", "Категория");
        System.out.println("-------------------------------------------------------------------------------------------");

        for (Operation operation : operations) {
            String typeStr = (operation.getType() == OperationType.INCOME) ? "Доход" : "Расход";
            Category category = facade.getCategoryById(operation.getCategoryId());
            String categoryName = (category != null) ? category.getName() : "Неизвестно";

            System.out.printf("%-5d | %-10s | %-12s | %-15.2f | %-30s | %-20s\n",
                    operation.getId(), typeStr, operation.getDate(),
                    operation.getAmount(), operation.getDescription(), categoryName);
        }
    }

    // ============== АНАЛИТИКА ==============

    private void analyzeData() {
        boolean running = true;
        while (running) {
            System.out.println("\nАНАЛИТИКА:");
            System.out.println("1. Разница доходов и расходов за период");
            System.out.println("2. Группировка доходов по категориям");
            System.out.println("3. Группировка расходов по категориям");
            System.out.println("4. Статистика операций");
            System.out.println("0. Вернуться в главное меню");

            int choice = readIntInput(CHOOSE_OPTION);

            switch (choice) {
                case 1:
                    // analyzeIncomeExpenseDifference();
                    break;
                case 2:
                    // analyzeIncomeByCategories();
                    break;
                case 3:
                    // analyzeExpenseByCategories();
                    break;
                case 4:
                    // showOperationsStatistics();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println(WRONG_OPTION);
            }
        }
    }
}