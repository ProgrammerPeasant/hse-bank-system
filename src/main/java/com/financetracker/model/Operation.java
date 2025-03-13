package com.financetracker.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    private Long id;
    private OperationType type;
    private Long bankAccountId;
    private double amount;
    private LocalDate date;
    private String description;
    private Long categoryId;
}