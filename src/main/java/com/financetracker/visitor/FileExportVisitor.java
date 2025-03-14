package com.financetracker.visitor;

import com.financetracker.model.BankAccount;
import com.financetracker.model.Category;
import com.financetracker.model.Operation;

import java.io.FileWriter;
import java.io.IOException;

public class FileExportVisitor implements DataExportVisitor {

    private final FileWriter writer;

    public FileExportVisitor(String filePath) throws IOException {
        this.writer = new FileWriter(filePath);
    }

    @Override
    public void visit(BankAccount bankAccount) {
        try {
            writer.write("BankAccount: " + bankAccount.getName() + ", Balance: " + bankAccount.getBalance() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(Category category) {
        try {
            writer.write("Category: " + category.getName() + ", Type: " + category.getType() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visit(Operation operation) {
        try {
            writer.write("Operation: " + operation.getDescription() + ", Amount: " + operation.getAmount() + ", Date: " + operation.getDate() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}