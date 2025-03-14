package com.financetracker.visitor;

import com.financetracker.model.BankAccount;
import com.financetracker.model.Category;
import com.financetracker.model.Operation;

public interface DataExportVisitor {
    void visit(BankAccount bankAccount);
    void visit(Category category);
    void visit(Operation operation);
}