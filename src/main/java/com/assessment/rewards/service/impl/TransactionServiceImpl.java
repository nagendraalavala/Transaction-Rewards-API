package com.assessment.rewards.service.impl;


import com.assessment.rewards.entity.Transaction;
import com.assessment.rewards.model.TransactionRequest;
import com.assessment.rewards.repository.TransactionRepository;
import com.assessment.rewards.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

import static com.assessment.rewards.util.DataInitializer.calculatePointsForTransaction;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction processTransaction(TransactionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        if (!hasText(request.customerId())) {
            throw new IllegalArgumentException("customerId must not be blank");
        }
        if (request.date() == null) {
            throw new IllegalArgumentException("date must not be null");
        }

        log.info("Processing transaction for customerId={}", request.customerId());

        Transaction transaction = new Transaction();
        transaction.setCustomerId(request.customerId());
        transaction.setAmount(request.amount());
        transaction.setDate(request.date());
        transaction.setRewards(calculatePointsForTransaction(request.amount()));

        Transaction saved = transactionRepository.save(transaction);
        log.info("Transaction saved | customerId={}, transactionId={}", request.customerId(), saved.getId());

        return saved;
    }


    @Override
    public List<Transaction> getFilteredTransactions(String customerId, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }

        log.info("Fetching transactions | customerId={}, page={}, size={}", customerId, page, size);

        List<Transaction> base = hasText(customerId)
                ? transactionRepository.findByCustomerId(customerId).orElseGet(List::of)
                : transactionRepository.findAll();

        // Defensive: stable ordering before manual pagination (important!)
        List<Transaction> sorted = base.stream()
                .sorted(Comparator.comparing(Transaction::getDate, Comparator.nullsLast(Comparator.naturalOrder()))
                        .thenComparing(Transaction::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        int from = Math.min(page * size, sorted.size());
        int to = Math.min(from + size, sorted.size());

        List<Transaction> pageItems = sorted.subList(from, to);
        log.info("Returning transactions | returned={}, total={}", pageItems.size(), sorted.size());

        return pageItems;
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}
