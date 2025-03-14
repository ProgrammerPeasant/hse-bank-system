package com.financetracker.visitor;

public interface Visitable {
    void accept(DataExportVisitor visitor);
}