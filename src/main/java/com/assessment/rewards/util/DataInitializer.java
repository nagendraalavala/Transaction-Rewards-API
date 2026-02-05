package com.assessment.rewards.util;

import com.assessment.rewards.entity.Transaction;
import com.assessment.rewards.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer {

    private final TransactionRepository repository;

    public DataInitializer(TransactionRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        repository.save(new Transaction(1l, "cust001", LocalDate.now().minusMonths(4).withDayOfMonth(15), 90, 40));
        repository.save(new Transaction(2l, "cust002", LocalDate.now().minusMonths(3).withDayOfMonth(10), 51, 1));
        repository.save(new Transaction(3L, "cust003", LocalDate.now().minusMonths(2).withDayOfMonth(25), 200, 350));
        repository.save(new Transaction(4l, "cust002", LocalDate.now().minusMonths(4).withDayOfMonth(30), 25, 0));
        repository.save(new Transaction(5L, "cust003", LocalDate.now().minusMonths(5).withDayOfMonth(03), 250,350 ));
    }


    public static int calculatePointsForTransaction(double amount) {
        int points = 0;
        if (amount > 100) {
            points += 2 * (int)(amount - 100);
            points += 1 * 50;  // 50 to 100
        } else if (amount > 50) {
            points += 1 * ((int)amount - 50);
        }
        return points;
    }
}
