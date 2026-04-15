package com.assessment.rewards.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TransactionRequest(

        @NotBlank(message = "Customer ID must not be blank")
        @Size(max = 50, message = "Customer ID must not exceed 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Customer ID must contain only alphanumeric characters, hyphens, or underscores")
        String customerId,

        @Min(value = 0, message = "Amount must be positive")
        double amount,

        LocalDate date
) {
}
