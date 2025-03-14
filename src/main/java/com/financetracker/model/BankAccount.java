package com.financetracker.model;

import com.financetracker.visitor.DataExportVisitor;
import com.financetracker.visitor.Visitable;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount implements Visitable {
    private Long id;
    private String name;
    private double balance;

    @Override
    public void accept(DataExportVisitor visitor) {
        visitor.visit(this);
    }
}