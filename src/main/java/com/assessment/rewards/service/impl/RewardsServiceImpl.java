package com.assessment.rewards.service.impl;

import com.assessment.rewards.entity.Transaction;
import com.assessment.rewards.exception.CustomerNotFoundException;
import com.assessment.rewards.model.RewardResponse;
import com.assessment.rewards.repository.TransactionRepository;
import com.assessment.rewards.service.RewardsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.assessment.rewards.util.DataInitializer.calculatePointsForTransaction;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardsServiceImpl implements RewardsService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<RewardResponse> getAllRewards() {
        log.info("Fetching all transactions to compute reward summaries");

        List<Transaction> allTransactions = transactionRepository.findAll();
        log.info("Total transactions retrieved: {}", allTransactions.size());

        // Group transactions by customerId
        Map<String, List<Transaction>> groupedByCustomer = allTransactions.stream()
                .filter(t -> hasText(t.getCustomerId()))
                .collect(Collectors.groupingBy(Transaction::getCustomerId));

        List<RewardResponse> rewards =  groupedByCustomer.entrySet().stream()
                .map(e -> calculateRewards(e.getKey(), e.getValue()))
                .toList();
        log.info("Total reward summaries generated: {}", rewards.size());

        return rewards;
    }

    @Override
    public RewardResponse getRewardsByCustomerId(String customerId) {
        if (!hasText(customerId)) {
            throw new IllegalArgumentException("customerId must not be blank");
        }

        log.info("Fetching rewards for customerId={}", customerId);

        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId)
                .orElseThrow(() -> {
                    log.warn("No transactions found for customerId={}", customerId);
                    return new CustomerNotFoundException("No transactions found for customerId: " + customerId);
                });

        return calculateRewards(customerId, transactions);
    }

    private RewardResponse calculateRewards(String customerId, List<Transaction> transactions) {
        Map<Month, Integer> monthly = new EnumMap<>(Month.class);

        int total = transactions.stream()
                .filter(t -> t.getDate() != null)
                .mapToInt(t -> {
                    int points = calculatePointsForTransaction(t.getAmount());
                    monthly.merge(t.getDate().getMonth(), points, Integer::sum);
                    return points;
                })
                .sum();

        if (log.isDebugEnabled()) {
            long skipped = transactions.stream().filter(t -> t.getDate() == null).count();
            if (skipped > 0) log.debug("Skipped {} transactions with null date for customerId={}", skipped, customerId);
        }

        return new RewardResponse(customerId, monthly, total);
    }


    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}
