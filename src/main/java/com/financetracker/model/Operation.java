package com.financetracker.model;

import com.financetracker.visitor.DataExportVisitor;
import com.financetracker.visitor.Visitable;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Operation implements Visitable {
    private Long id;
    private OperationType type;
    private Long bankAccountId;
    private double amount;
    private LocalDate date;
    private String description;
    private Long categoryId;

    @Override
    public void accept(DataExportVisitor visitor) {
        visitor.visit(this);
    }
}