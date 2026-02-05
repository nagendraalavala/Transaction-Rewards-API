package com.assessment.rewards.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer ID must not be blank")
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(nullable = false)
    private LocalDate date;

    @Min(value = 0, message = "Amount must be positive")
    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private double rewards;
}
