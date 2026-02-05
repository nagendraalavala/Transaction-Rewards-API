package com.assessment.rewards.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record TransactionRequest(

        @NotBlank(message = "Customer ID must not be blank")
        String customerId,

        @Min(value = 0, message = "Amount must be positive")
        double amount,

        LocalDate date
) {
}